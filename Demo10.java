package Thread;

public class Demo10 {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.sleep(1000);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
