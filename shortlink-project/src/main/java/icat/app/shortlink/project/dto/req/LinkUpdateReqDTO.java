package icat.app.shortlink.project.dto.req;

import lombok.Data;

import java.util.Date;

/**
 * 短链接修改请求
 */
@Data
public class LinkUpdateReqDTO {

    /**
     * 旧分组标识
     */
    private String oldGid;

    /**
     * 新分组标识
     */
    private String newGid;

    /*
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 原始链接
     */
    private String originUrl;

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
