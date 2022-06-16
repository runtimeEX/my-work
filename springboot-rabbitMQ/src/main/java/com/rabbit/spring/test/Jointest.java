package com.rabbit.spring.test;

public class Jointest extends Thread {
    private String name;

    public Jointest(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name);
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {


            Thread thread1 = new Jointest("tom");
            Thread thread2 = new Jointest("jack");

            thread2.start();
            thread2.join();//等待thread2执行完成再执行thread1
            thread1.start();
            thread1.join();
        }
    }
}
