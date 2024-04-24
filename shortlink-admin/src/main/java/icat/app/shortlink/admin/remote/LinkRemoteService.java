package icat.app.shortlink.admin.remote;

import icat.app.shortlink.admin.remote.dto.req.LinkCreateReqDTO;
import icat.app.shortlink.admin.remote.dto.req.LinkDeleteReqDTO;
import icat.app.shortlink.admin.remote.dto.req.LinkPageReqDTO;
import icat.app.shortlink.admin.remote.dto.req.LinkUpdateReqDTO;
import icat.app.shortlink.common.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 短链接服务远程调用
 */
@FeignClient(name = "link-service", url = "http://localhost:8000")
public interface LinkRemoteService {

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/v1/link")
    Result createShortLink(LinkCreateReqDTO requestParam);

    /**
     * 修改短链接
     */
    @PutMapping("/api/short-link/v1/link")
    Result updateShortLink(@RequestBody LinkUpdateReqDTO requestParam);

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/v1/link/page")
    Result getShortLinkPage(LinkPageReqDTO requestParam);

    /**
     * 删除短链接
     */
    @DeleteMapping("/api/short-link/v1/link")
    Result deleteShortLink(@RequestBody LinkDeleteReqDTO requestParam);

    /**
     * 查询指定分组短链接数量
     */
    @GetMapping("/api/short-link/v1/link/count")
    Result countShortLinks(@RequestParam("groups") Set<String> groups);


}
