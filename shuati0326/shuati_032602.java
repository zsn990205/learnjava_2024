package shuati0326;
/*
接雨水
 */
public class shuati_032602 {
    //双指针方法
    public int trap(int[] height) {
        int length = height.length;
        if (length <= 2) {
            //如果此时数组中只有两个元素的时候是不接雨水的
            return 0;
        }
        int[] maxLeft = new int[length];
        int[] maxRight = new int[length];

        // 记录每个柱子左边柱子最大高度
        maxLeft[0] = height[0];
        for (int i = 1; i < length; i++) {
            //左边就是向左看齐,寻找当前位置和左面的最大高度
            maxLeft[i] = Math.max(height[i], maxLeft[i-1]);
        }

        // 记录每个柱子右边柱子最大高度
        maxRight[length - 1] = height[length - 1];
        for(int j = length - 2; j >= 0; j--) {
            //右边就是向右看齐,寻找当前位置和右面的最大高度
            maxRight[j] = Math.max(height[j], maxRight[j+1]);
        }

        // 求和
        int sum = 0;
        for (int i = 0; i < length; i++) {
            int count = Math.min(maxLeft[i], maxRight[i]) - height[i];
            if (count > 0) {
                sum += count;
            }
        }
        return sum;
    }

    //暴力求解(超时)
    public int trap2(int[] height) {
        int sum = 0;
        for (int i = 0; i < height.length; i++) {
            // 第一个柱子和最后一个柱子不接雨水
            if (i == 0 || i == height.length - 1) {
                //也就是说第一个和最后一个柱子不接水
                continue;
            }
            int rHeight = height[i]; // 记录右边柱子的最高高度
            int lHeight = height[i]; // 记录左边柱子的最高高度
            for (int r = i + 1; r < height.length; r++) {
                if (height[r] > rHeight) {
                    rHeight = height[r];
                }
            }
            for (int l = i - 1; l >= 0; l--) {
                if(height[l] > lHeight) {
                    lHeight = height[l];
                }
            }
            int h = Math.min(lHeight, rHeight) - height[i];
            if (h > 0) {
                sum += h;
            }
        }
        return sum;
    }


}
