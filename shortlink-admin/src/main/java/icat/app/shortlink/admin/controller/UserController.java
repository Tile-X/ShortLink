package icat.app.shortlink.admin.controller;

import icat.app.shortlink.common.convention.result.Result;
import icat.app.shortlink.common.convention.result.Results;
import icat.app.shortlink.admin.dto.req.UserLoginReqDTO;
import icat.app.shortlink.admin.dto.req.UserRegisterReqDTO;
import icat.app.shortlink.admin.dto.req.UserUpdateReqDTO;
import icat.app.shortlink.admin.dto.resp.UserLoginRespDTO;
import icat.app.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/admin/v1")
public class UserController {

    private final UserService userService;

    /**
     * 注册用户
     */
    @PostMapping("/user/register")
    public Result register(@RequestBody UserRegisterReqDTO requestParam) {
        userService.register(requestParam);
        return Results.success("注册成功");
    }

    /**
     * 用户登录
     */
    @PostMapping("/user/login")
    public Result login(@RequestBody UserLoginReqDTO requestParam) {
        UserLoginRespDTO result = userService.login(requestParam);
        return Results.success(result);
    }

    /**
     * 用户退出登录
     */
    @GetMapping("/user/logout")
    public Result logout() {
        userService.logout();
        return Results.success("已退出登录");
    }

    /**
     * 修改用户信息
     */
    @PutMapping("/user")
    public Result update(@RequestBody UserUpdateReqDTO requestParam) {
        userService.updateUser(requestParam);
        return Results.success("修改成功");
    }

    /**
     * 根据用户名查询信息
     */
    @GetMapping("/user/{username}")
    public Result getUserByUsername(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }

    /**
     * 判断用户名是否可用
     */
    @GetMapping("/user/username-available")
    public Result isUsernameAvailable(@RequestParam("username") String username) {
        return Results.success(userService.isUsernameAvailable(username));
    }

    /**
     * 检测是否登录
     */
    @GetMapping("/user/check-login")
    public Result checkLogin() {
        return Results.success(userService.isUserLogin());
    }
}
