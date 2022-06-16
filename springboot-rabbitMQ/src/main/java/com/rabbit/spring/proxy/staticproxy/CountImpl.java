package com.rabbit.spring.proxy.staticproxy;

/**
 * 委托类(包含业务逻辑)
 */
public class CountImpl implements Count {
    @Override
    public void queryCount() {
        System.out.println("查看账户方法！");
    }

    @Override
    public void updateCount() {
        System.out.println("更新账户方法！");
    }
}
