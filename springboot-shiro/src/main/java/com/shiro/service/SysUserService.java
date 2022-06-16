package com.shiro.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiro.entity.SysUser;
import com.shiro.mapper.SysUserMapper;
import com.shiro.model.req.SysUserExportRes;
import com.shiro.utils.excel.EasyExcelTool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.service
 * @Description: TODO
 * @date Date : 2021年06月18日 下午4:22
 */
@Service
@Transactional
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {
    /**
     * 分页查询
     *
     * @param apSysUser 请求参数
     * @return 用户分页列表
     */
    public IPage<SysUser> page(SysUser apSysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if (apSysUser.getId() != null) {
            queryWrapper.eq(SysUser::getId, apSysUser.getId());
        }
        if (!StringUtils.isEmpty(apSysUser.getAccount())) {
            queryWrapper.eq(SysUser::getAccount, apSysUser.getAccount());
        }
        if (!StringUtils.isEmpty(apSysUser.getPassword())) {
            queryWrapper.eq(SysUser::getPassword, apSysUser.getPassword());
        }
        if (!StringUtils.isEmpty(apSysUser.getPhone())) {
            queryWrapper.eq(SysUser::getPhone, apSysUser.getPhone());
        }
        if (apSysUser.getCreateBy() != null) {
            queryWrapper.eq(SysUser::getCreateBy, apSysUser.getCreateBy());
        }
        if (apSysUser.getUpdateBy() != null) {
            queryWrapper.eq(SysUser::getUpdateBy, apSysUser.getUpdateBy());
        }
        if (apSysUser.getCreateDate() != null) {
            queryWrapper.eq(SysUser::getCreateDate, apSysUser.getCreateDate());
        }
        if (apSysUser.getUpdateDate() != null) {
            queryWrapper.eq(SysUser::getUpdateDate, apSysUser.getUpdateDate());
        }
        if (apSysUser.getDeleted() != null) {
            queryWrapper.eq(SysUser::getDeleted, apSysUser.getDeleted());
        }
        queryWrapper.orderByDesc(SysUser::getCreateDate);
        return super.baseMapper.selectPage(apSysUser.toPage(), queryWrapper);
    }

    /**
     * 查询所有列表
     *
     * @param apSysUser 请求参数
     * @return 用户分页列表
     */
    public List<SysUser> list(SysUser apSysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if (apSysUser.getId() != null) {
            queryWrapper.eq(SysUser::getId, apSysUser.getId());
        }
        if (!StringUtils.isEmpty(apSysUser.getAccount())) {
            queryWrapper.eq(SysUser::getAccount, apSysUser.getAccount());
        }
        if (!StringUtils.isEmpty(apSysUser.getPassword())) {
            queryWrapper.eq(SysUser::getPassword, apSysUser.getPassword());
        }
        if (!StringUtils.isEmpty(apSysUser.getPhone())) {
            queryWrapper.eq(SysUser::getPhone, apSysUser.getPhone());
        }
        if (apSysUser.getCreateBy() != null) {
            queryWrapper.eq(SysUser::getCreateBy, apSysUser.getCreateBy());
        }
        if (apSysUser.getUpdateBy() != null) {
            queryWrapper.eq(SysUser::getUpdateBy, apSysUser.getUpdateBy());
        }
        if (apSysUser.getCreateDate() != null) {
            queryWrapper.eq(SysUser::getCreateDate, apSysUser.getCreateDate());
        }
        if (apSysUser.getUpdateDate() != null) {
            queryWrapper.eq(SysUser::getUpdateDate, apSysUser.getUpdateDate());
        }
        if (apSysUser.getDeleted() != null) {
            queryWrapper.eq(SysUser::getDeleted, apSysUser.getDeleted());
        }
        queryWrapper.orderByDesc(SysUser::getCreateDate);
        return super.baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据条件查询详情
     *
     * @param apSysUser 请求参数
     * @return 用户详情
     */
    public SysUser query(SysUser apSysUser) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        if (apSysUser.getId() != null) {
            queryWrapper.eq(SysUser::getId, apSysUser.getId());
        }
        if (!StringUtils.isEmpty(apSysUser.getAccount())) {
            queryWrapper.eq(SysUser::getAccount, apSysUser.getAccount());
        }
        if (!StringUtils.isEmpty(apSysUser.getPassword())) {
            queryWrapper.eq(SysUser::getPassword, apSysUser.getPassword());
        }
        if (!StringUtils.isEmpty(apSysUser.getPhone())) {
            queryWrapper.eq(SysUser::getPhone, apSysUser.getPhone());
        }
        if (apSysUser.getCreateBy() != null) {
            queryWrapper.eq(SysUser::getCreateBy, apSysUser.getCreateBy());
        }
        if (apSysUser.getUpdateBy() != null) {
            queryWrapper.eq(SysUser::getUpdateBy, apSysUser.getUpdateBy());
        }
        if (apSysUser.getCreateDate() != null) {
            queryWrapper.eq(SysUser::getCreateDate, apSysUser.getCreateDate());
        }
        if (apSysUser.getUpdateDate() != null) {
            queryWrapper.eq(SysUser::getUpdateDate, apSysUser.getUpdateDate());
        }
        if (apSysUser.getDeleted() != null) {
            queryWrapper.eq(SysUser::getDeleted, apSysUser.getDeleted());
        }
        queryWrapper.last("limit 1");
        return super.getOne(queryWrapper);
    }

    /**
     * 根据主键id查询详情
     *
     * @param id 用户id
     * @return 用户详情
     */
    public SysUser queryById(Long id) {
        return super.getById(id);
    }

    /**
     * 添加用户
     *
     * @param apSysUser 实体
     * @return true/false
     */
    public boolean add(SysUser apSysUser) {
        apSysUser.preInsert();
        return super.save(apSysUser);
    }

    /**
     * 修改用户
     *
     * @param apSysUser 实体
     * @return true/false
     */
    public boolean update(SysUser apSysUser) {
        apSysUser.preUpdate();
        return super.updateById(apSysUser);
    }

    /**
     * 删除用户
     *
     * @param id 主键id
     * @return true/false
     */
    public boolean delete(Long id) {
        return super.removeById(id);
    }


    public void export(List<Long> ids, SysUser request, HttpServletResponse response) throws IOException {
        request.setCurrent(null);
        request.setSize(null);
        //   List<SysUserRe> list = super.baseMapper.selectPaListExport(request);
        List<SysUserExportRes> dataList = new ArrayList();
      /*  for (int i = 0; i < list.size(); i++) {
            SysUserExportRes sysUserExportRes = new SysUserExportRes();
            BeanUtils.copyProperties(list.get(i), sysUserExportRes);
            dataList.add(sysUserExportRes);
        }*/
        new EasyExcelTool().exportShare(dataList, response, "管理账号导出" + System.currentTimeMillis(), "管理账号导出", "管理平台账号", SysUserExportRes.class);

    }

}
