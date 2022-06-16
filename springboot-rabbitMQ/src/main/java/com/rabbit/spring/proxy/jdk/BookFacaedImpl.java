package com.rabbit.spring.proxy.jdk;

public class BookFacaedImpl implements BookFacaed {
    @Override
    public void addBook() {
        System.out.println("增加图书方法。。。");
    }
}
