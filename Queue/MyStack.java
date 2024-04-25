package Queue;

import java.util.LinkedList;
import java.util.Queue;

class MyStack {

    private Queue<Integer> qu1;
    private Queue<Integer> qu2;

    public MyStack() {
        qu1 = new LinkedList<>();
        qu2 = new LinkedList<>();
    }
    
    public void push(int x) {
        if(!qu1.isEmpty()) {
            qu1.offer(x);
        } else if(!qu2.isEmpty()) {
            qu2.offer(x);
        }
        //如果两个都为空指定放入第一个中
        qu1.offer(x);
    }

    //出队
    public int pop() {
        //如果两个队列都为空
        if(empty()) {
            return -1;
        }
        //哪个不为空,出size-1个数据
        if(!qu1.isEmpty()) {
            //随着下面qu1的出队,size在一直变化,所以需要先定义
            int size = qu1.size();
            for(int i = 0; i < size-1; i++) {
               int data1 = qu1.poll();
               qu2.offer(data1);
            }
            return qu1.poll();
        } else {
            int size = qu2.size();
            for(int i = 0; i < size-1; i++) {
                int data = qu2.poll();
                qu1.offer(data);
            }
            return qu2.poll();
        }
    }
    
    public int top() {
        if(empty()) {
            return -1;
        }
        if(!qu1.isEmpty()) {
            int x = -1;
            int size = qu1.size();
            for(int i = 0; i < size; i++) {
                x = qu1.poll();
                qu2.offer(x);
            }
            return x;
        } else {
            int x = -1;
            int size = qu2.size();
            for(int i = 0; i < size; i++) {
                x = qu2.poll();
                qu1.offer(x);
            }
            return x;
        }
    }
    
    public boolean empty() {
        if(qu1.isEmpty() && qu2.isEmpty()) {
            return true;
        }
        return false;
    }
}
