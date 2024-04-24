package icat.app.shortlink.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import icat.app.shortlink.project.dao.entity.LinkDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短链接分页请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkPageReqDTO extends Page<LinkDO> {

    /**
     * 短链接分组标识
     */
    private String gid;

}
