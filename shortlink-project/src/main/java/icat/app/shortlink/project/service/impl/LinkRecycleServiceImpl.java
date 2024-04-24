package icat.app.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icat.app.shortlink.project.dao.entity.LinkDO;
import icat.app.shortlink.project.dao.mapper.LinkMapper;
import icat.app.shortlink.project.dto.req.LinkDeleteReqDTO;
import icat.app.shortlink.project.dto.req.LinkRecycleMoveToDTO;
import icat.app.shortlink.project.dto.req.LinkRecyclePageReqDTO;
import icat.app.shortlink.project.dto.req.LinkRecycleRecoverDTO;
import icat.app.shortlink.project.dto.resp.LinkPageRespDTO;
import icat.app.shortlink.project.service.LinkRecycleService;
import icat.app.shortlink.project.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

import static icat.app.shortlink.common.constant.RedisCacheConstant.GOTO_SHORT_IS_NULL_KEY;
import static icat.app.shortlink.common.constant.RedisCacheConstant.GOTO_SHORT_LINK_KEY;

/**
 * 短链接回收站接口实现
 */
@Service
@RequiredArgsConstructor
public class LinkRecycleServiceImpl extends ServiceImpl<LinkMapper, LinkDO> implements LinkRecycleService {

    private final RedisUtils redisUtils;

    @Override
    public void moveToLinkRecycleBin(LinkRecycleMoveToDTO requestParam) {
        LambdaUpdateWrapper<LinkDO> wrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .set(LinkDO::getEnableStatus, 0);
        baseMapper.update(null, wrapper);
        redisUtils.remove(String.format(GOTO_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
    }

    @Override
    public IPage<LinkPageRespDTO> getLinkRecycleBinPage(LinkRecyclePageReqDTO requestParam) {
        LambdaQueryWrapper<LinkDO> wrapper = Wrappers.lambdaQuery(LinkDO.class)
                .in(LinkDO::getGid, requestParam.getGroups())
                .eq(LinkDO::getEnableStatus, 1);
        IPage<LinkDO> results = baseMapper.selectPage(requestParam, wrapper);
        return results.convert(each -> BeanUtil.copyProperties(each, LinkPageRespDTO.class));
    }

    @Override
    public void recoverFromLinkRecycleBin(LinkRecycleRecoverDTO requestParam) {
        LambdaUpdateWrapper<LinkDO> wrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .set(LinkDO::getEnableStatus, 1)
                .set(LinkDO::getUpdateTime, new Date());
        baseMapper.update(null, wrapper);
        redisUtils.remove(String.format(GOTO_SHORT_IS_NULL_KEY, requestParam.getFullShortUrl()));
    }

    @Override
    public void deleteFromLinkRecycleBin(LinkDeleteReqDTO requestParam) {
        LambdaUpdateWrapper<LinkDO> wrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(LinkDO::getEnableStatus, 0)
                .set(LinkDO::getDelFlag, 1);
        if (baseMapper.update(null, wrapper)>=1) {
            redisUtils.remove(String.format(GOTO_SHORT_LINK_KEY, requestParam.getFullShortUrl()));
        }
    }

}
