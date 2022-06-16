package com.shiro.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiro.utils.WebContextFacade;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.entity
 * @Description: TODO
 * @date Date : 2021年06月18日 下午3:27
 */
@Data
public class BaseEntity {
    @TableId(type = IdType.AUTO)//配置主键
    private Long id;
    private Long createBy;
    private Long updateBy;
    @TableField(fill = FieldFill.INSERT)//插入数据时更新该字段
    private LocalDateTime createDate;
    @TableField(fill = FieldFill.INSERT_UPDATE)//插入和修改时更新该字段
    private LocalDateTime updateDate;
    @TableLogic
    private Integer deleted;
    //页数
    @TableField(exist = false)
    private Integer current = 1;
    //每页数据，默认10条
    @TableField(exist = false)
    private Integer size = 10;


    public <T> IPage<T> toPage() {
        return new Page<>(current, size);
    }

    /**
     * 预插入方法
     */
    public void preInsert() {
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        Long userId = WebContextFacade.getRequestContext().getUserId();
        this.createBy = userId;
        this.updateBy = userId;
        this.deleted = 0;
    }
    /**
     * 预更新方法
     */
    public void preUpdate() {
        this.updateDate = LocalDateTime.now();
        this.updateBy = WebContextFacade.getRequestContext().getUserId();
    }
}
