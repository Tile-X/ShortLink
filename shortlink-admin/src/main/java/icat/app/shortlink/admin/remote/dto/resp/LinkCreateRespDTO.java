package icat.app.shortlink.admin.remote.dto.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 短链接创建响应
 */
@Data
@Accessors(chain = true)
public class LinkCreateRespDTO {

    /**
     * 短链接分组标识
     */
    private String gid;

    /**
     * 原始链接
     */
    private String originUrl;

    /**
     * 短链接
     */
    private String fullShortUrl;

}
