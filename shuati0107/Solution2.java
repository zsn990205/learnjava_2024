package shuati0107;

//滑动窗口问题1 长度最小的子数组
//时间复杂度是O(N) 空间复杂度是O(1)
public class Solution2 {
    public int minSubArrayLen(int target, int[] nums) {
        int length = Integer.MAX_VALUE;
        int sum = 0;
        int n = nums.length;
        for(int left = 0, right = 0; right < n; right++) {
            sum += nums[right];
            while(sum >= target) {
                //此时sum>=target了 记录此时len的长度
                length = Math.min(length,right - left + 1);    //第一次是4,但是得找最小的len
                //先更改此时sum的值
                sum = sum - nums[left];
                //再更改left的位置
                left = left + 1;
            }
        }
        return length == Integer.MAX_VALUE ? 0 : length;
    }
}
