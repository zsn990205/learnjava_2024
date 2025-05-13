package shuati1015;

public class Solution1 {
    //返回n这个数每一位的平方和
    public int bitSum(int n) {
        int sum = 0;
        while(n != 0) {
            int t = n % 10;   //找到个位上的数字
            sum += t * t;
            n = n / 10;
        }
        return sum;
    }

    public boolean isHappy(int n) {
        //定义快慢指针 让慢指针一次走1步  快指针一次2步
        int slow = n;
        int fast = bitSum(n);
        //相遇的时候就是环
        while(slow != fast) {
            slow = bitSum(slow);
            fast = bitSum(bitSum(fast));
        }
        if(fast == 1) {
            return true;
        } else {
            return false;
        }
    }
}
