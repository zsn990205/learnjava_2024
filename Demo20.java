package Thread;

import java.util.concurrent.Semaphore;

public class Demo20 {
    public static void main(String[] args) throws InterruptedException {
        //放入了4个信号量
        Semaphore semaphore = new Semaphore(4);
        //相当于p操作
        //p操作5次但是只有4个信号量,相当于有一个要阻塞等待
        semaphore.acquire();
        System.out.println("p操作");
        semaphore.acquire();
        System.out.println("p操作");
        semaphore.acquire();
        System.out.println("p操作");
        semaphore.acquire();
        System.out.println("p操作");
        semaphore.acquire();
        System.out.println("p操作");
        //相当于v操作
        semaphore.release();
    }
}
