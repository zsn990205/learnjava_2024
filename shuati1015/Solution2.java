package shuati1015;

public class Solution2 {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int ret = 0;
        while(left < right) {
            int a = Math.min(height[left],height[right]);  //最小的数字
            int b = right - left;     //两数的间距
            int v = a * b;
            ret = Math.max(v,ret);    //ret要取到最大值
            if(height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return ret;
    }
}
