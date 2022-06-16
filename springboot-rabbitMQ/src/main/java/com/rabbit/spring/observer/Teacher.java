package com.rabbit.spring.observer;

import java.util.Observable;

/**
 * 老师是被观察者，学生是观察者，当被观察者被调用以后，所有的观察者方法也会被调用
 */
public class Teacher extends Observable {

    private String name;

    public Teacher(String name) {
        this.name = name;
    }

    public void introduce() {
        System.out.println("我是" + name + "老师");
        setChanged();
        notifyObservers(name);
    }
}
