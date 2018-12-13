package com.thread.system.pool;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @packageName：com.thread.system.pool
 * @desrciption: 自定义线程池
 * @author: gaowei
 * @date： 2018-12-12 15:57
 * @history: (version) author date desc
 */
public class AutoThreadPool {


    /**
     * 默认线程池大小
     */
    private static final int DEFAULT_THREAD_POOL_COUNT = 10;

    /**
     * 默认线程组前缀
     */
    private static final String DEFAULT_THREAD_PRE = "AUTO-GROUP";

    /**
     * 线程池大小
     */
    private int count;

    /**
     * 线程池优先级，默认5
     */
    private int priority = Thread.NORM_PRIORITY;

    /**
     * 线程池组
     */
    private ThreadGroup threadGroup;

    /**
     * 线程池组前缀
     */
    private String threadPre;

    /**
     * 执行线程加锁
     */
    private final Object runnableLock = new Object();

    /**
     * 是否守护线程
     */
    private boolean makeThreadsDaemons = false;

    /**
     * 初始化一次
     */
    private boolean onceInit = true;

    /**
     * 可用线程池
     */
    private LinkedList<WorkerThread> avaliPools = new LinkedList<>();

    /**
     * 初始化线程
     */
    private List<WorkerThread> workers;


    public AutoThreadPool() {
        setCount(DEFAULT_THREAD_POOL_COUNT);
    }

    public AutoThreadPool(int threadCount, int priority) {
        setCount(threadCount);
        setPriority(priority);
    }

    /**
     * 初始化线程池
     */
    public void initialize() {
        if (workers != null && workers.size() > 0) {
            return;
        }

        /**
         * 线程组
         */
        threadGroup = Thread.currentThread().getThreadGroup();
        ThreadGroup parent = threadGroup;
        while ( !parent.getName().equals("main") ) {
            threadGroup = parent;
            parent = threadGroup.getParent();
        }
        threadGroup = new ThreadGroup(parent,  getThreadPre() + "-AutoThreadPool");

        Iterator<WorkerThread> iterator = createThreadPool(getCount()).iterator();
        while (iterator.hasNext()) {
            WorkerThread wt = iterator.next();
            wt.start();
            avaliPools.add(wt);
        }

    }

    private List<WorkerThread> createThreadPool(int count) {
        workers = new LinkedList<>();
        for (int i = 0; i < count; i++) {
            WorkerThread wt = new WorkerThread(this, threadGroup, getThreadPre() + "-" + i, getPriority(), isMakeThreadsDaemons());
            workers.add(wt);
        }
        return workers;
    }

    public boolean runInThread(Runnable runnable) {

        synchronized (runnableLock) {
            if (onceInit) {
                initialize();
            }
            onceInit = false;
        }

        if (runnable == null) {
            return false;
        }

        synchronized (runnableLock) {

            while (avaliPools.size() < 1) {
                try {
                    runnableLock.wait(500);
                } catch (Exception e) {

                }
            }

            WorkerThread wt = avaliPools.removeFirst();
            wt.run(runnable);
            runnableLock.notifyAll();
        }
        return true;
    }

    private void backPool(WorkerThread wt) {
        synchronized (runnableLock) {
            avaliPools.add(wt);
            runnableLock.notifyAll();
        }
    }



    class WorkerThread extends Thread {

        private AutoThreadPool tp;

        /**
         * 线程运行标识
         */
        private boolean run = true;

        private Runnable runnable;

        public WorkerThread(AutoThreadPool tp, ThreadGroup threadGroup, String name, int prio, boolean isDaemo) {
            super(threadGroup, name);
            this.tp = tp;
        }

        public WorkerThread(AutoThreadPool tp, ThreadGroup threadGroup, String name, int prio, boolean isDaemo, Runnable runnable) {
            super(threadGroup, name);
            this.tp = tp;
            this.runnable = runnable;
        }

        /**
         * 添加执行任务
         * @param runnable
         */
        public void run(Runnable runnable) {
            synchronized (this) {
                if (this.runnable != null) {
                    throw new IllegalArgumentException("exit run runable...");
                }
                this.runnable = runnable;
                /**
                 * add runable notify wait thread
                 */
                notifyAll();
            }
        }

        @Override
        public void run() {
            boolean shouldRun = false;
            synchronized (this) {
                shouldRun = run;
            }

            while (shouldRun) {
                try {
                    /**
                     * 无任务进来时，线程处于等待
                     */
                    synchronized (this) {
                        while (runnable == null && run) {
                            this.wait(500);
                        }
                    }

                    if (runnable != null) {
                        // 执行任务
                        runnable.run();
                    }
                } catch (Exception e) {
                    System.out.println("exception: " + e.getMessage());
                } finally {
                    synchronized (this) {
                        runnable = null;
                    }

                    if (getPriority() != tp.getPriority()) {
                        setPriority(tp.getPriority());
                    }

                    /**
                     * 回放线程池
                     */
                    backPool(this);
                }

                synchronized (this) {
                    shouldRun = run;
                }
            }
        }
    }

    public boolean isMakeThreadsDaemons() {
        return makeThreadsDaemons;
    }

    public void setMakeThreadsDaemons(boolean makeThreadsDaemons) {
        this.makeThreadsDaemons = makeThreadsDaemons;
    }

    public int getCount() {
        return count;
    }

    public int getPriority() {
        return priority;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getThreadPre() {
        return threadPre;
    }

    public void setThreadPre(String threadPre) {
        this.threadPre = threadPre;
    }
}
