package com.thread.system.base;

/**
 * @packageName：com.thread.system.base
 * @desrciption:
 * @author: gaowei
 * @date： 2018-12-14 14:31
 * @history: (version) author date desc
 */
public class LiftOff implements Runnable {

    private int countDown = 10;

    private static int taskCount = 0;
    private final int id = taskCount++;

    public LiftOff() {

    }

    public LiftOff(int countDown) {
        this.countDown = countDown;
    }

    public String status() {
        return "#" + id + "(" +
                (countDown > 0 ? countDown : "liftOff! ") + "), ";
    }


    @Override
    public void run() {
        while (countDown-- > 0) {
            System.out.println(status());
            Thread.yield();
        }
    }
}
