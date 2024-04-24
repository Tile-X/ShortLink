package icat.app.shortlink.admin.dto.req;

import lombok.Data;

@Data
public class GroupUpdateReqDTO {

    /**
     * 短链接分组标识
     */
    private String gid;

    /**
     * 短链接分组名称
     */
    private String groupName;

}
