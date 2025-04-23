package shuati0318;

public class Solution3 {
    public int maxArea(int[] height) {
        int right = height.length - 1;
        int left = 0;
        int ret = 0;
        while(left < right) {
            int a = Math.min(height[left],height[right]);
            int b = right - left;
            int v = a * b;
            ret = Math.max(ret,v);
            if(height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        return ret;
    }
}
