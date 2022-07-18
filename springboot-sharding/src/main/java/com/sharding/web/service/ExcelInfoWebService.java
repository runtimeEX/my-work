package com.sharding.web.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.sharding.models.excel.entity.ExcelInfo;
import com.sharding.models.excel.service.ExcelInfoService;
import com.sharding.web.model.response.ExcelInfoExportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-sharding
 * @Package com.sharding.web.service
 * @Description: TODO
 * @date Date : 2022年07月18日 下午2:37
 */
@Service
@Transactional
public class ExcelInfoWebService {
    @Autowired
    private ExcelInfoService excelInfoService;

    public void add() {
        List<ExcelInfo> excelInfos = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {
            ExcelInfo excelInfo = new ExcelInfo();
            excelInfo.setName(UUID.randomUUID().toString());
            excelInfos.add(excelInfo);
        }

        excelInfoService.saveBatch(excelInfos);
    }

    public void export(HttpServletResponse httpServletResponse) throws IOException {
        List<ExcelInfo> list = excelInfoService.list();
        List<ExcelInfoExportResponse> exportResponses = list.stream().map(item -> {
            ExcelInfoExportResponse excelInfoExportResponse = new ExcelInfoExportResponse();
            excelInfoExportResponse.setId(item.getId().toString());
            excelInfoExportResponse.setName(item.getName());
            return excelInfoExportResponse;
        }).collect(Collectors.toList());
        // ExcelWriter excelWriter = ExcelUtil.getWriter(true);
        ExcelWriter excelWriter = ExcelUtil.getBigWriter();
        excelWriter.addHeaderAlias("id", "编号");
        excelWriter.addHeaderAlias("name", "名称");
        excelWriter.write(exportResponses, true);
        // response为HttpServletResponse对象
        httpServletResponse.setContentType("application/vnd.ms-excel;charset=utf-8");
        // test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.createDefault().encode("售卡订单信息", Charset.defaultCharset()) + ".xlsx");
        ServletOutputStream out = httpServletResponse.getOutputStream();
        excelWriter.flush(out, true);
        excelWriter.close();
        IoUtil.close(out);
    }
//https://blog.csdn.net/qq_26586953/article/details/109059564

}
