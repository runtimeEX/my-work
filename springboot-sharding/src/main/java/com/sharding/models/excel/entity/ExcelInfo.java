package com.sharding.models.excel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-sharding
 * @Package com.sharding.models.excel.entity
 * @Description: TODO
 * @date Date : 2022年07月18日 下午2:30
 */
@Data
@TableName("tb_excel")
public class ExcelInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;
}
