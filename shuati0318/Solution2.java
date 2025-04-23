package shuati0318;

public class Solution2 {
    public void moveZeroes(int[] nums) {
        if(nums.length == 0) {
            return;
        }
        int dest = -1;
        for(int i = 0; i < nums.length; i++) {
            if(nums[i] != 0) {
                //此时=0的情况
                dest++;
                swap(nums,i,dest);
            }
        }
    }

    private void swap(int[] nums, int dest, int i) {
        int tmp = nums[i];
        nums[i] = nums[dest];
        nums[dest] = tmp;
    }

}
