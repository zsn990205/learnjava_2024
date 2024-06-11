package Thread;

import java.util.concurrent.ArrayBlockingQueue;

class MyBlockingQueue {
    //最大长度也可指定构造方法
    private String[] data = new String[1000];
    //队首
    private volatile int head = 0;
    //队的结束位置的下一个位置
    private volatile int tail = 0;
    //队列中的有效元素个数
    private volatile int size;

    public void put(String elem) throws InterruptedException {
        synchronized(this) {
            //使用wait的时候往往使用while进行判定,避免重复添加wait方法
            while(size == data.length) {
                //此时队满了
                //继续插入元素就会阻塞
                this.wait();
            }
            data[tail] = elem;
            size++;
            //当放元素进入的时候,将其唤醒,唤醒的是take中size为空时候的wait
            this.notify();
            tail++;
            if(tail == data.length) {
                //当最后一个插入的元素在队尾的时候,让tail=0,成环状
                tail = 0;
            }
        }
    }

    //出队列
    public String take() throws InterruptedException {
        synchronized(this) {
            while(size == 0) {
                //此时队列为空,触发阻塞
                this.wait();
            }
            //队列不为空的时候就可以把head位置的元素删除并进行返回
            String ret = data[head];
            head++;
            //无论是谁到达队尾都给他置为0
            if(head == data.length) {
                head = 0;
            }
            size--;
            //当有新元素释放的时候将其唤醒,唤醒的是put中的wait
            this.notify();
            return ret;
        }
        }
}

public class Demo13 {
    public static void main(String[] args) {
        MyBlockingQueue queue = new MyBlockingQueue();
        //消费者模型
        Thread t1 = new Thread(() -> {
            try {
                String res = queue.take();
                //Thread.sleep(500);
                System.out.println("消费元素: " + res);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //生产者模型
        Thread t2 = new Thread(() ->{
            int num = 1;
            while(true) {
                try {
                    queue.put(num+"");
                    System.out.println("生产元素: " + num);
                    num++;
                    //Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
    }
}
