package icat.app.shortlink.project.dto.req;

import lombok.Data;

/**
 * 回收站恢复请求
 */
@Data
public class LinkRecycleRecoverDTO {

    /**
     * 短链接分组标识
     */
    private String gid;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

}
