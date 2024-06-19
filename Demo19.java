package Thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Demo19 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                int sum = 0;
                for(int i = 0; i <= 1000; i++) {
                    sum += i;
                }
                return sum;
            }
        };
        //把任务放到线程中执行
        FutureTask<Integer> futureTask = new FutureTask<>(callable);
        Thread t = new Thread(futureTask);
        t.start();
        //此处的get就能得到返回的结果
        //由于线程是并发执行的,所以执行到get的时候t可能还没执行完
        //没关系 阻塞等待会
        System.out.println(futureTask.get());
    }
}
