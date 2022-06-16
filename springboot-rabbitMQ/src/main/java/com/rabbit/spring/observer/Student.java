package com.rabbit.spring.observer;

import java.util.Observable;
import java.util.Observer;

public class Student implements Observer {
    private String name;

    public Student(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println((String) arg + "老师你好，" + "我的名字叫" + name);
    }

}
