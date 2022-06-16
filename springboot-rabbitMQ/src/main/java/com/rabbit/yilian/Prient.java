package com.rabbit.yilian;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-rabbitMQ
 * @Package com.rabbit.yilian
 * @Description: TODO
 * @date Date : 2021年12月27日 上午11:32
 */
public class Prient {
    // 菜品集合--传入一个商品集合
    public static List<Test> testList = new ArrayList<Test>();
    public static double ZMoney=0;
    public static double YMoney=20;
    public static double SMoney=500;
    // 设置小票打印
    public static String print(){
        //字符串拼接
        StringBuilder sb=new StringBuilder();
        sb.append("<center>点菜清单\r\n</center>");
        sb.append("------------------------------------\r\n");
        sb.append("点餐员：测试打印\r\n");
        sb.append("电话：13408086368\r\n");
        sb.append("用餐时间：2015-04-09 13:01-13:30\r\n");
        sb.append("用餐地址：打印测试\r\n");
        sb.append("------------------------------------\r\n");
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<td>");
        sb.append("菜品");
        sb.append("</td>");
        sb.append("<td>");
        sb.append("单价");
        sb.append("</td>");
        sb.append("<td>");
        sb.append("小计");
        sb.append("</td>");
        sb.append("</tr>");
        for (Test test : testList) {
            ZMoney=ZMoney+(test.getMoney()*test.getNum());
            sb.append("<tr>");
            sb.append("<td>"+test.getName()+"</td>");
            sb.append("<td>"+test.getMoney()+"</td>");
            sb.append("<td>"+test.getMoney()*test.getNum()+"</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("------------------------------------\r\n");
        sb.append("合计：￥"+ZMoney+"\r\n");
        sb.append("优惠金额：￥"+YMoney+"\r\n");
        sb.append("应收：￥"+(ZMoney-YMoney)+"\r\n");
        sb.append("实收：￥"+SMoney+"\r\n");
        sb.append("找零：￥"+(SMoney-YMoney)+"\r\n");
        sb.append("收银员：打印测试\r\n");
        sb.append("<center>谢谢惠顾，欢迎下次光临！</center>");
        return sb.toString();
    }
    public static List<Test> getTestList() {
        return testList;
    }
    public static void setTestList(List<Test> testList) {
        Prient.testList = testList;
    }

    public static String x(){
        //字符串拼接
        StringBuilder sb =new StringBuilder();
        sb.append("订单号：1201823819221233\r\n");
        sb.append("下单时间：2021-04-09 13:01-13:30\r\n");
        sb.append("--------------------------------\r\n");
        sb.append("商品列表\r\n");
        sb.append("<table>");
        sb.append("<tr>");
        sb.append("<td>");
        sb.append("商品名称");
        sb.append("</td>");
        sb.append("<td>");
        sb.append("数量");
        sb.append("</td>");
        sb.append("<td>");
        sb.append("金额");
        sb.append("</td>");
        sb.append("</tr>");
        for (Test test : testList) {
            sb.append("<tr>");
            sb.append("<td>"+test.getName()+"</td>");
            sb.append("<td>"+test.getNum()+"</td>");
            sb.append("<td>"+test.getMoney()*test.getNum()+"</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("总数：99\r\n");
        sb.append("总计：￥10000.99\r\n");
        sb.append("--------------------------------\r\n");
        sb.append("买家昵称：地主家的傻儿子\r\n");
        sb.append("买家电话：18888888888\r\n");
        sb.append("配送地址：山东省济南市高新区瞬态广场\r\n");
        sb.append("联系人：李公子\r\n");
        sb.append("联系电话：13400000000\r\n");
        sb.append("买家备注：打印测试\r\n");
        sb.append("卖家备注：打印测试\r\n");
        sb.append("<QR>https://jdz.g1999.com/ghs/20220104/81c9b018440d4d17ba1dd9ec7f24d0f4.jpg</QR>\r\n");
        sb.append("<center>添加小桔，了解更多优惠！</center>\r\n");
        sb.append("\r\n");
        return sb.toString();
    }
}
