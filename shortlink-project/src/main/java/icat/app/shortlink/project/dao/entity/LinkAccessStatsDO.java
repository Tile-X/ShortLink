package icat.app.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import icat.app.shortlink.project.dao.entity.base.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 短链接访问数据实体
 */
@Accessors(chain = true)
@Getter
@Setter
@TableName("t_link_access_stats")
public class LinkAccessStatsDO extends BaseDO {

    /**
     * ID
     */
    private Long id;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 日期
     */
    private Date date;

    /**
     * 访问量
     */
    private Integer pv;

    /**
     * 独立访问数
     */
    private Integer uv;

    /**
     * 独立IP数
     */
    private Integer uip;

    /**
     * 小时
     */
    private Integer hour;

    /**
     * 星期
     */
    private Integer weekday;

}