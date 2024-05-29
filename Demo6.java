package Thread;

public class Demo6 {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            System.out.println("线程开始");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程结束");
        });

        System.out.println(t.isAlive());
        t.start();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        System.out.println(t.isAlive());
    }
}
