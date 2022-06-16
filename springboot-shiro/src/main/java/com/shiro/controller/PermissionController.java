package com.shiro.controller;

import com.shiro.entity.SysPermission;
import com.shiro.service.SysPermissionService;
import com.shiro.utils.ResultInfo;
import com.shiro.utils.Status;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.controller
 * @Description: TODO
 * @date Date : 2021年06月21日 下午6:14
 */
@RestController
@RequestMapping("/perm")
public class PermissionController {
    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * @return 权限列表
     */
    @GetMapping("/list")
    @RequiresPermissions("permissions:list")
    public ResultInfo list() {
        return new ResultInfo(Status.SUCCESS, sysPermissionService.list());
    }

    /**
     * @param permissionId 权限id
     * @return 权限详情
     */
    @GetMapping("/info")
    @RequiresPermissions("permissions:info")
    public ResultInfo info(Long permissionId) {
        return new ResultInfo(Status.SUCCESS, sysPermissionService.info(permissionId));
    }

    /**
     * 新增权限
     *
     * @param request 添加权限实体
     * @return ResponseEntity
     */
    @PostMapping("/add")
    @RequiresPermissions("permissions:add")
    public ResultInfo add(@RequestBody SysPermission request) {
        return sysPermissionService.add(request);
    }

    /**
     * @param request      更新权限实体
     * @param permissionId 权限id
     * @return ResponseEntity
     */
    @PostMapping("/modify")
    @RequiresPermissions("permissions:modify")
    public ResultInfo update(@RequestBody SysPermission request, @PathVariable Long permissionId) {
        SysPermission update = new SysPermission();
        BeanUtils.copyProperties(request, update);
        update.setId(permissionId);
        return sysPermissionService.update(update);
    }

    /**
     * 删除权限
     *
     * @param permissionIds 权限id列表
     * @return ResponseEntity
     */
    @PostMapping("/delete")
    @RequiresPermissions("permissions:delete")
    public ResultInfo delete(@RequestBody List<Long> permissionIds) {
        permissionIds.forEach(item -> sysPermissionService.delete(item));
        return new ResultInfo(Status.SUCCESS);
    }
}
