package DataStructer01;

import java.util.Arrays;

public class MyArrayList implements IList{
    public int[] elem;
    public int usedSize;  //当数组中放一个数字++一次

    public static final int DEFAULT = 10;   //表示此时数组的大小是常数
    public MyArrayList() {
        this.elem = new int[DEFAULT];
    }

    @Override
    public void add(int data) {
        checkCapacity();
        this.elem[usedSize] = data;
        this.usedSize++;
    }

    private void checkCapacity() {
        if(isFull()) {
            //如果满了对数组进行扩容
            this.elem = Arrays.copyOf(elem,elem.length * 2);
        }
    }

    @Override
    public void add(int pos, int data) {
       try {
           checkPos(pos);
       }
           catch(posIllegaly e) {
                e.printStackTrace();
                return;
           }
        checkCapacity();
        //把最后一个数字放在usedsize后面,一个个往后移
        for(int i = this.usedSize - 1; i >= pos; i--) {
            this.elem[i+1] = this.elem[i];
        }
        this.elem[pos] = data;
        this.usedSize++;
    }

    private void checkPos (int pos) throws posIllegaly{
        if(pos < 0 || pos > this.usedSize) {
            System.out.println("pos位置不合法");
            throw new posIllegaly("插入元素下标异常" + pos);
        }
    }

    @Override
    public boolean contains(int data) {
        if (isEmpty()) {
            return false;
        }
        for(int i = 0; i < elem.length; i++) {
            if(data == this.elem[i]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int indexOf(int toFind) {
        if(isEmpty()) {
            return -1;
        }
        for(int i = 0; i < this.usedSize; i++) {
            if(this.elem[i] == toFind) {
                return i;
            }
        }
        //没找到
        return -1;
    }

    @Override
    public int get(int pos) throws posIllegaly{
        if(pos < 0 || pos >= this.usedSize) {
            //和上面不同的是当下标=数组长度的时候也是不合法的
            return -1;
        }
        if(isEmpty()) {
         //数组为空
            throw new myException("数组为空异常");
        }
        return this.elem[pos];
    }

    @Override
    public void set(int pos, int val) {
        if(pos < 0 || pos >= this.usedSize) {
            //和上面不同的是当下标=数组长度的时候也是不合法的
            throw new myException("位置不合法");
        }
        this.elem[pos] = val;
    }

    @Override
    //删除第一次出现的关键字(值)
    public void remove(int key) {
        if(isEmpty()) {
            System.out.println("数组为空无处可删");
        }
        int pos = 0;
        for(int i = 0; i < this.usedSize; i++) {
            if(this.elem[i] == key) {
                   pos = i;
            }
        }
        if(pos == -1) {
            System.out.println("没有这个数字");
            return;
        }
        //上述操作找到了对应的下标
        for(int i = pos; i < this.usedSize - 1; i++) {
            this.elem[i] = this.elem[i+1];
        }
        this.usedSize--;
    }

    @Override
    public int size() {
        return this.usedSize;
    }

    @Override
    public void display() {
        for(int i = 0; i < usedSize; i++) {
            System.out.print(elem[i] + " ");
        }
        System.out.println();
    }

    @Override
    public boolean isFull() {
        //当顺序表中放的元素和数组长度相同时则满
        if(usedSize == elem.length) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmpty() {
        return this.usedSize == 0;
    }

    @Override
    public void clear() {
        //如果是基本数据类型的话
        this.usedSize = 0;
        //如果是引用数据类型的话
//        for(int i = 0; i < this.usedSize; i++) {
//            this.elem[i] = null;
//            this.usedSize--;
//        }

     }
}
