package com.thread.system.base;

/**
 * @packageName：com.thread.system.base
 * @desrciption:
 * @author: gaowei
 * @date： 2018-12-19 16:31
 * @history: (version) author date desc
 */
public class FibonacciTest {

    public static void main(String[] args) {
        int threadNum = 50;
        Fibonacci fib = new Fibonacci(10);
        for (int i = 0; i < threadNum; i++) {
            new Thread(fib).start();
        }
    }
}
