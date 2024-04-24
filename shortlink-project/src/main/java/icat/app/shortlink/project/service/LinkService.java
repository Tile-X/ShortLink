package icat.app.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import icat.app.shortlink.project.dao.entity.LinkDO;
import icat.app.shortlink.project.dto.req.LinkCreateReqDTO;
import icat.app.shortlink.project.dto.req.LinkDeleteReqDTO;
import icat.app.shortlink.project.dto.req.LinkPageReqDTO;
import icat.app.shortlink.project.dto.req.LinkUpdateReqDTO;
import icat.app.shortlink.project.dto.resp.LinkCreateRespDTO;
import icat.app.shortlink.project.dto.resp.LinkPageRespDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.Set;

/**
 * 短链接信息服务接口
 */
public interface LinkService extends IService<LinkDO> {

    /**
     * 短链接跳转
     * @param shortUri 短链接uri
     * @param request 短链接请求
     * @param response 重定向响应
     */
    void restoreUrl(String shortUri, HttpServletRequest request, HttpServletResponse response);

    /**
     * 创建短链接
     * @param requestParam 短链接创建请求
     * @return 短链接创建响应
     */
    LinkCreateRespDTO createShortLink(LinkCreateReqDTO requestParam);

    /**
     * 修改短链接信息
     * @param requestParam 短链接修改请求
     */
    void updateShortLink(LinkUpdateReqDTO requestParam);

    /**
     * 逻辑删除短链接
     * @param requestParam 短链接删除请求
     */
    void deleteShortLink(LinkDeleteReqDTO requestParam);

    /**
     * 分页查询短链接
     * @param requestParam 短链接分页查询请求
     * @return 短链接分页查询结果
     */
    IPage<LinkPageRespDTO> getShortLinkPage(LinkPageReqDTO requestParam);

    /**
     * 查询对应分组下短链接数量
     * @param groups 需要查询的分组的分组标识
     * @return gid与数量的映射
     */
    Map<String, Long> countShortLinks(Set<String> groups);
}
