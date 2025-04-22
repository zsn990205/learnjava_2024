package shuati0314;

import java.util.Arrays;

public class Solution1 {
    public static int[] twoSum(int[] nums, int target) {
        int sum = 0;
        for(int i = 0; i < nums.length; i++) {
            for(int j = i + 1; j < nums.length; j++) {
                if(nums[i] + nums[j] == target) {
                    return new int[]{i,j};
                }
            }
        }
        return new int[0];
    }

    public static void main(String[] args) {
        int[] arr = {2,7,11,5};
        int target = 9;
        int[] sum = twoSum(arr,target);
        System.out.println(Arrays.toString(sum));
    }
}
