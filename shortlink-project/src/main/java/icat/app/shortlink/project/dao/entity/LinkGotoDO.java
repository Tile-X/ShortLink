package icat.app.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 短链接跳转实体
 */
@Accessors(chain = true)
@Data
@TableName("t_link_goto")
public class LinkGotoDO {

    /**
     * ID
     */
    private Long id;

    /**
     * 短链接分组标识
     */
    private String gid;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

}
