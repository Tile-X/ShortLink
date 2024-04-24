package icat.app.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import icat.app.shortlink.project.dao.entity.base.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Getter
@Setter
@TableName("t_link_browser_stats")
public class LinkBrowserStatsDO extends BaseDO {

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
     * 日期
     */
    private Date date;

    /**
     * 访问量
     */
    private Integer cnt;

    /**
     * 浏览器
     */
    private String browser;

}
