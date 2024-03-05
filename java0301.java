import java.util.Arrays;

public class java0301 {
    public static void main7(String[] args) {
        //拷贝数组(手动实现)
        int[] arr = {2,4,6,3};
        int[] copy = new int[arr.length];
        for(int i = 0; i < arr.length; i++) {
            copy[i] = arr[i];
        }
        System.out.println(Arrays.toString(copy));
    }

    public static void main(String[] args) {
        int[] arr = {1,3,5,4,6};
        int[] arr2 = {1,2,5,4,6};
        System.out.println(threeOdd(arr));
        System.out.println(threeOdd(arr2));
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.equals(arr, arr2));
        Arrays.fill(arr,9);
        System.out.println(Arrays.toString(arr));
        System.out.println("====================");
        int[] copy = Arrays.copyOf(arr,arr.length * 2);
        System.out.println(Arrays.toString(copy));
        int[] copy2 = Arrays.copyOfRange(arr,0,2); //左闭右开区间
        System.out.println(Arrays.toString(copy2));
        System.out.println("================");
        int[] copy3 = new int[arr.length];
        System.arraycopy(arr,0,copy3,0,arr.length);
        System.out.println(Arrays.toString(copy3));
    }

    //是否存在三个连续的奇数
    public static boolean threeOdd(int[] arr){
        int count = 0;
        for(int i = 0; i < arr.length; i++) {
            if(arr[i] % 2 != 0) {
                count++;
                if (count == 3) {
                    return true;
                }
            } else {
                //如果是偶数的话,直接置为 0
                //否则无法保证是连续的奇数
                count = 0;
            }
        }
        return false;
    }

    public static void main5(String[] args) {
        int[] arr = {1,3,4,5};
        int[] ret = findNum(arr, 8);
        System.out.println(Arrays.toString(ret));
        int[] ret2 = findNum(arr, 1);
        System.out.println(Arrays.toString(ret2));
        int[] ret3 = findNum(arr, 10);
        System.out.println(Arrays.toString(ret3));
    }

    public static int[] findNum(int[] arr, int target) {
        int[] ret = {-1,-1};
        for(int i = 0; i < arr.length; i++) {
            for(int j = i + 1; j < arr.length; j++) {
                if(target == arr[i] + arr[j]) {
                    ret[0] = i;
                    ret[1] = j;
                }
            }
        }
        //没找到返回的是{-1,-1}
        return ret;
    }

    public static void main4(String[] args) {
        int[] arr = {2,4,6,8,10};
        System.out.print(binarySearch(arr, 2));
        System.out.print(binarySearch(arr, 6));
        System.out.print(binarySearch(arr, 10));
        System.out.println();
        System.out.println(Arrays.binarySearch(arr, 2));
    }

    public static int binarySearch(int[] arr, int key) {
        int i = 0;
        int j = arr.length-1;
        while(i <= j) {
            int mid = (i + j)/2;
            if(arr[mid] < key) {
                i = mid + 1;
            } else if(arr[mid] == key) {
                return mid;
            } else {
                 j = mid - 1;
            }
        }
            return -1;//表示没找到
    }

    public static void main2(String[] args) {
        int[] arr = {1,2,3,4,5};
        int[] arr2 = {2,2,6,4,8};
        int[] arr3 = {1,2,6,3,5};
        oddPreEven(arr3);
        System.out.println(Arrays.toString(arr3));
    }

    //写一个奇数位于偶数之前
    public static void oddPreEven(int[] arr) {
        int i = 0;
        int j = arr.length-1;
        while(i < j) {
            //此时j下标的数组的值对应的数全为偶数
            while(i < j && arr[j] % 2 == 0) {
                j--;
            }
            //此时i下标对应的数组的值全是奇数
             while(i < j && arr[i] % 2 != 0) {
                 i++;
             }
             int tmp = arr[i];
             arr[i] = arr[j];
             arr[j] = tmp;

        }
    }

    //在这个汉诺塔问题中
    //n代表的是盘子的数目
    //pos1代表的是起始位置
    //pos2代表的是中转位置
    //pos3代表的是目的地位置
    public static void move(char pos1, char pos2) {
        System.out.print(pos1 +"-->"+ pos2+" ");
    }

    public static void hanoi(int n, char pos1, char pos2, char pos3) {
        if(n == 1) {
            //如果只有一个盘子的话就把它从1->3上
            move(pos1,pos3);
            return;
        }
        //把pos1位置上的n-1个盘子通过中转pos3挪到pos2上,
        //再把pos1位置上的直接挪到pos3上
        //把pos2位置上的n-1个盘子通过中转pos1挪到pos3上,
        hanoi(n-1,pos1,pos3,pos2);
        move(pos1,pos3);
        hanoi(n-1,pos2,pos1,pos3);
    }

    public static void main1(String[] args) {
        hanoi(1,'A','B','C');
        System.out.println();
        hanoi(2,'A','B','C');
        System.out.println();
        hanoi(3,'A','B','C');
        System.out.println();
        hanoi(4,'A','B','C');
        System.out.println();
    }
}
