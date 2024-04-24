package icat.app.shortlink.project.mq.producer;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 短链接信息统计生产者
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LinkStatsSaveProducer {

    private final RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.producer.topic}")
    private String topic;

    public void send(Map<String, String> producerMap) {
        String keys = UUID.randomUUID().toString();
        producerMap.put("keys", keys);
        Message<Map<String, String>> message = MessageBuilder
                .withPayload(producerMap)
                .setHeader(MessageConst.PROPERTY_KEYS, keys)
                .build();
        SendResult sendResult;
        try {
            sendResult = rocketMQTemplate.syncSend(topic, message, 3000L);
            log.info("[消息访问统计监控] 消息发送结果：{}，消息ID：{}，消息Keys：{}", sendResult.getSendStatus(), sendResult.getMsgId(), keys);
        } catch (Throwable e) {
            log.error("[消息访问统计监控] 消息发送失败，消息体：{}", JSON.toJSONString(producerMap), e);
        }
    }

}
