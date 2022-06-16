package com.shiro.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.conf.ShiroRealm;
import com.shiro.entity.SysPermission;
import com.shiro.mapper.SysPermissionMapper;
import com.shiro.utils.ResultInfo;
import com.shiro.utils.SpringAppContextFacade;
import com.shiro.utils.Status;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.service
 * @Description: TODO
 * @date Date : 2021年06月18日 下午4:21
 */
@Service
@Transactional
public class SysPermissionService extends ServiceImpl<SysPermissionMapper, SysPermission> {
    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    /**
     * @return 权限列表
     */
    public List<SysPermission> selectAll() {
        return super.baseMapper.selectList(null);
    }

    /**
     * @param permissionId 权限id
     * @return 权限详情
     */
    public SysPermission info(Long permissionId) {
        return super.baseMapper.selectById(permissionId);
    }

    /**
     * 添加权限
     *
     * @param sysPermission 权限实体
     * @return ResponseEntity
     */
    public ResultInfo add(SysPermission sysPermission) {
        boolean existParentId = completeParentIds(sysPermission);
        if (!existParentId) {
            return new ResultInfo(Status.PERMISSION_PARENT_NOT_EXIST);
        }
        if (StringUtils.isNotEmpty(sysPermission.getCode())) {
            if (existCode(sysPermission.getCode())) {
                return new ResultInfo(Status.PERMISSION_CODE_EXIST);
            }
        }
        sysPermission.preInsert();
        super.baseMapper.insert(sysPermission);
        return new ResultInfo(Status.SUCCESS);
    }

    /**
     * @param sysPermission 填充parentIds
     * @return 如果parentId不存在，返回false
     */
    private boolean completeParentIds(SysPermission sysPermission) {
        if (sysPermission.getParentId() != 0) {
            //获取parentIds
            SysPermission existParent = super.baseMapper.selectById(sysPermission.getParentId());
            if (existParent == null) {
                return false;
            }
            sysPermission.setParentIds(existParent.getParentIds().concat(",").concat("[").concat(existParent.getId().toString()).concat("]"));
        } else {
            sysPermission.setParentIds("[0]");
        }
        return true;
    }


    /**
     * 判断是否已经存在code
     *
     * @param code 权限code
     * @return true or false
     */
    private boolean existCode(String code) {
        return this.getOne(new LambdaQueryWrapper<SysPermission>().eq(SysPermission::getCode, code)) != null;
    }

    /**
     * 编辑权限
     *
     * @param sysPermission 权限实体
     * @return ResponseEntity
     */
    public ResultInfo update(SysPermission sysPermission) {
        if (sysPermission.getId().equals(sysPermission.getParentId())) {
            return new ResultInfo().message("不能选择当前节点作为父级");
        }
        SysPermission oldPermission = super.baseMapper.selectById(sysPermission.getId());
        if (oldPermission == null) {
            return new ResultInfo().message("权限不存在");
        }
        boolean existParentId = this.completeParentIds(sysPermission);
        if (!existParentId) {
            return new ResultInfo(Status.PERMISSION_PARENT_NOT_EXIST);
        }
        SysPermission parentPermission = new SysPermission();
        parentPermission.setParentIds("[0]");
        if (!Long.valueOf(0).equals(sysPermission.getParentId())) {
            parentPermission = super.baseMapper.selectById(sysPermission.getParentId());
        }

        if (parentPermission == null) {
            return new ResultInfo(Status.PERMISSION_PARENT_NOT_EXIST);
        }

        if (parentPermission.getParentIds().contains(oldPermission.getParentIds())
                && parentPermission.getParentIds().length() > oldPermission.getParentIds().length()) {
            return new ResultInfo().message("不能选择子节点作为父级");
        }


        //判断要更新的code是否已经存在
        if (!StringUtils.isEmpty(sysPermission.getCode())) {
            //如果旧的code跟要更改的code一致，说明不需要修改。如果不一致，并且数据库已经存在要更改的code，返回错误
            if (!sysPermission.getCode().equals(oldPermission.getCode()) && existCode(sysPermission.getCode())) {
                return new ResultInfo(Status.PERMISSION_CODE_EXIST);
            }
        }
        sysPermission.preUpdate();
        super.baseMapper.updateById(sysPermission);

        if (!oldPermission.getParentId().equals(sysPermission.getParentId())) {
            // 处理旧的权限的全部下级的parentids
            LambdaUpdateWrapper<SysPermission> updateWrapper = Wrappers.<SysPermission>lambdaUpdate()
                    .setSql(" parent_ids = REPLACE(parent_ids, '" + oldPermission.getParentIds() + "','" + sysPermission.getParentIds() + "')")
                    .likeRight(SysPermission::getParentIds, new StringBuilder(oldPermission.getParentIds()).append(",").append("[").append(oldPermission.getId()).append("]"))
                    .ne(SysPermission::getId, oldPermission.getId());
            super.update(updateWrapper);
        }

        if (!oldPermission.getCode().equals(sysPermission.getCode())) {
            //清空缓存
            SpringAppContextFacade.applicationContext.getBean(ShiroRealm.class).getAuthorizationCache().clear();
        }

        return new ResultInfo(Status.SUCCESS);
    }

    /**
     * 删除
     *
     * @param id 权限id
     */
    public void delete(Long id) {
        //获取该用户子集
        List<Long> delList = super.baseMapper.selectList(new LambdaQueryWrapper<SysPermission>()
                .like(SysPermission::getParentIds, "[".concat(id.toString()).concat("]"))
        ).stream().map(SysPermission::getId).collect(Collectors.toList());
        delList.add(id);
        //删除所有
        super.baseMapper.deleteBatchIds(delList);
        //根据权限列表，删除角色权限关系
        sysRolePermissionService.deleteByPermissionIdList(delList);
        //清空缓存
        SpringAppContextFacade.applicationContext.getBean(ShiroRealm.class).getAuthorizationCache().clear();
    }

    /**
     * 根据id列表查询
     *
     * @param idList 权限id列表
     * @return 权限列表
     */
    public List<SysPermission> selectByIdList(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return Collections.emptyList();
        }
        return super.baseMapper.selectBatchIds(idList);
    }

    /**
     * @return 全部的权限列表
     */
    public List<SysPermission> list() {
        LambdaQueryWrapper<SysPermission> queryWrapper = new LambdaQueryWrapper<SysPermission>();
        queryWrapper.orderByAsc(SysPermission::getParentIds);
        return baseMapper.selectList(queryWrapper);
    }
}
