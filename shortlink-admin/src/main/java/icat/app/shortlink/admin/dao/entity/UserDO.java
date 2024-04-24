package icat.app.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import icat.app.shortlink.admin.dao.entity.base.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户持久层实体
 * @author iCatOH
 */
@Getter
@Setter
@TableName("t_user")
public class UserDO extends BaseDO {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 注销时间
     */
    private Long deletionTime;
}