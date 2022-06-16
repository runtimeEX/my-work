package com.rabbit;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-rabbitMQ
 * @Package com.rabbit
 * @Description: TODO
 * @date Date : 2022年01月05日 上午9:58
 */
public class MyTest {
    public static void main(String[] args) throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long start = inputFormat.parse("2021-09-03 10:21:12").getTime();
        long end = inputFormat.parse("").getTime();
        System.out.println((end-start)/1000);
    }
}
