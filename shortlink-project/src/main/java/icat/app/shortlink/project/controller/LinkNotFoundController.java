package icat.app.shortlink.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 短链接不存在跳转控制器
 */
@Controller
public class LinkNotFoundController {

    /**
     * 短链接不存在404页面
     */
    @RequestMapping("/error/404")
    public String notFound() {
        return "404";
    }

}
