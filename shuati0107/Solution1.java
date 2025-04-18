package shuati0107;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution1 {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> ret = new ArrayList<>();
        //1.对数组进行排序
        Arrays.sort(nums);
        int n = nums.length;
        for(int i = 0; i < n; ) {  //固定数a
            for(int j = i + 1; j < n; ) {  //固定数b
                int left = j + 1;
                int right = n - 1;
                long aims = (long) target - nums[i] - nums[j];
                while (left < right) {
                    int sum = nums[left] + nums[right];
                    if (sum < aims) {
                        //此时c部分的数字比绝对值数字小
                        left++;
                    } else if (sum > aims) {
                        right--;
                    } else {
                        //此时c部分的数字等于绝对值部分的数字
                        ret.add(Arrays.asList(nums[i], nums[j], nums[left++], nums[right--]));
                        //去重1
                        while (left < right && nums[left] == nums[left - 1]) {
                            //此处的left是++后的left
                            left++;
                        }
                        while (left < right && nums[right] == nums[right + 1]) {
                            //此处的right是--后的right
                            right--;
                        }
                    }
                }
                //去重2
                j++;
                while (j < n && nums[j] == nums[j-1]) {
                    j++;
                }
            }
            //去重3
                i++;
                while(i < n && nums[i] == nums[i-1]) {
                    i++;
                }
            }

        return ret;
    }
}
