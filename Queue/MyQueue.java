package Queue;

import java.util.Stack;

class MyQueue {

    public Stack<Integer> s1;
    public Stack<Integer> s2;
    public MyQueue() {
        s1 = new Stack<>();
        s2 = new Stack<>();
    }
    
    public void push(int x) {
            s1.push(x);
    }
    
    public int pop() {
        if(empty()) {
            return -1;
        }
        if(s2.empty()) {
            int size = s1.size();
            for(int i = 0; i < size; i++) {
                int x = s1.pop();
                s2.push(x);
            }
        }
        return s2.pop();
    }
    
    public int peek() {
        if(empty()) {
            return -1;
        }
        if(s2.empty()) {
            int size = s1.size();
            for(int i = 0; i < size; i++) {
                int x = s1.pop();
                s2.push(x);
            }
        }
        return s2.peek();
    }
    
    public boolean empty() {
        if(s1.empty() && s2.empty()) {
            return true;
        }
        return false;
    }
}