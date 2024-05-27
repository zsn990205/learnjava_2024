package Thread;

public class Demo4 {
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new Runnable(){
            @Override
            public void run() {
                while (true) {
                    System.out.println("hello thread");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread t = new Thread(runnable);
        t.start();
        while(true) {
            System.out.println("hello main");
            Thread.sleep(1000);
        }
    }
}
