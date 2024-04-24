package icat.app.shortlink.admin.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import icat.app.shortlink.common.serialize.PhoneDesensitizationSerializer;
import lombok.Data;

/**
 * 用户信息查询响应
 */
@Data
public class UserRespDTO {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    @JsonSerialize(using = PhoneDesensitizationSerializer.class)
    private String phone;

    /**
     * 邮箱
     */
    private String mail;
}
