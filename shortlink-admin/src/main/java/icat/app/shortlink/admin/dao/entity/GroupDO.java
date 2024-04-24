package icat.app.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import icat.app.shortlink.admin.dao.entity.base.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *短链接分组实体
 */
@Getter
@Setter
@TableName("t_group")
@Accessors(chain = true)
public class GroupDO extends BaseDO {

    /**
     * ID
     */
    private Long id;

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
}