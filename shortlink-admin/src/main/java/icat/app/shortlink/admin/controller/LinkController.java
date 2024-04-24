package icat.app.shortlink.admin.controller;

import icat.app.shortlink.admin.remote.LinkRemoteService;
import icat.app.shortlink.admin.remote.dto.req.LinkCreateReqDTO;
import icat.app.shortlink.admin.remote.dto.req.LinkDeleteReqDTO;
import icat.app.shortlink.admin.remote.dto.req.LinkPageReqDTO;
import icat.app.shortlink.admin.remote.dto.req.LinkUpdateReqDTO;
import icat.app.shortlink.common.convention.result.Result;
import icat.app.shortlink.common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/admin/v1/")
public class LinkController {

    private final LinkRemoteService linkRemoteService;

    @PostMapping("/link")
    public Result createShortLink(@RequestBody LinkCreateReqDTO requestParam) {
        return linkRemoteService.createShortLink(requestParam);
    }

    /**
     * 修改短链接
     */
    @PutMapping("/api/short-link/v1/link")
    public Result updateShortLink(@RequestBody LinkUpdateReqDTO requestParam) {
        return linkRemoteService.updateShortLink(requestParam);
    }

    /**
     * 删除短链接
     */
    @DeleteMapping("/api/short-link/v1/link")
    public Result deleteShortLink(@RequestBody LinkDeleteReqDTO requestParam) {
        return linkRemoteService.deleteShortLink(requestParam);
    }

    @GetMapping("/link/page")
    public Result getShortLinkPage(LinkPageReqDTO requestParam) {
        return linkRemoteService.getShortLinkPage(requestParam);
    }

    @GetMapping("/link/count")
    public Result getShortLinkCount(@RequestParam("groups") Set<String> groups) {
        return Results.success(linkRemoteService.countShortLinks(groups));
    }

}
