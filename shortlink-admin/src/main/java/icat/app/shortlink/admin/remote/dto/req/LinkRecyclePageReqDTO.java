package icat.app.shortlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import icat.app.shortlink.project.dao.entity.LinkDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class LinkRecyclePageReqDTO extends Page<LinkDO> {

    Set<String> groups;

}
