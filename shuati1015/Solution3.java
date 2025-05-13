package shuati1015;

import java.util.Arrays;

public class Solution3 {
    //判断是否是三角形的最优解如下
    //我们先对数组的三个数据进行排序
    //找到最大的哪个数字记为c 其他两个分别记为a b 最大的数字一定是大于其他两个的
    //此时我们就不需要考虑其他的情况了
    //我们只需考虑a + b > c即可
    //因为a + c > b  b + c > a
    //c是最大的  c加任何数都比其他两个数字大
    public int triangleNumber(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int ret = 0;
        for(int i = n - 1; i >= 2; i--) {
            int left = 0;
            int right = i - 1;
            while(left < right) {
                if(nums[left] + nums[right] > nums[i]) {
                    ret += right - left;
                    right--;
                } else {
                    left++;
                }
            }
        }
        return ret;
    }
}
