package icat.app.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import icat.app.shortlink.project.dao.entity.LinkAccessStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * 短链接数据访问持久层
 */
public interface LinkAccessStatsMapper extends BaseMapper<LinkAccessStatsDO> {

    /**
     * 记录短链接访问信息
     */
    @Insert("""
           INSERT INTO t_link_access_stats (full_short_url, gid, date, pv, uv, uip, hour, weekday, create_time, update_time, del_flag)
           VALUES ( #{linkAccessStats.fullShortUrl}, #{linkAccessStats.gid}, #{linkAccessStats.date},
           #{linkAccessStats.pv}, #{linkAccessStats.uv}, #{linkAccessStats.uip},
           #{linkAccessStats.hour}, #{linkAccessStats.weekday},
           NOW(), NOW(), 0 ) ON DUPLICATE KEY
           UPDATE pv=pv+#{linkAccessStats.pv}, uv=uv+#{linkAccessStats.uv}, uip=uip+#{linkAccessStats.uip}, update_time=NOW();
    """)
    void addShortLinkAccessStats(@Param("linkAccessStats") LinkAccessStatsDO linkAccessStats);

}
