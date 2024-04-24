package icat.app.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import icat.app.shortlink.project.dao.entity.LinkDO;
import icat.app.shortlink.project.dto.req.LinkDeleteReqDTO;
import icat.app.shortlink.project.dto.req.LinkRecycleMoveToDTO;
import icat.app.shortlink.project.dto.req.LinkRecyclePageReqDTO;
import icat.app.shortlink.project.dto.req.LinkRecycleRecoverDTO;
import icat.app.shortlink.project.dto.resp.LinkPageRespDTO;

/**
 * 短链接回收站接口
 */
public interface LinkRecycleService extends IService<LinkDO> {

    /**
     * 将短链接移入回收站
     * @param requestParam 移入回收站请求
     */
    void moveToLinkRecycleBin(LinkRecycleMoveToDTO requestParam);

    /**
     * 分页查询回收站内的短链接信息
     * @param requestParam 回收站分页查询请求
     * @return 分页查询结果
     */
    IPage<LinkPageRespDTO> getLinkRecycleBinPage(LinkRecyclePageReqDTO requestParam);

    /**
     * 从回收站内恢复链接
     * @param requestParam 需要恢复的短链接信息
     */
    void recoverFromLinkRecycleBin(LinkRecycleRecoverDTO requestParam);

    /**
     * 从回收站中删除指定短链接
     * @param requestParam 需要删除的短链接
     */
    void deleteFromLinkRecycleBin(LinkDeleteReqDTO requestParam);
}
