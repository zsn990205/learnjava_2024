package shuati0314;

import java.util.Arrays;

public class Solution2 {
    public int longestConsecutive2(int[] nums) {
        if(nums.length == 0) {
            return 0;
        }
        Arrays.sort(nums);
        int ans = 0;
        int tmp = 1;
        for(int i = 1; i < nums.length; i++) {
            if(nums[i] == nums[i-1]) {
                continue;   //跳过相同的元素
            } else if(nums[i] == nums[i-1] + 1){
                //连续的元素
                tmp++;
            } else {
                //连续中断
                ans = Math.max(ans, tmp);
                tmp = 1;
            }
        }
        return Math.max(ans, tmp);
    }

    public static int longestConsecutive(int[] nums) {
        if(nums.length == 0) {
            return 0;
        }
        Arrays.sort(nums);
        int tmp = 1;
        int ans = 0;
        for(int i = 1; i < nums.length; i++) {
            if(nums[i] == nums[i-1]) {
                continue;   //跳过相同的元素
            } else if(nums[i] == nums[i-1] + 1){
                //连续的元素
                tmp++;
            } else {
                //此时不满足上述的两个条件
                ans = Math.max(ans,tmp);
                tmp = 1;
            }
        }
        return Math.max(ans,tmp);
    }

    public static void main(String[] args) {
        int[] nums = {100,4,200,1,3,2};
        System.out.println(longestConsecutive(nums));
    }
}
