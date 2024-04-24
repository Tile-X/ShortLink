package icat.app.shortlink.project.controller;

import icat.app.shortlink.common.convention.result.Result;
import icat.app.shortlink.common.convention.result.Results;
import icat.app.shortlink.project.dto.req.LinkCreateReqDTO;
import icat.app.shortlink.project.dto.req.LinkDeleteReqDTO;
import icat.app.shortlink.project.dto.req.LinkPageReqDTO;
import icat.app.shortlink.project.dto.req.LinkUpdateReqDTO;
import icat.app.shortlink.project.service.LinkService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 短链接信息控制层
 */
@RestController
@RequiredArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @GetMapping("/{short-uri}")
    public void restoreUrl(@PathVariable("short-uri") String shortUri, HttpServletRequest request, HttpServletResponse response) {
        linkService.restoreUrl(shortUri, request, response);
    }

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/v1/link")
    public Result createShortLink(@RequestBody LinkCreateReqDTO requestParam) {
        return Results.success(linkService.createShortLink(requestParam));
    }

    /**
     * 修改短链接
     */
    @PutMapping("/api/short-link/v1/link")
    public Result updateShortLink(@RequestBody LinkUpdateReqDTO requestParam) {
        linkService.updateShortLink(requestParam);
        return Results.success("修改成功");
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/v1/link/page")
    public Result getShortLinkPage(LinkPageReqDTO requestParam) {
        return Results.success(linkService.getShortLinkPage(requestParam));
    }

    /**
     * 删除短链接
     */
    @DeleteMapping("/api/short-link/v1/link")
    public Result deleteShortLink(@RequestBody LinkDeleteReqDTO requestParam) {
        linkService.deleteShortLink(requestParam);
        return Results.success("短链接删除成功");
    }

    /**
     * 查询指定分组短链接数量
     */
    @GetMapping("/api/short-link/v1/link/count")
    public Result countShortLinks(@RequestParam("groups") Set<String> groups) {
        return Results.success(linkService.countShortLinks(groups));
    }

}
