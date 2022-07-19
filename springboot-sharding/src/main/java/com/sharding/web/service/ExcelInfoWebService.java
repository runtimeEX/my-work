package com.sharding.web.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.URLEncoder;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sharding.models.excel.entity.ExcelInfo;
import com.sharding.models.excel.service.ExcelInfoService;
import com.sharding.utils.ExcelUtils;
import com.sharding.web.model.response.ExcelInfoExportResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-sharding
 * @Package com.sharding.web.service
 * @Description: TODO
 * @date Date : 2022年07月18日 下午2:37
 */
@Slf4j
@Service
@Transactional
public class ExcelInfoWebService {
    @Autowired
    private ExcelInfoService excelInfoService;
    @Autowired
    private ExecutorService executorService;

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

    public void sheetExport(HttpServletResponse httpServletResponse) throws IOException {
        long start = System.currentTimeMillis();
        //查看数据总条数
        int total = excelInfoService.count();
        //总条数超出这个数量就分sheet
        Long rowMaxCount = 20000L;
        //总数超过这个数量就开启多线程查询
        Long eachCount = 20000L;
        ExcelWriter writer = ExcelUtil.getBigWriter();
        //不分sheet
        if (total <= rowMaxCount) {
            List<ExcelInfo> list = excelInfoService.list();
            writer = excelWriter(list, 0, writer);
            // response为HttpServletResponse对象
            httpServletResponse.setContentType("application/vnd.ms-excel;charset=utf-8");
            // test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.createDefault().encode("售卡订单信息", Charset.defaultCharset()) + ".xlsx");
            ServletOutputStream out = httpServletResponse.getOutputStream();
            writer.flush(out, true);
            writer.close();
            IoUtil.close(out);
            long end = System.currentTimeMillis();
            System.out.println("单线程查询数据用时:" + (end - start) + "ms");
        } else {
            //多线程分页查询
            int sheetSize = ExcelUtils.getSheetSize(Long.valueOf(total), eachCount);
            List<Callable<List<ExcelInfo>>> result = new ArrayList<>();
            for (int i = 1; i <= sheetSize; i++) {
                Page<ExcelInfo> page = new Page<>();
                page.setCurrent(i);
                page.setSize(eachCount);
                Callable<List<ExcelInfo>> excelThread = new ExcelThread(excelInfoService, page);
                result.add(excelThread);
            }
            try {
                List<Future<List<ExcelInfo>>> futures = executorService.invokeAll(result);
                for (int i = 0; i < futures.size(); i++) {
                    try {
                        List<ExcelInfo> infos = futures.get(i).get();
                        writer = excelWriter(infos, i, writer);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            httpServletResponse.setContentType("application/vnd.ms-excel;charset=utf-8");
            httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.createDefault().encode("售卡订单信息", Charset.defaultCharset()) + ".xlsx");
            ServletOutputStream out = httpServletResponse.getOutputStream();
            writer.flush(out, true);
            writer.close();
            IoUtil.close(out);
            long end = System.currentTimeMillis();
            System.out.println("线程查询数据用时:" + (end - start) + "ms");
        }
    }

    private ExcelWriter excelWriter(List<ExcelInfo> list, int sheet, ExcelWriter excelWriter) {
        List<ExcelInfoExportResponse> exportResponses = list.stream().map(item -> {
            ExcelInfoExportResponse excelInfoExportResponse = new ExcelInfoExportResponse();
            excelInfoExportResponse.setId(item.getId().toString());
            excelInfoExportResponse.setName(item.getName());
            return excelInfoExportResponse;
        }).collect(Collectors.toList());
        excelWriter.setSheet(sheet);
        excelWriter.addHeaderAlias("id", "编号");
        excelWriter.addHeaderAlias("name", "名称");
        excelWriter.write(exportResponses, true);
        return excelWriter;
    }

}
