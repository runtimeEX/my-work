package com.shiro.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.entity
 * @Description: TODO
 * @date Date : 2021年06月18日 下午3:49
 */
@TableName("sys_permission")
@EqualsAndHashCode(callSuper = true)
@Data
public class SysPermission extends BaseEntity{
    private String name;
    private String code;
    private Long parentId;
    private String parentIds;
}
