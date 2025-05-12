package shuati1009;

public class Solution2 {
    public void duplicateZeros(int[] arr) {
        int cur = 0;
        int dest = -1;
        //1.先找到最后一个要移动的位置
        //cur是最后复写的元素
        //因为dest会先到终点,所以会当cur还在走的时候会出现dest下标越界的情况
        //所以得判断一下是否dest已经走到最后的位置当走到最后位置的时候停止
        while(cur < arr.length) {
            if(arr[cur] == 0) {
                dest += 2;
            } else {
                dest++;
            }
            if(dest >= arr.length-1) {
                break;
            }
            cur++;
        }
        //dest是最后一个位置的元素
        //2.处理一下边界情况
        //此时dest的位置就是最后要复写开始的位置
        //cur的位置的数字就是最后开始复写的数字
        if(dest == arr.length) {
            arr[arr.length - 1] = 0;
            dest -= 2;
            cur--;
        }
        //3.从后向前覆盖
        while (cur >= 0) {
            if(arr[cur] != 0) {
                arr[dest] = arr[cur];
                dest --;
                cur--;
            } else {
                arr[dest] = 0;
                arr[dest - 1] = 0;
                dest -= 2;
                cur--;
            }
        }
    }

}
