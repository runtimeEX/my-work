package com.shiro.utils.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro.utils
 * @Description: TODO
 * @date Date : 2021年06月22日 上午11:49
 */
public class BizMergeStrategy extends AbstractMergeStrategy {

    private Map<String, List<RowRangeDto>> strategyMap;
    private Sheet sheet;

    public BizMergeStrategy(Map<String, List<RowRangeDto>> strategyMap) {
        this.strategyMap = strategyMap;
    }

    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer integer) {
        this.sheet = sheet;
        //如果没有标题，只有表头的话，这里的 cell.getRowIndex() == 1
        if (cell.getRowIndex() == 2 && cell.getColumnIndex() == 0) {
            /**
             * 保证每个cell被合并一次，如果不加上面的判断，因为是一个cell一个cell操作的，
             * 例如合并A2:A3,当cell为A2时，合并A2,A3，但是当cell为A3时，又是合并A2,A3，
             * 但此时A2,A3已经是合并的单元格了
             */
            for (Map.Entry<String, List<RowRangeDto>> entry : strategyMap.entrySet()) {
                Integer columnIndex = Integer.valueOf(entry.getKey());
                entry.getValue().forEach(rowRange -> {
                    //添加一个合并请求
                    sheet.addMergedRegionUnsafe(new CellRangeAddress(rowRange.getStart(),
                            rowRange.getEnd(), columnIndex, columnIndex));
                });
            }
        }
    }


    public static Map<String, List<RowRangeDto>> addAnnualMerStrategy(List<T> projectDtoList) {
        Map<String, List<RowRangeDto>> strategyMap = new HashMap<>();
        T preUser = null;
        for (int i = 0; i < projectDtoList.size(); i++) {
            T curUser = projectDtoList.get(i);
            //如果名字一样，将名字合并（真正开发中一般不会通过名字这样字段，而是通过一些关联的唯一值，比如父id）
            if (preUser != null) {
//                if (curUser.getName() == preUser.getName()){    // 名字相同则合并第一列
////                    BizMergeStrategy.fillStrategyMap(strategyMap, "0", i+1);
//                    //如果没有标题，只有表头的话，这里为 BizMergeStrategy.fillStrategyMap(strategyMap, "1", i);
//                    BizMergeStrategy.fillStrategyMap(strategyMap, "1", i+1);
//                }
            }
            preUser = curUser;
        }
        return strategyMap;
    }
    /**
     * @description: 新增或修改合并策略map
     * @author

     * @param strategyMap
     * @param key
     * @param index
     * @since 2020/11/17 17:32
     * @Modified By:
     * @return
     */
    private static void fillStrategyMap(Map<String, List<RowRangeDto>> strategyMap, String key, int index){
        List<RowRangeDto> rowRangeDtoList = strategyMap.get(key) == null ? new ArrayList<>() : strategyMap.get(key);
        boolean flag = false;
        for (RowRangeDto dto : rowRangeDtoList) {
            //分段list中是否有end索引是上一行索引的，如果有，则索引+1
            if (dto.getEnd() == index) {
                dto.setEnd(index + 1);
                flag = true;
            }
        }
        //如果没有，则新增分段
        if (!flag) {
            rowRangeDtoList.add(new RowRangeDto(index, index + 1));
        }
        strategyMap.put(key, rowRangeDtoList);
    }

    /**
     * @description: 表格样式
     * @author

     * @since 2020/11/20 9:40
     * @Modified By:
     * @return
     */
    public static HorizontalCellStyleStrategy CellStyleStrategy(){
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //设置背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        //设置头字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short)13);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return horizontalCellStyleStrategy;
    }
}
