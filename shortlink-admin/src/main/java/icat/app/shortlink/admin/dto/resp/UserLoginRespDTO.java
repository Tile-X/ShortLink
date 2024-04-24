package icat.app.shortlink.admin.dto.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户登录响应
 */
@Data
@Accessors(chain = true)
public class UserLoginRespDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户Token
     */
    private String token;

}
