package icat.app.shortlink.project.controller;

import icat.app.shortlink.common.convention.result.Result;
import icat.app.shortlink.common.convention.result.Results;
import icat.app.shortlink.project.dto.req.LinkDeleteReqDTO;
import icat.app.shortlink.project.dto.req.LinkRecycleMoveToDTO;
import icat.app.shortlink.project.dto.req.LinkRecyclePageReqDTO;
import icat.app.shortlink.project.dto.req.LinkRecycleRecoverDTO;
import icat.app.shortlink.project.service.LinkRecycleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 回收站控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/v1")
public class LinkRecycleController {

    private final LinkRecycleService linkRecycleService;

    /**
     * 将指定短链接移入回收站
     */
    @PostMapping("/link-bin")
    public Result moveToLinkRecycleBin(@RequestBody LinkRecycleMoveToDTO requestParam) {
        linkRecycleService.moveToLinkRecycleBin(requestParam);
        return Results.success("成功移入回收站");
    }

    /**
     * 分页查询回收站内的短链接
     */
    @GetMapping("/link-bin")
    public Result getLinkRecycleBinPage(@RequestBody LinkRecyclePageReqDTO requestParam) {
        return Results.success(linkRecycleService.getLinkRecycleBinPage(requestParam));
    }

    /**
     * 将指定短链接从回收站移出
     */
    @PostMapping("/link-bin/recover")
    public Result recoverFromLinkRecycleBin(@RequestBody LinkRecycleRecoverDTO requestParam) {
        linkRecycleService.recoverFromLinkRecycleBin(requestParam);
        return Results.success("短链接恢复成功");
    }

    /**
     * 删除回收站内的指定短链接
     */
    @DeleteMapping("/link-bin")
    public Result deleteFromLinkRecycleBin(@RequestBody LinkDeleteReqDTO requestParam) {
        linkRecycleService.deleteFromLinkRecycleBin(requestParam);
        return Results.success("删除成功");
    }

}
