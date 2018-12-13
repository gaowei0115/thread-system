package com.thread.system.pool;

/**
 * @packageName：com.thread.system.pool
 * @desrciption:
 * @author: gaowei
 * @date： 2018-12-13 10:43
 * @history: (version) author date desc
 */
public class TaskRunnle implements Runnable {


    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " start...");

        System.out.println(Thread.currentThread().getName() + " 任务执行中....");

        System.out.println(Thread.currentThread().getName() + " end...");
    }
}
