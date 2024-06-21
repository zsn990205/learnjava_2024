package Thread;

import java.util.concurrent.CountDownLatch;

public class Demo21 {
    public static void main(String[] args) throws InterruptedException {
        //表示当前有10个选手参赛,await会在10次调用完countDown之后才能继续执行
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for(int i = 0; i < 9; i++) {
            int count = i;
            Thread t = new Thread(() -> {
                System.out.println("Thread: " + count);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //通知当前的任务完成了
                countDownLatch.countDown();
            });
            t.start();
        }
        countDownLatch.await();
        System.out.println("所有的任务都完成了");
    }
}
