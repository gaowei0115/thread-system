package com.thread.system.lock;

/**
 * @packageName：com.thread.system.lock
 * @desrciption: 监视器锁
 * @author: gaowei
 * @date： 2018-12-28 9:37
 * @history: (version) author date desc
 */
public class SynchronziedLock {


    /**
     * synchronized JVM(java虚拟机)内部实现的同步机制，类似于监视器
     *  基于进入和退出Monitor对象实现，通过monitorEnter和monitorExit指令
     *
     *  同步代码块：monitorenter指令插入到同步代码块开始位置，monitorexit指令插入到同步代码块结束位置，JVM需要维护每个monitorenter对应一个唯一的monitorexit指令。
     *              任何对象都有monitor相关联，当monitor被持有后，代码处于锁定状态。
     *
     *   加锁对象
     *      修饰方法：默认是当前实体对象
     *      修饰代码块：可以随意制定加锁对象
     *
     *  进入方法加入monitor监视，执行完方法退出监视。在监视没退出之前，其它线程处于排队等待锁状态。
     *
     *  synchronize用法
     *      1. 声明方法上
     *          加锁监视对象是当前操作实体对象。
     *          存在局限性，修饰在方法上会导致方法中所有业务被同步监视，可能业务中不存在线程安全的操作，没必要加入监视，损耗性能。
     *          如：
     *              public synchronized void fun() {
     *                  // ....
     *              }
     *      2. 修饰代码块
     *          加锁监视对象是synchronize传入对象
     *          应用灵活，控制具体代码块加锁。
     *          如：
     *              public void fun() {
     *                  //.. 业务1
     *
     *                  synchronize(lock) {
     *                      //.. 业务2
     *                  }
     *
     *                  //.. 业务3
     *              }
     *
     *
     *
     */


    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        SynchronziedLock lock = new SynchronziedLock();
        /**
         * 线程不安全类实现
         *  出现count紊乱情况
         */
        BaseTaskWork taskWork = lock.new UnSafeTaskWork();

        /**
         * 模拟10个线程执行 --
         */
        for (int i = 0; i < 100; i++) {
//            new Thread(lock.new SynchronizedThread(taskWork)).start();
        }

        /**
         * 线程安全类实现
         *    通过Synchronized实现
         */
        BaseExtendsTaskWork extendsTaskWork = lock.new ExtendTaskWork();
        /**
         * 模拟10个线程执行 --
         */
        for (int i = 0; i < 100; i++) {
            new Thread(lock.new SynchronizedExtendThread(extendsTaskWork)).start();
        }


    }

    /**
     * synchronize在继承中应用分析
     */
    class SynchronizedExtendThread implements Runnable {
        BaseExtendsTaskWork taskWork;

        public SynchronizedExtendThread(BaseExtendsTaskWork taskWork) {
            this.taskWork = taskWork;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " - " + Thread.currentThread().getId() + " >> count: " + taskWork.getDecrement());
        }
    }

    class SynchronizedThread implements Runnable {

        BaseTaskWork taskWork;

        public SynchronizedThread(BaseTaskWork taskWork) {
            this.taskWork = taskWork;
        }

        @Override
        public void run() {
//            taskWork.addCount();
            System.out.println(Thread.currentThread().getName() + " - " + Thread.currentThread().getId() + " >> count: " + taskWork.decrement());
        }
    }

    /**
     * 模拟线程不安全类
     */
    class UnSafeTaskWork extends BaseTaskWork {
        @Override
        void addCount() {
            count++;
        }

        @Override
        int decrement() {
            return count++;
        }
    }

    /**
     * 模拟线程安全类
     */
    class SafeTaskWork extends BaseTaskWork{

        /**
         * 使用Synchronized（监视器锁实现）
         */
        @Override
        synchronized  void addCount() {
            count++;
        }

        @Override
        synchronized int decrement() {
            return count++;
        }

        @Override
        synchronized  int getCount() {
            return count;
        }
    }

    abstract class BaseTaskWork {
        protected int count;

        int getCount() {
            return count;
        }

        abstract void addCount();

        abstract int decrement();
    }

    class ExtendTaskWork extends BaseExtendsTaskWork {

        private int count;


        @Override
        int decrement() {
            return count++;
        }

        @Override
        synchronized int getDecrement() {
            return super.getDecrement();
        }
    }

    abstract class BaseExtendsTaskWork extends BaseTaskWork {
        @Override
        void addCount() {
            count++;
        }

        @Override
        abstract int decrement();

        synchronized int getDecrement() {
            return decrement();
        }
    }
}
