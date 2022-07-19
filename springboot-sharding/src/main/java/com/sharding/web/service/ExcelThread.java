package com.sharding.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sharding.models.excel.entity.ExcelInfo;
import com.sharding.models.excel.service.ExcelInfoService;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-sharding
 * @Package com.sharding.web.service
 * @Description: TODO
 * @date Date : 2022年07月19日 下午3:18
 */
public class ExcelThread implements Callable<List<ExcelInfo>> {

    private ExcelInfoService excelInfoService;

    private Page<ExcelInfo> page;

    public ExcelThread(ExcelInfoService excelInfoService, Page<ExcelInfo> page) {
        this.excelInfoService = excelInfoService;
        this.page = page;
    }

    @Override
    public List<ExcelInfo> call() throws Exception {
        Page<ExcelInfo> page = excelInfoService.page(this.page);
        return page.getRecords();
    }
}
