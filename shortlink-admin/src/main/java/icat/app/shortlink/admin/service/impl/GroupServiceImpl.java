package icat.app.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import icat.app.shortlink.admin.dao.entity.GroupDO;
import icat.app.shortlink.admin.dao.mapper.GroupMapper;
import icat.app.shortlink.admin.dto.req.GroupSortReqDTO;
import icat.app.shortlink.admin.dto.req.GroupUpdateReqDTO;
import icat.app.shortlink.admin.dto.resp.GroupRespDTO;
import icat.app.shortlink.admin.remote.LinkRemoteService;
import icat.app.shortlink.admin.service.GroupService;
import icat.app.shortlink.common.biz.user.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 分组接口实现
 */
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    private final LinkRemoteService linkRemoteService;

    @Override
    public void createGroup(String groupName) {
        String gid;
        do {
            gid = RandomUtil.randomString(8);
        } while (isGidExist(gid));
        GroupDO group = new GroupDO()
                .setGid(gid)
                .setUsername(UserContext.getUsername())
                .setGroupName(groupName);
        baseMapper.insert(group);
    }

    @Override
    public void createGroup(String username, String groupName) {
        String gid;
        do {
            gid = RandomUtil.randomString(8);
        } while (isGidExist(gid));
        GroupDO group = new GroupDO()
                .setGid(gid)
                .setUsername(username)
                .setGroupName(groupName);
        baseMapper.insert(group);
    }

    @Override
    public List<GroupRespDTO> listGroups() {
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .orderByDesc(List.of(GroupDO::getUsername, GroupDO::getUpdateTime));
        List<GroupDO> groups = baseMapper.selectList(wrapper);
        Map<?, ?> linkCount = linkRemoteService.countShortLinks(
                groups.stream().map(GroupDO::getGid).collect(Collectors.toSet())).getData(Map.class);
        List<GroupRespDTO> results = BeanUtil.copyToList(groups, GroupRespDTO.class);
        results.forEach((result) -> {
            result.setShortLinkCount((Long) linkCount.get(result.getGid()));
        });
        return results;
    }

    @Override
    public void updateGroup(GroupUpdateReqDTO requestParam) {
        LambdaUpdateWrapper<GroupDO> wrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getGid, requestParam.getGid())
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .set(GroupDO::getGroupName, requestParam.getGroupName());
        baseMapper.update(null, wrapper);
    }

    @Override
    public void deleteGroup(String gid) {
        LambdaUpdateWrapper<GroupDO> wrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .set(GroupDO::getDelFlag, 1);
        baseMapper.update(null, wrapper);
    }

    @Override
    public void sortGroup(List<GroupSortReqDTO> requestParam) {
        requestParam.forEach(sortItem -> {
            LambdaUpdateWrapper<GroupDO> wrapper = Wrappers.lambdaUpdate(GroupDO.class)
                    .eq(GroupDO::getGid, sortItem.getGid())
                    .eq(GroupDO::getUsername, UserContext.getUsername())
                    .set(GroupDO::getSortOrder, sortItem.getSortOrder());
            baseMapper.update(null, wrapper);
        });
    }

    private Boolean isGidExist(String gid) {
        LambdaQueryWrapper<GroupDO> wrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid);
        return baseMapper.exists(wrapper);
    }

}
