package icat.app.shortlink.project.dto.req;

import lombok.Data;

/**
 * 短链接删除请求
 */
@Data
public class LinkDeleteReqDTO {

    /**
     * 短链接分组标识
     */
    private String gid;

    /**
     * 短链接URL
     */
    private String fullShortUrl;

}
