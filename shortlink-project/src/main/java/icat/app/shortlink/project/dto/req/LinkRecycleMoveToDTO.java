package icat.app.shortlink.project.dto.req;

import lombok.Data;

@Data
public class LinkRecycleMoveToDTO {

    /**
     * 短链接分组标识
     */
    private String gid;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

}
