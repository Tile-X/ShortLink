package icat.app.shortlink.project.mq.consumer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import icat.app.shortlink.common.convention.exception.ServiceException;
import icat.app.shortlink.project.dao.entity.LinkAccessLogsDO;
import icat.app.shortlink.project.dao.entity.LinkAccessStatsDO;
import icat.app.shortlink.project.dao.entity.LinkBrowserStatsDO;
import icat.app.shortlink.project.dao.entity.LinkDeviceStatsDO;
import icat.app.shortlink.project.dao.mapper.*;
import icat.app.shortlink.project.dto.LinkStatsRecordDTO;
import icat.app.shortlink.project.mq.idempotent.MessageQueueIdempotentHandler;
import icat.app.shortlink.project.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import static icat.app.shortlink.common.constant.RedisCacheConstant.LOCK_GID_UPDATE_KEY;

/**
 * 短链接信息统计消费者
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        topic = "${rocketmq.producer.topic}",
        consumerGroup = "${rocketmq.consumer.group}"
)
public class LinkStatsSaveConsumer implements RocketMQListener<Map<String, String>> {


    private final RedisUtils redisUtils;
    private final RedissonClient redissonClient;
    private final LinkGotoMapper linkGotoMapper;
    private final LinkAccessStatsMapper linkAccessStatsMapper;
    private final LinkDeviceStatsMapper linkDeviceStatsMapper;
    private final LinkBrowserStatsMapper linkBrowserStatsMapper;
    private final LinkAccessLogsMapper linkAccessLogsMapper;
    private final MessageQueueIdempotentHandler messageQueueIdempotentHandler;


    @Override
    public void onMessage(Map<String, String> messageMap) {
        String keys = messageMap.get("keys");
        if (!messageQueueIdempotentHandler.isMessageProcessed(keys)) {
            // 判断当前的这个消息流程是否执行完成
            if (messageQueueIdempotentHandler.isAccomplished(keys)) {
                return;
            }
            throw new ServiceException("消息未完成流程，需要消息队列重试");
        }
        try {
            String fullShortUrl = messageMap.get("fullShortUrl");
            if (StrUtil.isNotBlank(fullShortUrl)) {
                String gid = messageMap.get("gid");
                LinkStatsRecordDTO statsRecord = JSON.parseObject(messageMap.get("linkStatsRecord"), LinkStatsRecordDTO.class);
                saveShortLinkStats(gid, fullShortUrl, statsRecord);
            }
        } catch (Throwable ex) {
            log.error("记录短链接监控消费异常", ex);
            throw ex;
        }
        messageQueueIdempotentHandler.putAccomplishedFlag(keys);
    }

    private void saveShortLinkStats(String gid, String fullShortUrl, LinkStatsRecordDTO record) {

        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(String.format(LOCK_GID_UPDATE_KEY, fullShortUrl));
        RLock lock = readWriteLock.readLock();
        lock.lock();
        try {
            LinkAccessStatsDO linkAccessStats = new LinkAccessStatsDO()
                    .setGid(gid)
                    .setFullShortUrl(fullShortUrl)
                    .setPv(1)
                    .setUv(record.getUniqueVisitor() ? 1 : 0)
                    .setUip(record.getUniqueIp() ? 1 : 0)
                    .setHour(DateUtil.hour(record.getDate(), true))
                    .setWeekday(DateUtil.thisDayOfWeekEnum().getIso8601Value())
                    .setDate(record.getDate());
            linkAccessStatsMapper.addShortLinkAccessStats(linkAccessStats);

            addShortLinkBrowserStats(gid, fullShortUrl, record);
            addShortLinkDeviceStats(gid, fullShortUrl, record);
            addShortLinkLogs(gid, fullShortUrl, record);
        }catch (Throwable e) {
            log.error("短链接访问量统计异常", e);
        } finally {
            lock.unlock();
        }
    }

    private void addShortLinkBrowserStats(String gid, String fullShortUrl, LinkStatsRecordDTO record) {
        Date now = new Date();
        LinkBrowserStatsDO linkBrowserStats = new LinkBrowserStatsDO()
                .setCnt(1)
                .setGid(gid)
                .setDate(now)
                .setFullShortUrl(fullShortUrl)
                .setBrowser(record.getBrowser());
        linkBrowserStatsMapper.addShortLinkBrowserState(linkBrowserStats);
    }

    private void addShortLinkDeviceStats(String gid, String fullShortUrl, LinkStatsRecordDTO record) {
        Date now = new Date();
        LinkDeviceStatsDO linkDeviceStats = new LinkDeviceStatsDO()
                .setCnt(1)
                .setGid(gid)
                .setDate(now)
                .setFullShortUrl(fullShortUrl)
                .setDevice(record.getDevice());
        linkDeviceStatsMapper.addShortLinkDeviceState(linkDeviceStats);
    }

    private void addShortLinkLogs(String gid, String fullShortUrl, LinkStatsRecordDTO record) {
        LinkAccessLogsDO linkAccessLogs = new LinkAccessLogsDO()
                .setGid(gid)
                .setFullShortUrl(fullShortUrl)
                .setOs(record.getSystem())
                .setIp(record.getIp())
                .setBrowser(record.getBrowser())
                .setUser(record.getUniqueVisitor()?"New User":"Old User");
        linkAccessLogsMapper.insert(linkAccessLogs);
    }

}
