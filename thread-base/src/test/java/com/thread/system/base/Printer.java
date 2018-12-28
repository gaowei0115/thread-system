package com.thread.system.base;

/**
 * @packageName：com.thread.system.base
 * @desrciption:
 * @author: gaowei
 * @date： 2018-12-19 14:41
 * @history: (version) author date desc
 */
public class Printer implements Runnable {

    private static int taskCount = 0;

    private final int taskId = taskCount++;


    public Printer() {
        System.out.println("print start  taskId > " + taskId);
    }

    @Override
    public void run() {
        System.out.println("Stage 1 .... taskId > " + taskId);
        Thread.yield();
        System.out.println("Stage 2 .... taskId > " + taskId);
        Thread.yield();
        System.out.println("Stage 3 .... taskId > " + taskId);
        Thread.yield();
        System.out.println("Print end taskId > " + taskId);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new Printer()).start();
        }
    }
}
