package com.thread.system.lock;

/**
 * @packageName：com.thread.system.lock
 * @desrciption: synchronized 同步监视器
 *                      在继承关系中体现
 * @author: gaowei
 * @date： 2018-12-28 13:54
 * @history: (version) author date desc
 */
public class SynchronizedExtends {

    /**
     * 目的：
     *      在修饰方法上
     *      验证子类复写父类synchronized方法时，同步加锁用的是否一致
     *
     *      继承模式synchronized加锁父子类使用同一个锁
     */

    public static void main(String[] args) {

        SynchronizedExtends synchronizedExtends = new SynchronizedExtends();

        final TaskRun taskRun = synchronizedExtends.new TaskRun();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                taskRun.doTask();
            }
        });

        thread.start();

        try {
            Thread.sleep(100);
        } catch (Exception e) {

        }

        taskRun.doFun();

    }



    class BaseTaskRun {

        synchronized void doTask() {
            System.out.println("baseTaskRun .. doTask()");
            try {
                Thread.sleep(1000);
                System.out.println("working....");
            } catch (Exception e) {

            }
        }
    }

    class TaskRun extends BaseTaskRun{
        @Override
        synchronized void doTask() {
            System.out.println("taskRun .. doTask()");
            super.doTask();
        }

        public synchronized void doFun() {
            System.out.println("doFun()....");
        }
    }

}
