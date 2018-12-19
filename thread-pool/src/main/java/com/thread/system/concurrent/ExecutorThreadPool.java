package com.thread.system.concurrent;

import java.util.concurrent.*;

/**
 * @packageName：com.thread.system.concurrent
 * @desrciption:
 * @author: gaowei
 * @date： 2018-12-13 15:31
 * @history: (version) author date desc
 */
public class ExecutorThreadPool {


    public static void main(String[] args) {
        int poolSize = 10;
        ExecutorService executorService = createThreadPool(poolSize);
        for (int i = 0; i < 11; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    String groupName = Thread.currentThread().getThreadGroup().getName();
                    String threadName = Thread.currentThread().getName();
                    System.out.println(groupName + "-" + threadName + " 开始执行...");
                }
            });
        }

        executorService.shutdown();
    }

    /**
     * pool
     * @param poolSize
     * @return
     */
    public static ExecutorService createThreadPool(int poolSize) {
        return new ThreadPoolExecutor(poolSize, 100, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), Executors.defaultThreadFactory());
    }
}
