package com.rabbit.spring.proxy.staticproxy;

/**
 * ้ๆไปฃ็
 */
public class TestCount {
    public static void main(String[] args) {
        CountImpl countImpl = new CountImpl();
        CountProxy countProxy = new CountProxy(countImpl);
        countProxy.queryCount();
        countProxy.updateCount();
    }
}
