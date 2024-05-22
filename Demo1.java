package Thread;
//创建一个类
class MyThread extends Thread {
    @Override
    public void run() {
        //线程的入口方法
        while(true) {
            System.out.println("hello thread");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
public class Demo1 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new MyThread();
        t.start();
        while(true) {
            System.out.println("hello main");
            Thread.sleep(1000);
        }
    }

}


