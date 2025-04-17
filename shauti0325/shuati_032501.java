package shauti0325;

/*
最大子数组和
 */
public class shuati_032501 {
    public int maxSubArray(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        //res和dp[0]分别都表示nums[0]
        int res = nums[0];
        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            //dp[i]这个数组要么就是dp[i-1]+nums[i]为最大值,要么就是nums[i]为最大值
            dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
            //使用res来记录每次遍历的最大值
            res = Math.max(res,dp[i]);
        }
        return res;
    }

    public int maxSubArray2(int[] nums) {
        int res = nums[0];
        int pre = nums[0];
        for(int i = 1; i < nums.length; i++) {
            pre = Math.max(pre + nums[i], nums[i]);
            res = Math.max(res, pre);
        }
        return res;
    }
}
