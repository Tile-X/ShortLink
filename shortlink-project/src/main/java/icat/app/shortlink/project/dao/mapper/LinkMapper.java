package icat.app.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import icat.app.shortlink.project.dao.entity.LinkDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

/**
 * 短链接数据持久层
 */
public interface LinkMapper extends BaseMapper<LinkDO> {

    @Delete("DELETE FROM t_link WHERE gid=#{gid} AND full_short_url=#{fullShortUrl}")
    void deleteTruly(@Param("gid") String gid,@Param("fullShortUrl") String fullShortUrl);

}
