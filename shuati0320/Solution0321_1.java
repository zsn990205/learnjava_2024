package shuati0320;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
三数之和
 */
public class Solution0321_1 {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ret = new ArrayList<>();
        //先对数组进行排序
        Arrays.sort(nums);

        for(int i = 0; i < nums.length; i++) {
            if(nums[i] > 0) {
                //假如数组的第一个就>0 那么此时绝对没有sum=0的数组
                return ret;
            }
            //进行去重操作
            //假如此时的i和i-1是一样的 进行去重操作
            if(i > 0 && nums[i-1] == nums[i]) {
                //当i位于初始位置的时候,此时i=0,那么i-1<0此时下标就越界了
                //也就是说第一个元素一定不会和前面的元素重复
                continue;
            }
            int left = i + 1;
            int right = nums.length - 1;
            while(left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                if(sum > 0) {
                    //表示此时的三元组的和大了,需要缩短下标
                    right--;
                } else if(sum < 0) {
                    left++;
                } else {
                    ret.add(Arrays.asList(nums[i],nums[left],nums[right]));
                    //因为下标走的位置是i left++ right-- 这样
                    while(left < right && nums[left] == nums[left + 1]) {
                        //当left和left+1相等的时候,此时会产生重复元素
                        left++;
                    }
                    while(left < right && nums[right] == nums[right - 1]) {
                        //当right和right-1相等的时候,此时也会产生重复元素
                        right--;
                    }

                    left++;
                    right--;
                }
            }
        }
        return ret;
    }
}
