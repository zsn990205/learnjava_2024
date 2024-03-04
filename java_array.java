import java.util.Arrays;

public class java_array {
    //数组的逆置
    public static void reverse(int[] arr) {
        if (arr == null) {
            return;
        }
        int i = 0;
        int j = arr.length-1;
        while(i < j) {
            int tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
            i++;
            j--;
        }
    }

    public static void main(String[] args) {
        int[] arr = {12,4,3,8,19,7};
        reverse(arr);
        System.out.println(myToString(arr));
    }

    //下面这个代码就很好的展现出循环的步骤次序!(错误学习示范)
    public static String myToString2(int[] arr) {
        if(arr.length == 0) {
            return "[]";
        }
        if(arr == null) {
            return "null";
        }
        String ret = "[";
        for(int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + ",");
        }
        ret += "]";
        return ret;
    }

    //实现一个我自己的打印数组方法
    public static String myToString(int[] arr) {
        if(arr.length == 0) {
            return "[]";
        }
        if(arr == null) {
            return "null";
        }
        String ret = "[";
        for(int i = 0; i < arr.length; i++) {
            ret += arr[i];
            if(i != arr.length - 1) {
                ret += ",";
            }
        }
        ret += "]";
        return ret;
    }

    public static void main4(String[] args) {
        int[] arr = {12,23,34,45,56,67};
        System.out.println(myToString2(arr));
    }

    //打印一个数组的每一个下标(快速)
    public static void main3(String[] args) {
        int[] arr = {1,2,3,4};
        System.out.println(Arrays.toString(arr));
        int[] arr2 = {3,1,5,4};
        System.out.println(Arrays.toString(arr2));
        Arrays.sort(arr2,0,4);
        System.out.println(Arrays.toString(arr2));
    }

    public static void main1(String[] args) {
        int[] arr = {1,2,3,4,5};
        func1(arr);
        for(int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] +" ");
        }
        System.out.println();
        func2(arr);
        for(int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] +" ");
        }
    }

    public static void func1(int[] arr) {
        arr[0] = 99;

    }

    public static void func2(int[] arr) {
        arr = new int[] {11,22,33,44,55};

    }
}
