package com.shiro.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.conf.ShiroRealm;
import com.shiro.entity.SysPermission;
import com.shiro.entity.SysRole;
import com.shiro.entity.SysRolePermission;
import com.shiro.mapper.SysRoleMapper;
import com.shiro.utils.SpringAppContextFacade;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysRolePermissionService sysRolePermissionService;
    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 列表
     *
     * @param sysRole 角色实体
     * @return 分页实体
     */
    public IPage<SysRole> list(SysRole sysRole) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(sysRole.getName())) {
            queryWrapper.likeRight(SysRole::getName, sysRole.getName());
        }
        queryWrapper.orderByDesc(SysRole::getCreateDate);
        return super.baseMapper.selectPage(sysRole.toPage(), queryWrapper);
    }

    /**
     * 详情
     *
     * @param roleId 角色id
     * @return 角色详情
     */
    public SysRole info(Long roleId) {
        return super.baseMapper.selectById(roleId);
    }

    /**
     * 添加
     *
     * @param sysRole 角色实体
     */
    public void add(SysRole sysRole) {
        sysRole.preInsert();
        super.baseMapper.insert(sysRole);
    }

    /**
     * 编辑
     *
     * @param sysRole 角色实体
     */
    public void update(SysRole sysRole) {
        sysRole.preUpdate();
        super.baseMapper.updateById(sysRole);
    }

    /**
     * 删除
     *
     * @param id 角色id
     */
    public void delete(Long id) {
        super.baseMapper.deleteById(id);
        //删除角色用户关系
        sysUserRoleService.deleteByRoleId(id);
        //删除角色权限关系
        sysRolePermissionService.deleteByRoleId(id);
        //清空缓存
        SpringAppContextFacade.applicationContext.getBean(ShiroRealm.class).getAuthorizationCache().clear();
    }

    /**
     * 根据一个角色id获取权限id列表
     *
     * @param roleId 角色id
     * @return 权限列表
     */
    public List<Long> permissions(Long roleId) {
        boolean isAdmin = roleId == 0;
        List<Long> permissionIdList;
        //如果是管理员用户，查询全部权限
        if (isAdmin) {
            permissionIdList = sysPermissionService.selectAll().stream().map(SysPermission::getId).collect(Collectors.toList());
        } else {
            //如果不是管理员用户，查询该用户所有角色id
            List<Long> roleIds = new ArrayList<>();
            roleIds.add(roleId);
            //根据角色id列表查询所有权限id
            permissionIdList = sysRolePermissionService.selectByRoleIdList(roleIds).stream().map(SysRolePermission::getPermissionId).collect(Collectors.toList());
        }
        return permissionIdList;
    }
}
