package com.sharding.utils;

import java.math.BigDecimal;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-sharding
 * @Package com.sharding.utils
 * @Description: TODO
 * @date Date : 2022年07月18日 下午6:01
 */
public class ExcelUtils {
    /**
     * @param total     总条数
     * @param eachCount 每个sheet多少条
     * @return
     */
    public static int getSheetSize(Long total, Long eachCount) {
        int size = new BigDecimal(total).divide(new BigDecimal(eachCount), 1).intValue();
        int mod = new BigDecimal(total).divideAndRemainder(new BigDecimal(eachCount))[1].intValue();
        if (mod > 0) {
            size = size + 1;
        }
        return size;
    }
}
