package icat.app.shortlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 短链接分页请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LinkPageReqDTO extends Page {

    /**
     * 短链接分组标识
     */
    private String gid;

}
