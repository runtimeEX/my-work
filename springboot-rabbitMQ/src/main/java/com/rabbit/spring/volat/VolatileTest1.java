package com.rabbit.spring.volat;

public class VolatileTest1 extends Thread {

    volatile boolean flag = true;

    int i = 0;

    @Override
    public void run() {
        while (flag) {
            i++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        VolatileTest1 test1 = new VolatileTest1();
        test1.start();
        Thread.sleep(2000);
        System.out.println(test1.i);
        //设置成false后，线程并没有停止，还在运行。test1.flag = false;没有起作用,线程每次判断flag标记的时候是从它自己的“工作内存中”取值，而并非从主内存中取值！
        //在flag前面加上volatile关键字，强制线程每次读取该值的时候都去“主内存”中取值。在试试程序吧，已经正常退出了
        test1.flag = false;
        System.out.println(test1.i);
    }
}
