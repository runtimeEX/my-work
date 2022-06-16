package com.rabbit.spring.test;

/**
 * sleep()不释放锁，wait()释放锁
 * yield()的作用是让步。它能让当前线程由“运行状态”进入到“就绪状态”
 */
public class WaitTest {

    Object object = new Object();
    volatile boolean flag = false;

    public void thread1() {
        new Thread(() -> {
            synchronized (object) {
                for (int i = 1; i < 10; i++) {
                    if (i % 2 != 0) {
                        try {
                            if (flag) {
                                object.wait();
                            }
                            System.out.println(Thread.currentThread().getName() + "===" + i);
                            flag = true;
                            object.notify();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }


    public void thread2() {
        new Thread(() -> {
            synchronized (object) {
                for (int i = 1; i < 10; i++) {
                    if (i % 2 == 0) {
                        try {
                            if (!flag) {
                                object.wait();
                            }
                            System.out.println(Thread.currentThread().getName() + "===" + i);
                            flag = false;
                            object.notify();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        WaitTest test = new WaitTest();
        test.thread1();
        test.thread2();

    }
}
