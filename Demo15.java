package Thread;

import java.util.PriorityQueue;
import java.util.TimerTask;

//通过这个类描述了一个任务
class MyTimerTask implements Comparable<MyTimerTask>{
    //要有一个要执行的任务
    private Runnable runnable;
    //还要有一个执行任务的时间
    private long time;

    //此处的delay指的是schedule方法的相对时间
    public MyTimerTask(Runnable runnable,long delay) {
        this.runnable = runnable;
        this.time = System.currentTimeMillis() + delay;
    }

    @Override
    public int compareTo(MyTimerTask o) {
        //这样的写法是让队首元素是最小时间的值
        return (int)(this.time - o.time);
    }

    public long getTime() {
        return time;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}

//咱们自己搞得定时器
class MyTimer {
    //使用一个数据结构来保存所有要安排的任务
    private PriorityQueue<MyTimerTask> queue = new PriorityQueue<>();
    //使用locker这个对象进行加锁
    private Object locker = new Object();
    public void schedule(Runnable runnable,long delay) {
        synchronized(locker) {
            queue.offer(new MyTimerTask(runnable,delay));
            locker.notify();
        }
    }
    //搞一个扫描线程
    public MyTimer() {
        //创建一个扫描线程
        Thread t = new Thread(() -> {
            while(true) {
                synchronized(locker) {
                    //加锁保证线程是安全的
                    //使用while是保证wait被唤醒的时候,再次确认一下条件
                    while(queue.isEmpty()) {
                        //加入队列是空的话,就阻塞等待直到队列不为空的时候
                        //使用wait进行等待
                        //这里的wait需要由另外的线程唤醒
                        //添加了新的任务就应该唤醒
                        try {
                            locker.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                MyTimerTask task = queue.peek();
                //去比较一下当前的队首元素是否可以执行
                long curTime = System.currentTimeMillis();
                if(curTime >= task.getTime()) {
                     //如果当前时间已经到了任务时间,就可以执行任务了
                    task.getRunnable().run();
                    //执行完再将这个任务从队列中删除
                    queue.poll();
                } else {
                    //如果当前时间还没到人物时间,暂时不执行任务
                    //暂时先啥也不用干,等下一轮的循环判定
                    try {
                        locker.wait(task.getTime() - curTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t.start();
    }
}

public class Demo15 {
    public static void main(String[] args) {
        MyTimer timer = new MyTimer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("3000");
            }
        },3000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("2000");
            }
        },2000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("1000");
            }
        },1000);

        System.out.println("程序开始执行!");
    }
}
