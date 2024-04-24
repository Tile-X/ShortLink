package icat.app.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icat.app.shortlink.common.convention.exception.ClientException;
import icat.app.shortlink.common.convention.exception.ServiceException;
import icat.app.shortlink.common.enums.ValidDateTypeEnum;
import icat.app.shortlink.project.dao.entity.LinkDO;
import icat.app.shortlink.project.dao.entity.LinkGotoDO;
import icat.app.shortlink.project.dao.mapper.*;
import icat.app.shortlink.project.dto.LinkStatsRecordDTO;
import icat.app.shortlink.project.dto.req.LinkCreateReqDTO;
import icat.app.shortlink.project.dto.req.LinkDeleteReqDTO;
import icat.app.shortlink.project.dto.req.LinkPageReqDTO;
import icat.app.shortlink.project.dto.req.LinkUpdateReqDTO;
import icat.app.shortlink.project.dto.resp.LinkCreateRespDTO;
import icat.app.shortlink.project.dto.resp.LinkPageRespDTO;
import icat.app.shortlink.project.mq.producer.LinkStatsSaveProducer;
import icat.app.shortlink.project.service.LinkService;
import icat.app.shortlink.project.util.RedisUtils;
import icat.app.shortlink.project.util.ServletUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static icat.app.shortlink.common.constant.RedisCacheConstant.*;
import static icat.app.shortlink.common.convention.errorcode.BaseErrorCode.USER_NOT_EXIST;

/**
 * 短链接信息服务接口实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDO> implements LinkService {

    private final LinkStatsSaveProducer linkStatsSaveProducer;
    private final LinkGotoMapper linkGotoMapper;
    private final RedisUtils redisUtils;
    private final RedissonClient redissonClient;
    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

    @SneakyThrows
    @Override
    public void restoreUrl(String shortUri, HttpServletRequest request, HttpServletResponse response) {
        String domain = request.getServerName();
        String fullShortUrl = domain + "/" + shortUri;

        String originalUrl = redisUtils.get(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl), String.class);
        if (StrUtil.isNotBlank(originalUrl)) {
            addShortLinkAccessStats(null, fullShortUrl, request, response);
            response.sendRedirect(originalUrl);
            return;
        }

        if (!shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl)) {
            response.sendRedirect("/error/404");
            return;
        }

        String gotoIsNull = redisUtils.get(String.format(GOTO_SHORT_IS_NULL_KEY, fullShortUrl), String.class);
        if (StrUtil.isNotBlank(gotoIsNull)) {
            response.sendRedirect("/error/404");
            return;
        }

        RLock lock = redissonClient.getLock(String.format(LOCK_GOTO_SHORT_LINK_KEY, fullShortUrl));
        lock.lock();
        try {
            originalUrl = redisUtils.get(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl), String.class);
            if (StrUtil.isNotBlank(originalUrl)) {
                response.sendRedirect(originalUrl);
                return;
            }
            LambdaQueryWrapper<LinkGotoDO> linkGotoQueryWrapper = Wrappers.lambdaQuery(LinkGotoDO.class)
                    .eq(LinkGotoDO::getFullShortUrl, fullShortUrl);
            LinkGotoDO linkGotoDO = linkGotoMapper.selectOne(linkGotoQueryWrapper);
            if (linkGotoDO == null) {
                //TODO 风控
                redisUtils.set(String.format(GOTO_SHORT_IS_NULL_KEY, fullShortUrl), "-", 30);
                response.sendRedirect("/error/404");
                return;
            }
            LambdaQueryWrapper<LinkDO> linkQueryWrapper = Wrappers.lambdaQuery(LinkDO.class)
                    .eq(LinkDO::getGid, linkGotoDO.getGid())
                    .eq(LinkDO::getFullShortUrl, fullShortUrl)
                    .eq(LinkDO::getEnableStatus, 1);
            LinkDO link = baseMapper.selectOne(linkQueryWrapper);
            if (link == null) {
                redisUtils.set(String.format(GOTO_SHORT_IS_NULL_KEY, fullShortUrl), "-", 30);
                response.sendRedirect("/error/404");
                return;
            }
            if (link.getValidDateType().equals(ValidDateTypeEnum.CUSTOM.getType())&&
                            (link.getValidDate() == null||link.getValidDate().before(new Date()))) {
                redisUtils.set(String.format(GOTO_SHORT_IS_NULL_KEY, fullShortUrl), "-", 30);
                response.sendRedirect("/error/404");
                return;
            }
            redisUtils.set(
                    String.format(GOTO_SHORT_LINK_KEY, fullShortUrl),
                    link.getOriginUrl(),
                    calculateCacheValidTime(link.getValidDateType(), link.getValidDate())
            );
            addShortLinkAccessStats(link.getGid(), fullShortUrl, request, response);
            response.sendRedirect(link.getOriginUrl());
        } finally {
            lock.unlock();
        }
    }

    @Override
    public LinkCreateRespDTO createShortLink(LinkCreateReqDTO requestParam) {
        String shortUri = generateRandomUri(requestParam.getDomain());
        String fullShortUrl = requestParam.getDomain() + "/" + shortUri;
        LinkDO link = BeanUtil.copyProperties(requestParam, LinkDO.class);
        link.setShortUri(shortUri);
        link.setEnableStatus(1);
        link.setFullShortUrl(fullShortUrl);
        LinkGotoDO linkGoto = new LinkGotoDO()
                .setGid(link.getGid())
                .setFullShortUrl(fullShortUrl);

        try {
            baseMapper.insert(link);
            linkGotoMapper.insert(linkGoto);
        } catch (DuplicateKeyException e) {
            LambdaQueryWrapper<LinkDO> wrapper = Wrappers.lambdaQuery(LinkDO.class)
                    .eq(LinkDO::getGid, link.getGid())
                    .eq(LinkDO::getFullShortUrl, fullShortUrl);
            if (baseMapper.exists(wrapper)) {
                log.warn("短链接 {} 重复入库", fullShortUrl);
                throw new ServiceException("短链接生成失败");
            }
        }

        redisUtils.set(
                String.format(GOTO_SHORT_LINK_KEY, fullShortUrl),
                link.getOriginUrl(),
                calculateCacheValidTime(link.getValidDateType(), link.getValidDate())
        );

        shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
        return BeanUtil.copyProperties(link, LinkCreateRespDTO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateShortLink(LinkUpdateReqDTO requestParam) {
        LambdaQueryWrapper<LinkDO> wrapper = Wrappers.lambdaQuery(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getOldGid())
                .eq(LinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(LinkDO::getEnableStatus, 1);
        LinkDO originLink = baseMapper.selectOne(wrapper);
        if (originLink == null) {
            throw new ClientException(USER_NOT_EXIST);
        }
        LinkDO newLink = BeanUtil.copyProperties(originLink, LinkDO.class);
        newLink.setGid(requestParam.getNewGid());
        newLink.setOriginUrl(requestParam.getOriginUrl());
        newLink.setValidDateType(requestParam.getValidDateType());
        if (ValidDateTypeEnum.PERMANENT.getType().equals(requestParam.getValidDateType())) {
            newLink.setValidDate(requestParam.getValidDate());
        } else {
            newLink.setValidDate(null);
        }
        newLink.setDescription(requestParam.getDescription());
        newLink.setCreateTime(originLink.getCreateTime());
        newLink.setUpdateTime(new Date());
        if (!Objects.equals(originLink.getGid(), newLink.getGid())) {
            baseMapper.deleteTruly(originLink.getGid(), originLink.getFullShortUrl());
            baseMapper.insert(newLink);

            LambdaUpdateWrapper<LinkGotoDO> linkGotoWrapper = Wrappers.lambdaUpdate(LinkGotoDO.class)
                    .eq(LinkGotoDO::getGid, originLink.getGid())
                    .set(LinkGotoDO::getFullShortUrl, newLink.getFullShortUrl());
            linkGotoMapper.update(null, linkGotoWrapper);
        } else {
            baseMapper.update(newLink, wrapper);
        }
        redisUtils.remove(String.format(GOTO_SHORT_LINK_KEY, newLink.getFullShortUrl()));
        redisUtils.set(
                String.format(GOTO_SHORT_LINK_KEY, newLink.getFullShortUrl()),
                newLink.getOriginUrl(),
                calculateCacheValidTime(newLink.getValidDateType(), newLink.getValidDate())
        );
    }

    @Override
    public void deleteShortLink(LinkDeleteReqDTO requestParam) {
        LambdaUpdateWrapper<LinkDO> wrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .set(LinkDO::getDelFlag, 1);
        if (baseMapper.update(null, wrapper)>=1) {
            redisUtils.remove(String.format(GOTO_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
        }
    }

    @Override
    public IPage<LinkPageRespDTO> getShortLinkPage(LinkPageReqDTO requestParam) {
        Wrapper<LinkDO> wrapper = Wrappers.lambdaQuery(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getEnableStatus, 1);
        IPage<LinkDO> results = baseMapper.selectPage(requestParam, wrapper);
        return results.convert(each -> BeanUtil.copyProperties(each, LinkPageRespDTO.class));
    }

    @Override
    public Map<String, Long> countShortLinks(Set<String> groups) {
        Map<String, Long> result = new HashMap<>();
        groups.forEach((gid) -> {
            Wrapper<LinkDO> wrapper = Wrappers.lambdaQuery(LinkDO.class)
                    .eq(LinkDO::getGid, gid);
            Long count = baseMapper.selectCount(wrapper);
            result.put(gid, count);
        });
        return result;
    }

    private String generateRandomUri(String domain) {
        int retryTimes = 10;
        while (retryTimes > 0) {
            retryTimes--;
            String shortUri = RandomUtil.randomString(6);
            if (!shortUriCreateCachePenetrationBloomFilter.contains(domain + "/" + shortUri)) {
                return shortUri;
            }
        }
        throw new ServiceException("操作过于频繁，请稍后重试");
    }

    private long calculateCacheValidTime(Integer validType, Date validDate) {
        if (validType.equals(ValidDateTypeEnum.PERMANENT.getType()) || validDate == null) {
            return 30 * 24 * 60 * 60;
        } else {
            return DateUtil.between(new Date(), validDate, DateUnit.SECOND, true);
        }
    }

    private void addShortLinkAccessStats(String gid, String fullShortUrl, HttpServletRequest request, HttpServletResponse response) {

        boolean uniqueVisitedFlag = false;
        Cookie[] cookies = request.getCookies();
        if (ArrayUtil.isNotEmpty(cookies)) {
            Optional<Cookie> cookie = Arrays.stream(cookies)
                    .filter(each -> Objects.equals(each.getName(), "uv"))
                    .findFirst();
            uniqueVisitedFlag = cookie.isEmpty();
        }

        String ip = ServletUtils.getClientIp(request);
        Long addResult = redisUtils.addSet(UNIQUE_IP_KEY, ip);
        boolean uniqueIpFlag = addResult != null && addResult > 0;

        if (!uniqueVisitedFlag) {
            String uv = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("uv", uv);
            cookie.setMaxAge(30 * 24 * 60 * 60);
            cookie.setPath(StrUtil.sub(fullShortUrl, fullShortUrl.indexOf("/"), fullShortUrl.length()));
            response.addCookie(cookie);
        }

        if (StrUtil.isBlank(gid)) {
            LambdaQueryWrapper<LinkGotoDO> wrapper = Wrappers.lambdaQuery(LinkGotoDO.class)
                    .eq(LinkGotoDO::getFullShortUrl, fullShortUrl);
            LinkGotoDO linkGoto = linkGotoMapper.selectOne(wrapper);
            gid = linkGoto.getGid();
        }

        LinkStatsRecordDTO linkStatsRecord = new LinkStatsRecordDTO()
                .setUniqueVisitor(uniqueVisitedFlag)
                .setUniqueIp(uniqueIpFlag)
                .setIp(ServletUtils.getClientIp(request))
                .setBrowser(ServletUtils.getBrowser(request))
                .setSystem(ServletUtils.getSystemInfo(request))
                .setDevice(ServletUtils.getDevice(request))
                .setDate(new Date());
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("gid", gid);
        messageMap.put("fullShortUrl", fullShortUrl);
        messageMap.put("linkStatsRecord", JSONUtil.toJsonStr(linkStatsRecord));
        linkStatsSaveProducer.send(messageMap);
    }
}
