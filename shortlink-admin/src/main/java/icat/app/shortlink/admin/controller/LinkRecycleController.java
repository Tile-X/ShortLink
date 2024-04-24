package icat.app.shortlink.admin.controller;

import icat.app.shortlink.admin.remote.LinkRecycleRemoteService;
import icat.app.shortlink.admin.remote.dto.req.LinkDeleteReqDTO;
import icat.app.shortlink.admin.remote.dto.req.LinkRecycleMoveToDTO;
import icat.app.shortlink.admin.remote.dto.req.LinkRecyclePageReqDTO;
import icat.app.shortlink.admin.remote.dto.req.LinkRecycleRecoverDTO;
import icat.app.shortlink.common.convention.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/admin/v1")
public class LinkRecycleController {

    private final LinkRecycleRemoteService linkRecycleRemoteService;

    /**
     * 将指定短链接移入回收站
     */
    @PostMapping("/link-bin")
    public Result moveToLinkRecycleBin(@RequestBody LinkRecycleMoveToDTO requestParam) {
        return linkRecycleRemoteService.moveToLinkRecycleBin(requestParam);
    }

    /**
     * 分页查询回收站内的短链接
     */
    @GetMapping("/link-bin")
    public Result getLinkRecycleBinPage(@RequestBody LinkRecyclePageReqDTO requestParam) {
        return linkRecycleRemoteService.getLinkRecycleBinPage(requestParam);
    }

    /**
     * 将指定短链接从回收站移出
     */
    @PostMapping("/link-bin/recover")
    public Result recoverFromLinkRecycleBin(@RequestBody LinkRecycleRecoverDTO requestParam) {
        return linkRecycleRemoteService.recoverFromLinkRecycleBin(requestParam);
    }

    /**
     * 删除回收站内的指定短链接
     */
    @DeleteMapping("/link-bin")
    public Result deleteFromLinkRecycleBin(@RequestBody LinkDeleteReqDTO requestParam) {
        return linkRecycleRemoteService.deleteFromLinkRecycleBin(requestParam);
    }

}
