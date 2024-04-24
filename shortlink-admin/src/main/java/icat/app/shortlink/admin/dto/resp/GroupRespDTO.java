package icat.app.shortlink.admin.dto.resp;

import lombok.Data;

/**
 * 短链接分组查询响应
 */
@Data
public class GroupRespDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String groupName;

    /**
     * 创建分组用户名
     */
    private String username;

    /**
     * 分组排序
     */
    private Integer sortOrder;

    /**
     * 当前分组短链接数量
     */
    private Long shortLinkCount;
}
