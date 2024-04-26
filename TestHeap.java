package priorityQueue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
class Imp implements Comparator<Integer> {
    @Override
    public int compare(Integer o1,Integer o2) {
        return o2.compareTo(o1);
    }
}

public class TestHeap {
    private int[] elem;
    private int usedSize;

    public TestHeap() {
        this.elem = new int[10];
    }

    //初始化elem数组
    public void initElem(int[] arr) {
        for(int i = 0; i < arr.length; i++) {
            elem[i] = arr[i];
            usedSize++;
        }
    }

    public void createHeap(int[] arr) {
        for(int parent = (usedSize-1-1) / 2; parent >= 0; parent--) {
            siftDown(parent,usedSize);
        }
    }

    private void siftDown(int parent, int len) {
        int child = 2 * parent + 1;
        while(child < len) {
            //child+1必须在前面,如果说child+1越界的话表示根本无右孩子
            if(child + 1 < len && elem[child] < elem[child + 1]) {
                child = child + 1;
            }
            //走到这儿的时候表示此时child记录的就是左右孩子中最大的值
            if(elem[parent] < elem[child]) {
                int tmp = elem[child];
                elem[child] = elem[parent];
                elem[parent] = tmp;
                parent = child;
                child = 2 * parent + 1;
            } else {
                break;
            }
        }
    }

    public void push(int val) {
        //1.数组满了
        if(usedSize == elem.length) {
            elem = Arrays.copyOf(elem,elem.length*2);
        }
        elem[usedSize] = val;
        //向上调整
        swiftUp(usedSize);
        usedSize++;
    }

    private void swiftUp(int child) {
        int parent = (child - 1) / 2;
        while (child > 0) {
            if (elem[child] > elem[parent]) {
                int tmp = elem[child];
                elem[child] = elem[parent];
                elem[parent] = tmp;
                child = parent;
                parent = (child - 1) / 2;
            } else {
                break;
            }
        }
    }

    public void pop() {
        if(usedSize == 0) {
            return;
        }
        int tmp = elem[usedSize - 1];
        elem[usedSize - 1] = elem[0];
        elem[0] = tmp;
        usedSize--;
        siftDown(0,usedSize);

    }

    public static int[] smallestK(int[] arr,int k) {
        int[] ret = new int[k];
        if(ret == null || k <= 0) {
            return ret;
        }
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(new Imp());
        //1.先建立大根堆(堆默认是小根堆),使用comparator接口
        for(int i = 0; i < k; i++) {
            priorityQueue.offer(arr[i]);
        }
        //2.将后期的数组的数字和top进行比较,如果小就换
        for(int i = k; i < arr.length; i++) {
            if(arr[i] < priorityQueue.peek()) {
                priorityQueue.poll();
                priorityQueue.offer(arr[i]);
            }
        }
        //3.输出
        for(int i = 0; i < k; i++) {
            ret[i] = priorityQueue.poll();
        }
        return ret;
    }

    //建立大根堆,然后0下标和end下标交换
    public void HeapSort() {
        int end = usedSize - 1;
        while(end > 0) {
            int tmp = elem[0];
            elem[0] = elem[end];
            elem[end] = tmp;
            siftDown(0,end);
            end--;
        }

    }

}
