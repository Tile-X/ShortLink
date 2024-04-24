package icat.app.shortlink.project.controller;

import icat.app.shortlink.common.convention.result.Result;
import icat.app.shortlink.common.convention.result.Results;
import icat.app.shortlink.project.service.LinkToolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接工具控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/v1")
public class LinkToolController {

    private final LinkToolService linkToolService;

    @GetMapping("/link/title")
    public Result getLinkTitle(@RequestParam("url") String url) {
        return Results.success(linkToolService.getTitleByUrl(url));
    }

}
