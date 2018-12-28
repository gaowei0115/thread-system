package com.thread.system.lock;

/**
 * @packageName：com.thread.system.lock
 * @desrciption: 死锁模式实现
 * @author: gaowei
 * @date： 2018-12-28 14:28
 * @history: (version) author date desc
 */
public class DeadLockPattern {


    /**
     * 分析死锁产生原因，通过两个示例分析
     * 案例一：
     *      DeadLockA和DeadLockB模拟实现两个线程抢占锁
     *      synchronized实现代码块锁控制
     *      出现死锁的原因：两个线程各自等待对方释放自己所需要的锁
     *
     *
     *
     * 案例二：
     *      传递入参顺序不同导致死锁
     *      Transfer中transfer方法中需要两个入参，且入参作为加锁对象
     *      当两个入参传递顺序不一致时，会导致死锁情况发生。
     *
     *
     *  两种死锁调整：
     *      调整加锁顺序，保证所有加锁对象顺序一致
     *      调整加锁力度，尽量控制在最小范围内。
     */

    public static void main(String[] args) {
//        new Thread(new DeadLockA()).start();
//        new Thread(new DeadLockB()).start();
//

        final Object form = new Object();
        final Object to = new Object();
        final Transfer transfer = new Transfer();


        new Thread(new Runnable() {
            @Override
            public void run() {
                transfer.transfer(form, to);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                transfer.transfer(to, form);
            }
        }).start();
    }


    static class Transfer{

        void transfer(Object from, Object to) {
            synchronized (from) {
                System.out.println("start from");

                synchronized (to) {
                    System.out.println("start to");
                }
            }
        }
    }







    static class DeadLockA implements Runnable{

        @Override
        public void run() {
            while (true) {
                synchronized (DeadLockClient.lock1) {
                    System.out.println("DeadLockA lock lock1锁");
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {

                    }
                    System.out.println("DeadLockA trying to lock lock2锁");
                    synchronized (DeadLockClient.lock2) {
                        System.out.println("DeadLockA lock lock2锁");
                    }
                }
            }
        }
    }

    static class DeadLockB implements Runnable{
        @Override
        public void run() {
            while (true) {
                synchronized (DeadLockClient.lock2) {
                    System.out.println("DeadLockA lock lock2锁");
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {

                    }
                    System.out.println("DeadLockA trying to lock lock1锁");
                    synchronized (DeadLockClient.lock1) {
                        System.out.println("DeadLockA lock lock1锁");
                    }
                }
            }
        }
    }

    static class DeadLockClient {
        /**
         * 定义两个锁
         */
        public static Object lock1 = new Object();
        public static Object lock2 = new Object();
    }
}
