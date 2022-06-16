package com.rabbit.yilian;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-rabbitMQ
 * @Package com.rabbit.yilian
 * @Description: TODO
 * @date Date : 2021年12月27日 上午11:33
 */
public class Test {
    // 菜品名称
    private String name;
    // 价格
    private double money;
    // 数量
    private Integer num;
    //菜品分类
    private String fenlei;
    public Test() {
        super();
    }
    public Test(String name, double money, Integer num,String fenlei) {
        super();
        this.name = name;
        this.money = money;
        this.num = num;
        this.fenlei=fenlei;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getMoney() {
        return money;
    }
    public void setMoney(double money) {
        this.money = money;
    }
    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }
    public String getFenlei() {
        return fenlei;
    }
    public void setFenlei(String fenlei) {
        this.fenlei = fenlei;
    }
}
