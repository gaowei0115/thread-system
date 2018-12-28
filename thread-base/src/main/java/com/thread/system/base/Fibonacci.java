package com.thread.system.base;

import java.util.Arrays;

/**
 * @packageName：com.thread.system.base
 * @desrciption:
 * @author: gaowei
 * @date： 2018-12-19 15:24
 * @history: (version) author date desc
 */
public class Fibonacci implements Generator<Integer>, Runnable {

    private int count;
    private final int n;

    public Fibonacci(int n) {
        this.n = n;
    }

    private int fib(int n) {
        if (n < 2) {
            return 1;
        }
        return fib(n - 2) + fib(n - 1);
    }



    @Override
    public Integer next() {
        return fib(count++);
    }

    @Override
    public void run() {
        Integer[] sequence = new Integer[n];
        synchronized (this) {
            for (int i = 0; i < n; i++) {
                sequence[i] = next();
            }
            count = 0;
        }
        System.out.println(Thread.currentThread().getName() + " >> " + "Seq.of " + n + ": " + Arrays.toString(sequence));
    }
}
