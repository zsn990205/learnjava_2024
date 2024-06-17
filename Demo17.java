package Thread;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//手动创建一个线程池
class MyThreadPool {
    //需要有一个任务队列
    private BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1000);

    //通过这个方法把任务加到队列中
    public void submit(Runnable runnable) throws InterruptedException {
        //此处我们的策略相当于第五种策略-->阻塞等待(下策)
        queue.put(runnable);
    }

    public MyThreadPool(int n) {
        //创建N个线程,负责执行上述队列中的任务
        //此处是创建固定的线程池  相当于fix
        for(int i = 0; i < n; i++) {
            Thread t = new Thread(() -> {
                //让这个线程能在队列中消费任务并执行
                try {
                    //取出任务
                    Runnable runnable = queue.take();
                    //执行任务
                    runnable.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
    }
}

public class Demo17 {
    public static void main(String[] args) throws InterruptedException {
        MyThreadPool myThreadPool = new MyThreadPool(4);
        for(int i = 0; i < 1000; i++) {
            int id = i;
            myThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    //i是匿名内部类中的变量,捕获了外部变量中的i,
                    // 变量只能捕获final或者事实final
                    //此时捕获的就是id了,就不是i了
                    System.out.println("执行任务: " + id);
                }
            });
        }
    }
}
