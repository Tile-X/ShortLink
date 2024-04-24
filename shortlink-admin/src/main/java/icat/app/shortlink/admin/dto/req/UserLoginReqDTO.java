package icat.app.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 用户登录请求
 */
@Data
public class UserLoginReqDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户登录密码
     */
    private String password;
}
