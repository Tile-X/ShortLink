package icat.app.shortlink.admin.dto.req;

import lombok.Data;

/**
 * 短链接分组创建请求
 */
@Data
public class GroupCreateReqDTO {
    /**
     * 短链接分组名称
     */
    String groupName;
}
