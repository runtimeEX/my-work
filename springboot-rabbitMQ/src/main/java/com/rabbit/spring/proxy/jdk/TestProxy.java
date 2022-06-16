package com.rabbit.spring.proxy.jdk;

/**
 * jdk动态代理（需要依靠接口实现）
 */
public class TestProxy {
    public static void main(String[] args) {
        BookFacadeProxy bookFacadeProxy = new BookFacadeProxy();
        BookFacaed bookFacaed = (BookFacaed) bookFacadeProxy.bind(new BookFacaedImpl());
        bookFacaed.addBook();
        System.out.println(bookFacaed.getClass());
    }
}
