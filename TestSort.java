package Sort;

import java.util.Arrays;

public class TestSort {
    public static void insertSort(int[] arr) {
        for(int i = 1; i < arr.length; i++) {
            int tmp = arr[i];
            int j = i - 1;
            for(; j >= 0; j--) {
                if(arr[j] > tmp) {
                    arr[j+1] = arr[j];
                } else {
                    break;
                }
            }
            arr[j+1] = tmp;
        }
    }

    private static void swap(int[] arr,int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static void shellSort(int[] arr) {
        int gap = arr.length;
        while(gap > 1) {
            gap = gap / 2;
            insertSortGap(arr,gap);
        }
    }

    private static void insertSortGap(int[] arr, int gap) {
        for(int i = gap; i < arr.length; i++) {
            int tmp = arr[i];
            int j = i - gap;
            for(; j >= 0; j -= gap) {
                if(arr[j] > tmp) {
                    arr[j + gap] = arr[j];
                } else {
                    break;
                }
            }
            arr[j+gap] = tmp;
        }
    }

    public static void selectSort(int[] arr) {
        for(int i = 0; i < arr.length; i++) {
            for(int j = i ; j < arr.length; j++) {
                if(arr[i] > arr[j]) {
                    swap(arr,i,j);
                }
            }
        }
    }

    public static void HeapSort(int[] arr) {
        createHeap(arr);
        int end = arr.length - 1;
        while(end > 0) {
            int tmp = arr[0];
            arr[0] = arr[end];
            arr[end] = tmp;
            shiftDown(arr,0,end);
            end--;
        }
    }

    private static void createHeap(int[] arr) {
        for(int parent = (arr.length-1-1) / 2; parent >= 0; parent--) {
            shiftDown(arr,0,arr.length);
        }
    }

    private static void shiftDown(int[] arr,int parent, int len) {
        int child = 2 * parent + 1;
        while(child < len) {
            if(child + 1 < len && arr[child] < arr[child+1]) {
                child = child + 1;
            }
            if(arr[parent] < arr[child]) {
                int tmp = arr[child];
                arr[child] = arr[parent];
                arr[parent] = tmp;
                parent = child;
                child = 2*parent+1;
            }
            else {
                break;
            }
        }
    }

    public static void bubbleSort(int[] arr) {
        //设置flg的目的是为了让已排序的数组直接跳过节省时间
        boolean flg = true;
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr.length-1-i; j++) {
                if(arr[j] > arr[j+1]) {
                    swap(arr,j,j+1);
                    flg = false;
                }
            }
            if(flg == true) {
                break;
            }
        }
    }

    public static void quickSort(int[] arr) {
        quickSortHelp(arr,0,arr.length-1);
    }

    private static void quickSortHelp(int[] arr, int start, int end) {
        //如果左边比右边大的话直接返回
        if(start >= end) {
            return;
        }
        int index = partition(arr,start,end);
        quickSortHelp(arr,start,index-1);
        quickSortHelp(arr,index+1,end);
    }

    private static int partition(int[] arr, int left, int right) {
        int tmp = arr[left];
        int again = left;    //记录初始值为left固定的
        while(left < right) {
            //左右顺序不能改变,先右后左
            //先走的左边可能会出现相遇的是大的数字,把大的数字放在前面
            //右边的都得比标记位大
            while(left < right && arr[right] >= tmp) {
                right--;
            }
            //左边的都得比标记位小
            while(left < right && arr[left] <= tmp) {
                left++;
            }
            swap(arr,left,right);
        }
        swap(arr,again,left);
        return left;
    }

    //快排的第二种思想
    //找到中位数的下标
    public static int middleNum(int[] arr, int left, int right) {
        int mid = (left + right) / 2;
        if (arr[left] < arr[right]) {
            if (arr[mid] < arr[left]) {
                return left;
            } else if (arr[mid] > arr[right]) {
                return right;
            } else {
                return mid;
            }
        } else {
            if (arr[mid] > arr[left]) {
                return left;
            } else if (arr[mid] > arr[right]) {
                return mid;
            } else {
                return right;
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {15,2,34,2,6};
        quickSort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
