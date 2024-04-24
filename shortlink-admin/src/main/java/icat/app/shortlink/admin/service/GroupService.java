package icat.app.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import icat.app.shortlink.admin.dao.entity.GroupDO;
import icat.app.shortlink.admin.dto.req.GroupSortReqDTO;
import icat.app.shortlink.admin.dto.req.GroupUpdateReqDTO;
import icat.app.shortlink.admin.dto.resp.GroupRespDTO;

import java.util.List;

/**
 * 短链接分组服务接口
 */
public interface GroupService extends IService<GroupDO> {

    /**
     * 创建短链接分组
     * @param groupName 分组名
     */
    void createGroup(String groupName);

    /**
     * 创建短链接分组
     * @param username 用户名
     * @param groupName 分组名
     */
    void createGroup(String username, String groupName);

    /**
     * 查询用户短链接分组集合
     * @return 短链接用户分组集合
     */
    List<GroupRespDTO> listGroups();

    /**
     * 修改短链接分组
     * @param requestParam 修改短链接分组请求
     */
    void updateGroup(GroupUpdateReqDTO requestParam);

    /**
     * 删除短链接分组
     * @param gid 被删除分组的分组标识
     */
    void deleteGroup(String gid);

    /**
     * 短链接分组排序
     * @param requestParam 短链接分组排序请求
     */
    void sortGroup(List<GroupSortReqDTO> requestParam);
}
