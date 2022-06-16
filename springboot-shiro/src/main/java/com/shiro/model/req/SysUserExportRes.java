package com.shiro.model.req;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.shiro.utils.excel.LocalDateTimeConverter;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.model.req
 * @Description: TODO
 * @date Date : 2021年06月22日 上午11:39
 */
@ContentRowHeight(20)
@HeadRowHeight(25)
@ColumnWidth(25)
@Data
public class SysUserExportRes {
    @ExcelProperty(value = "账号id",converter = LongStringConverter.class)
    private Long id;

    @ExcelProperty(value = "用户名")
    private String name;

    @ExcelProperty(value = "账号")
    private String account;

    @ExcelProperty(value = "手机号")
    private String phone;

    @ExcelProperty(value = "角色名")
    private String roleName;

    @ExcelProperty(value = "创建时间" , converter = LocalDateTimeConverter.class)
    private LocalDateTime createDate;

    @ExcelProperty(value = "修改时间" , converter = LocalDateTimeConverter.class)
    private LocalDateTime updateDate;
}
