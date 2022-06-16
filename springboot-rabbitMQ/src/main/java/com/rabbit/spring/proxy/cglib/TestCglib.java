package com.rabbit.spring.proxy.cglib;

import com.rabbit.spring.proxy.jdk.BookFacaedImpl;
import net.sf.cglib.proxy.Enhancer;

public class TestCglib {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        //继承被代理类
        enhancer.setSuperclass(BookFacaedImpl.class);
        //设置回调
        enhancer.setCallback(new BookFacadeCglib());
        BookFacaedImpl facaed = (BookFacaedImpl) enhancer.create();

        facaed.addBook();
        System.out.println(facaed.getClass());
    }
}
