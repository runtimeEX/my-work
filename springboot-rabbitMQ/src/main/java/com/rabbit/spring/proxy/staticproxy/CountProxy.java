package com.rabbit.spring.proxy.staticproxy;

/**
 * 代理类(增强CountImpl实现类)
 */
public class CountProxy implements Count {

    private CountImpl countImpl;

    /**
     * 覆盖默认构造器
     * @param countImpl
     */
    public CountProxy(CountImpl countImpl) {
        this.countImpl = countImpl;
    }

    @Override
    public void queryCount() {
        System.out.println("事物处理之前");
        //调用委托类的方法
        countImpl.queryCount();
        System.out.println("事物处理之后");

    }

    @Override
    public void updateCount() {
        System.out.println("事物处理之前");
        //调用委托类的方法
        countImpl.updateCount();
        System.out.println("事物处理之后");
    }
}
