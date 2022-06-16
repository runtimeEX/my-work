package com.rabbit.spring.observer;

public class Demo {
    public static void main(String[] args) {

        Teacher tony = new Teacher("Tony");

        Student studentA = new Student("A");
        Student studentB = new Student("B");
        Student studentC = new Student("C");

        tony.addObserver(studentA);
        tony.addObserver(studentB);
        tony.addObserver(studentC);

        tony.introduce();


    }

}
