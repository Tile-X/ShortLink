package icat.app.shortlink.common.biz.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户信息实体
 */
@Data
@Accessors(chain = true)
public class UserInfo {

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 用户 Token
     */
    private String token;
}
