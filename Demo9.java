package Thread;

public class Demo9 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            for(int i = 0; i < 5; i++) {
                System.out.println("t 线程工作中");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t.start();

        //让主线程等待t线程执行结束
        //一直调用join主线程就会阻塞此时t线程就能完成后续的操作
        //一直阻塞到t执行完毕了join才会解除阻塞才会执行
        System.out.println("join等待开始");
        t.join();
        System.out.println("join等待结束");
    }
}
