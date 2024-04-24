package icat.app.shortlink.admin.controller;


import icat.app.shortlink.admin.dto.req.GroupCreateReqDTO;
import icat.app.shortlink.admin.dto.req.GroupSortReqDTO;
import icat.app.shortlink.admin.dto.req.GroupUpdateReqDTO;
import icat.app.shortlink.admin.service.GroupService;
import icat.app.shortlink.common.convention.result.Result;
import icat.app.shortlink.common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/admin/v1")
public class GroupController {

    private final GroupService groupService;

    /**
     * 创建短链接分组
     */
    @PostMapping("/group")
    public Result createGroup(@RequestBody GroupCreateReqDTO requestParam) {
        groupService.createGroup(requestParam.getGroupName());
        return Results.success("分组创建成功");
    }

    /**
     * 查询短链接分组集合
     */
    @GetMapping("/group")
    public Result listGroups() {
        return Results.success(groupService.listGroups());
    }

    /**
     * 修改短链接分组
     */
    @PutMapping("/group")
    public Result updateGroup(@RequestBody GroupUpdateReqDTO requestParam) {
        groupService.updateGroup(requestParam);
        return Results.success("修改成功");
    }

    /**
     * 删除短链接分组
     */
    @DeleteMapping("/group")
    public Result deleteGroup(@RequestParam("gid") String gid) {
        groupService.deleteGroup(gid);
        return Results.success("删除成功");
    }

    /**
     * 短链接分组排序
     */
    @PostMapping("/group/sort")
    public Result sortGroup(@RequestBody List<GroupSortReqDTO> requestParam) {
        groupService.sortGroup(requestParam);
        return Results.success("更新成功");
    }

}
