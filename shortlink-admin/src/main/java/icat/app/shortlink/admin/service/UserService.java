package icat.app.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import icat.app.shortlink.admin.dao.entity.UserDO;
import icat.app.shortlink.admin.dto.req.UserLoginReqDTO;
import icat.app.shortlink.admin.dto.req.UserRegisterReqDTO;
import icat.app.shortlink.admin.dto.req.UserUpdateReqDTO;
import icat.app.shortlink.admin.dto.resp.UserLoginRespDTO;
import icat.app.shortlink.admin.dto.resp.UserRespDTO;

/**
 * 用户服务接口
 */
public interface UserService extends IService<UserDO> {

    /**
     * 注册用户
     * @param requestParam 注册请求
     */
    void register(UserRegisterReqDTO requestParam);

    /**
     * 用户登录
     * @param requestParam 用户登录请求
     * @return 用户登录响应
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * 用户退出登录
     */
    void logout();

    /**
     * 修改用户信息
     * @param requestParam 修改请求
     */
    void updateUser(UserUpdateReqDTO requestParam);

    /**
     * 通过用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    UserRespDTO getUserByUsername(String username);

    /**
     * 检测用户名是否可用
     * @param username 用户名
     * @return 用户名是否可用, 可用为true, 不可用为false
     */
    Boolean isUsernameAvailable(String username);

    /**
     * 检测用户是否登录
     * @return 用户登录状态
     */
    Boolean isUserLogin();
}
