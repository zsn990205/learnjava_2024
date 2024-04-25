package Queue;

public class TestQueue {
    public static void main(String[] args) {
        MyQueue1 myQueue = new MyQueue1();
        myQueue.offer(1);
        myQueue.offer(2);
        myQueue.offer(3);
        myQueue.offer(4);

        System.out.println(myQueue.peek());  //1
        System.out.println(myQueue.poll());  //1
        System.out.println(myQueue.poll());  //2


    }
}
