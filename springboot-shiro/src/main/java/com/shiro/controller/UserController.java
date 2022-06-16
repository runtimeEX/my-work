package com.shiro.controller;

import com.shiro.entity.SysUser;
import com.shiro.entity.SysUserRole;
import com.shiro.service.SysUserRoleService;
import com.shiro.service.SysUserService;
import com.shiro.utils.ResultInfo;
import com.shiro.utils.Status;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.controller
 * @Description: TODO
 * @date Date : 2021年06月18日 下午5:52
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @GetMapping("/add")
    @RequiresPermissions("user:add")
    public ResultInfo add(SysUser user) {
        user.setSalt(UUID.randomUUID().toString());
        ByteSource salt = ByteSource.Util.bytes(user.getSalt());
        SimpleHash simpleHash = new SimpleHash("md5", user.getPassword(), salt, 6);
        user.setPassword(simpleHash.toString());
        sysUserService.add(user);
        return new ResultInfo(Status.SUCCESS);
    }

    /**
     * @return 用户分页列表
     */
    @GetMapping("/list")
    @RequiresPermissions("user:list")
    public ResultInfo list(SysUser sysUserRe) {
        return new ResultInfo(Status.SUCCESS,sysUserService.list(sysUserRe));
    }

    /**
     * @param userId 用户id
     * @return 用户详情
     */
    @GetMapping("/{userId}")
    @RequiresPermissions("users:info")
    public ResultInfo info(@PathVariable Long userId) {
        return new ResultInfo(Status.SUCCESS,sysUserService.queryById(userId));
    }

    /**
     * 编辑用户
     */
    @PostMapping("/update")
    @RequiresPermissions("users:modify")
    public ResultInfo updateSta(@RequestBody SysUser sysUserUpdateReq) {
        sysUserService.update(sysUserUpdateReq);
        return new ResultInfo(Status.SUCCESS);
    }


    /**
     * 删除用户
     *
     * @param userIds 用户id列表
     * @return ResponseEntity
     */
    @PostMapping("/delete")
    @RequiresPermissions("users:delete")
    public ResultInfo delete(@RequestBody List<Long> userIds) {
        userIds.forEach(item -> {
            if (item == 0L) {
                return;
            }
            sysUserService.delete(item);
        });
        return new ResultInfo(Status.SUCCESS);
    }

    /**
     * 批量选择角色
     *
     * @param list   角色列表 id
     * @param userId 用户id
     * @return ResponseEntity
     */
    @PostMapping("/{userId}/roles/choose")
    @RequiresPermissions("users:roles:choose")
    public ResultInfo chooseRoles(@Validated @RequestBody List<SysUserRole> list, @PathVariable Long userId) {
        sysUserRoleService.batchChoose(list, userId);
        return new ResultInfo(Status.SUCCESS);
    }

    /**
     * 获取用户的角色id列表
     *
     * @param userId 用户id
     * @return 角色ids列表
     */
    @GetMapping("/{userId}/roles")
    @RequiresPermissions("users:roles:list")
    public ResultInfo roles(@PathVariable Long userId) {
        return new ResultInfo(Status.SUCCESS,sysUserRoleService.roles(userId));
    }


    /**
     * 导出账号列表
     */
    @GetMapping("/export")
    @RequiresPermissions("users:export")
    public ResultInfo export( List<Long> ids,SysUser request , HttpServletResponse response) throws IOException {
         sysUserService.export(ids,request,response);
         return new ResultInfo(Status.SUCCESS);
    }

}
