package com.rabbit.yilian;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-rabbitMQ
 * @Package com.rabbit.yilian
 * @Description: TODO
 * @date Date : 2021年12月27日 上午11:11
 */
public class TestP {
    public static void main(String[] args) {
        String k="2/4";
        String[] split = k.split("[/]");
        System.out.println(split[0]);
        //测试数据
        List<Test> testList = new ArrayList<Test>();
        Test t1 = new Test(null, 23.00, 1,null);
        Test t2 = new Test("麻辣牛肉", 23.00, 2,"2");
        Test t3 = new Test("精品千层肚", 24.00, 3,"3");
        Test t4 = new Test("麻辣牛肉", 23.00, 2,"1");
        Test t5 = new Test("极品鲜毛肚", 26.00, 2,"1");
        Test t6 = new Test("极品鲜毛肚", 26.00, 1,"2");
        Test t7 = new Test("极品鲜毛肚", 26.00, 3,"2");
        Test t8 = new Test("极品鲜毛肚", 26.00, 1,"1");
        Test t9 = new Test("极品鲜毛肚", 26.00, 2,"3");
        testList.add(t1);
        testList.add(t2);
        testList.add(t3);
        testList.add(t4);
        testList.add(t5);
        testList.add(t6);
        testList.add(t7);
        testList.add(t8);
        testList.add(t9);
        Prient.setTestList(testList);
        //初始化控制器类
        Methods m=Methods.getInstance();
        //初始化终端信息
        m.init("1070250485", "9f8e61a27244e34bc8a972b91e2a8b96");

        //获取token
        String freedomToken = m.getFreedomToken();
        System.out.println(freedomToken);
        String s = m.deletePrinter("4004782164");
        System.out.println(s);
        //刷新token
     //   String refreshToken = m.refreshToken();
     //   String s = m.addPrinter("4004782164", "254934710987");
        //打印
        //终端编号     打印内容    订单号
        //生成6位随机数
        Integer random6 = (int) ((Math.random() * 9 + 1) * 100000);
        String icon = m.setIcon("4004782164", "https://jdz.g1999.com/ghs/20220104/ba15327068a74d53a5d0a91c793b049b.jpg");
        System.out.println(icon);
        for (int i=0;i<2;i++){
            String url=m.print("4004782164", Prient.x(), "Z"+System.currentTimeMillis()+random6.toString());
            System.out.println(url);
            System.out.println("--------");
        }

    }
}
