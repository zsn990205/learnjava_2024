package shuati1009;

public class Solution1 {
    public void moveZeroes(int[] nums) {
        int dest = -1;
        for(int cur = 0; cur < nums.length; cur++) {
            if(nums[cur] != 0) {
                dest++;
                swap(nums,dest,cur);
            }
        }
        }

    private void swap(int[] nums, int dest, int cur) {
        int tmp = nums[cur];
        nums[cur] = nums[dest];
        nums[dest] = tmp;
    }
}

