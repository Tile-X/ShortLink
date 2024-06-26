package icat.app.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import icat.app.shortlink.project.dao.entity.base.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 短链接数据实体
 */
@Getter
@Setter
@TableName("t_link")
public class LinkDO extends BaseDO {

    /**
     * ID
     */
    private Long id;

    /**
     * 域名
     */
    private String domain;

    /**
     * 短链接
     */
    private String shortUri;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 网站图标
     */
    private String favicon;

    /**
     * 点击量
     */
    private Integer clickNum;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 启用标识 0：未启用 1：已启用
     */
    private Integer enableStatus;

    /**
     * 创建类型 0：控制台 1：接口
     */
    private Integer createdType;

    /**
     * 有效期类型 0：永久有效 1：用户自定义
     */
    private Integer validDateType;

    /**
     * 有效期
     */
    private Date validDate;

    /**
     * 描述
     */
    private String description;
}