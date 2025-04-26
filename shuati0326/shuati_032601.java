package shuati0326;

/*
除自身以外数组的乘积
 */
public class shuati_032601 {
    public int[] productExceptSelf(int[] nums) {
        int[] ret = new int[nums.length];
        ret[nums.length - 1] = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            ret[i] = nums[i + 1] * ret[i + 1];
        }

        int left = 1;
        for (int i = 1; i < nums.length; i++) {
            left = left * nums[i - 1];
            ret[i] *= left;
        }
        return ret;
    }
}
