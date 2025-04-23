package shuati0318;

import java.util.ArrayList;
import java.util.List;

public class Solution4 {
    public static int lengthOfLongestSubstring1(String s) {
        int max = 0;
        List<Character> list = new ArrayList<>();
        for(int i = 0; i < s.length(); i++) {
            while(list.contains(s.charAt(i))) {
                //因为可能会出现删除了但是仍存在的情况,所以就需要循环删除直至此时的链表中无此元素的存在
                //当我们记录的链表中存在最大值的时候就是此时没有连续出现的最大值的情况
                list.remove(0);
            }
            list.add(s.charAt(i));
            max = Math.max(max,list.size());
        }
        return max;
    }

    public static void main(String[] args) {
        String s = "abcabcbb";
        List<Character> list = new ArrayList<>();
        for(int i = 0; i < s.length(); i++) {
            list.add(s.charAt(i));
        }
        list.remove(0);
        for(char ch : list) {
            System.out.println(ch);
        }
    }
}
