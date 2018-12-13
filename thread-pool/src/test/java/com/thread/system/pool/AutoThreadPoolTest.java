package com.thread.system.pool;

import static org.junit.Assert.*;

/**
 * @packageName：com.thread.system.pool
 * @desrciption:
 * @author: gaowei
 * @date： 2018-12-13 10:42
 * @history: (version) author date desc
 */
public class AutoThreadPoolTest {


    public static void main(String[] args) {
        int count = 10;

        AutoThreadPool atp = new AutoThreadPool(count, Thread.NORM_PRIORITY);
        atp.setThreadPre("test");

        for (int i = 0; i < 20; i++) {
            atp.runInThread(new TaskRunnle());
        }
    }
}