1. thrad-base
    线程基础实践
    
    synchronize加锁模式
    synchronized JVM(java虚拟机)内部实现的同步机制，类似于监视器
    基于进入和退出Monitor对象实现，通过monitorEnter和monitorExit指令
    
        同步代码块：monitorenter指令插入到同步代码块开始位置，monitorexit指令插入到同步代码块结束位置，JVM需要维护每个monitorenter对应一个唯一的monitorexit指令。
       任何对象都有monitor相关联，当monitor被持有后，代码处于锁定状态。
       加锁对象
            修饰方法：默认是当前实体对象
            修饰代码块：可以随意制定加锁对象
            进入方法加入monitor监视，执行完方法退出监视。在监视没退出之前，其它线程处于排队等待锁状态。
            synchronize用法
                1. 声明方法上
                    加锁监视对象是当前操作实体对象。
                    存在局限性，修饰在方法上会导致方法中所有业务被同步监视，可能业务中不存在线程安全的操作，没必要加入监视，损耗性能。
                   如：
                        public synchronized void fun() {
                            // ....
                        }
                2. 修饰代码块
                    加锁监视对象是synchronize传入对象
                    应用灵活，控制具体代码块加锁。
                    如：
                        public void fun() {
                            //.. 业务1
                              synchronize(lock) {
                                //.. 业务2
                            }
                              //.. 业务3
                         }
                         
       在修饰方法上
               验证子类复写父类synchronized方法时，同步加锁用的是否一致
               继承模式synchronized加锁父子类使用同一个锁
               
       关于synchronized死锁产生及原因分析
       分析死锁产生原因，通过两个示例分析
            * 案例一：
            *      DeadLockA和DeadLockB模拟实现两个线程抢占锁
            *      synchronized实现代码块锁控制
            *      出现死锁的原因：两个线程各自等待对方释放自己所需要的锁
            *
            *
            *
            * 案例二：
            *      传递入参顺序不同导致死锁
            *      Transfer中transfer方法中需要两个入参，且入参作为加锁对象
            *      当两个入参传递顺序不一致时，会导致死锁情况发生。
            *
            *
            *  两种死锁调整：
            *      调整加锁顺序，保证所有加锁对象顺序一致
            *      调整加锁力度，尽量控制在最小范围内。