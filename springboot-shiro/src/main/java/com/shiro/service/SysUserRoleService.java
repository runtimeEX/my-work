package com.shiro.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.conf.ShiroRealm;
import com.shiro.entity.SysUserRole;
import com.shiro.mapper.SysUserRoleMapper;
import com.shiro.utils.SpringAppContextFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.service
 * @Description: TODO
 * @date Date : 2021年06月18日 下午4:23
 */
@Service
@Transactional
public class SysUserRoleService extends ServiceImpl<SysUserRoleMapper, SysUserRole> {
    /**
     * @param sysUserRole 用户角色关系实体
     * @return 用户角色列表
     */
    public List<SysUserRole> list(SysUserRole sysUserRole) {
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        if (sysUserRole.getRoleId() != null) {
            queryWrapper.eq(SysUserRole::getRoleId, sysUserRole.getRoleId());
        }
        if (sysUserRole.getUserId() != null) {
            queryWrapper.eq(SysUserRole::getUserId, sysUserRole.getUserId());
        }
        return super.baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据角色id删除用户角色关系
     *
     * @param roleId 角色id
     */
    public void deleteByRoleId(Long roleId) {
        super.baseMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getRoleId, roleId));
    }

    /**
     * 根据用户id删除用户角色关系
     *
     * @param userId 用户id
     */
    public void deleteByUserId(Long userId) {
        super.baseMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
    }

    /**
     * 批量选择用户的角色
     *
     * @param list   角色列表
     * @param userId 用户id
     */
    public void batchChoose(List<SysUserRole> list, Long userId) {
        //根据用户删除角色用户关系表
        this.deleteByUserId(userId);
        //清空缓存
        SpringAppContextFacade.applicationContext.getBean(ShiroRealm.class).getAuthorizationCache().clear();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(item -> {
            item.setUserId(userId);
            item.preInsert();
        });
        //插入新的用户角色关系
        super.saveBatch(list);
    }

    /**
     * 根据用户id获取角色id列表
     *
     * @param userId 用户id
     * @return 角色id列表
     */
    public List<Long> roles(Long userId) {
        return super.baseMapper.selectList(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId)).stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
    }
}
