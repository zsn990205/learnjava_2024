package shuati0324;

import java.util.ArrayDeque;

/*
滑动窗口最大值
 */
public class shauti_032403 {
    public int[] maxSlidingWindow(int[] nums, int k) {
        //ArrayDeque这个数据结构类似于循环数组中的双端队列
        ArrayDeque<Integer> deque = new ArrayDeque<>();
        int n = nums.length;
        int[] res = new int[n - k + 1];
        int index = 0;
        for(int i = 0; i < n; i++) {
            // 根据题意，i为nums下标，是要在[i - k + 1, i] 中选到最大值，只需要保证两点
            // 1.队列头结点需要在[i - k + 1, i]范围内，不符合则要弹出
            //下标是左闭右开区间,总长度为k
            while(!deque.isEmpty() && deque.peek() < i - k + 1) {
                //peek返回队首元素但是不删除
                //保证当前的区间是
                deque.poll();
            }
            // 2.维护单调递减队列：新元素若大于队尾元素，则弹出队尾元素，直到满足单调性
            while(!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                //从尾部删除
                //保证尾部的数必须是最大的,如果比后面的小,就要删除掉
                deque.pollLast();
            }

            //offer进去的就是i本i
            deque.offer(i);

            // 因为单调，当i增长到符合第一个k范围的时候，每滑动一步都将队列头节点放入结果就行了
            if(i >= k - 1) {
                //因为是寻找k范围内的最大值,所以i的位置必须得>=k-1,不然还没到k范围是不包括的
                res[index] = nums[deque.peek()];
                index++;
            }
        }
        return res;
    }
}
