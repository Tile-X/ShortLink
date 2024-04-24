package icat.app.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import icat.app.shortlink.project.dao.entity.base.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@Setter
@TableName("t_link_access_logs")
public class LinkAccessLogsDO extends BaseDO {
    /**
     * ID
     */
    private Long id;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 用户信息
     */
    private String user;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * IP
     */
    private String ip;
}
