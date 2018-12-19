package com.thread.system.base;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @packageName：com.thread.system.base
 * @desrciption:
 * @author: gaowei
 * @date： 2018-12-14 14:35
 * @history: (version) author date desc
 */
public class LiftOffTest {

    public static void main(String[] args) {
        int count = 10;
        /**
         * 线程池操作
         */
        ThreadPoolExecutor executor = createThreadPool(count, 100);

        for (int i = 0; i < count; i++) {
            executor.execute(new LiftOff());
        }

        executor.shutdown();


        System.out.println("validte begin: start..");
        for (int i = 0; i < 100; i++) {
            System.out.println("----------------------- >> " + i);
            if (i == 10) {
                break ;
            }
        }

        System.out.println("validte begin: end..");

    }

    private static ThreadPoolExecutor createThreadPool(int coreSize, int maxSize) {
        return new ThreadPoolExecutor(coreSize, maxSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), Executors.defaultThreadFactory());
    }
}