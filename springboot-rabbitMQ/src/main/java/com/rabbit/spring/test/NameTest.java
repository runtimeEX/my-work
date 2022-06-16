package com.rabbit.spring.test;

import java.util.Date;

/**
 * 由于getClass()在Object类中定义成了final，子类不能覆盖该方法，所以，在name方法中调用getClass().getName()方法，
 * 其实就是在调用从父类继承的getClass()方法，等效于调用super.getClass().getName()方法，
 * 所以，super.getClass().getName()方法返回的也应该是NameTest。
 */
public class NameTest extends Date {

    public void name(){
        System.out.println(getClass().getName());
    }

    public void fatherName(){
        System.out.println(getClass().getSuperclass().getName());
    }
    public static void main(String[] args) {
        NameTest nameTest = new NameTest();
        nameTest.name();
        nameTest.fatherName();
    }
}
