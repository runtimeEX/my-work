package com.shiro.utils.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.shiro.utils.ResultInfo;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.utils
 * @Description: TODO
 * @date Date : 2021年06月22日 上午11:46
 */
public class EasyExcelTool {
    public ResultInfo exportShare(List dataList, HttpServletResponse response, String fileName, String title, String sheetName, Class t) throws IOException {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setCharacterEncoding("utf-8");
        String fileNames = URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileNames + ".xlsx");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        int row = 0;
        if (dataList.size() > 0) {
            if (dataList.get(0).getClass().getDeclaredFields().length == 1) {
                row = dataList.get(0).getClass().getDeclaredFields().length;
            } else {
                row = dataList.get(0).getClass().getDeclaredFields().length - 1;
            }
        } else {
            EasyExcel.write(response.getOutputStream(), t)
                    .excelType(ExcelTypeEnum.XLSX).head(t)
                    .registerWriteHandler(new TitleSheetWriteHandler(title, t.getClass().getDeclaredFields().length - 1)) // 标题及样式，lastCol为标题第0列到底lastCol列的宽度
                    //设置默认样式及写入头信息开始的行数
                    .relativeHeadRowIndex(1)
                    .sheet(sheetName)
                    .registerWriteHandler(new Custemhandler()) //
                    .registerWriteHandler(BizMergeStrategy.CellStyleStrategy())
                    .doWrite(null);
            return null;
        }

        EasyExcel.write(response.getOutputStream(), t)
                .excelType(ExcelTypeEnum.XLSX).head(t)
                .registerWriteHandler(new TitleSheetWriteHandler(title, row)) // 标题及样式，lastCol为标题第0列到底lastCol列的宽度
                //设置默认样式及写入头信息开始的行数
                .relativeHeadRowIndex(1)
                .sheet(sheetName)
                .registerWriteHandler(new Custemhandler()) //
                .registerWriteHandler(BizMergeStrategy.CellStyleStrategy())
                .doWrite(dataList);
        return null;
    }
}
