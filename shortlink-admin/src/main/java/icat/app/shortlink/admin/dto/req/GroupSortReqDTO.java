package icat.app.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 短链接排序请求
 */
@Data
public class GroupSortReqDTO {
    /**
     * 短链接分组标识
     */
    private String gid;
    /**
     * 短链接分组排序索引
     */
    private Integer sortOrder;
}
