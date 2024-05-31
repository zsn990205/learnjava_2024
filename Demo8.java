package Thread;

//线程终止
public class Demo8 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> {
            //Thread类内部有一个现成的标志位,可以预判当前的循环是否要结束
            while(!Thread.currentThread().isInterrupted()) {
                System.out.println("线程工作中");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        t.start();
        Thread.sleep(5000);
        System.out.println("让t线程终止");
        t.interrupt();
    }
}
