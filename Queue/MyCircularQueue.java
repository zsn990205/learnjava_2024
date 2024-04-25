package Queue;

class MyCircularQueue {

    public int[] elem;

    public int front;  //队头
    public int rear;   //队尾

    public MyCircularQueue(int k) {
        elem = new int[k+1];
    }

    //入队
    public boolean enQueue(int value) {
        if(isFull()) {
            return false;
        }
        elem[rear] = value;
        rear = (rear + 1) % elem.length;
        return true;
    }

    //出队
    public boolean deQueue() {
        if(isEmpty()) {
            return false;
        }
        front = (front + 1) % elem.length;
        return true;
    }

    //得到队头元素
    public int Front() {
        if(isEmpty()) {
            return -1;
        }
        return elem[front];
    }

    //得到队尾元素
    public int Rear() {
        if(isEmpty()) {
            return -1;
        }
        int index = rear;
        if(rear == 0) {
            index = elem.length - 1;
        } else {
            index = rear - 1;
        }
        return elem[index];
    }
    
    public boolean isEmpty() {
        if(rear == front) {
            return true;
        }
        return false;
    }
    
    public boolean isFull() {
        if((rear + 1) % elem.length == front) {
            return true;
        }
        return false;
    }
}