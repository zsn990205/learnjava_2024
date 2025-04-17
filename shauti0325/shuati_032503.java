package shauti0325;

import java.util.Arrays;
/*
轮转数组
 */
public class shuati_032503 {
    public void rotate(int[] nums, int k) {
            int[] ret = new int[nums.length];
            for(int i = 0; i < nums.length; i++) {
                //找规律得到
                //0->3  1->4  2->5  3->6  4->0  5->1  6->2
                ret[(i + k) % nums.length] = nums[i];
            }
            //进行数组的拷贝,将得到的新的ret数组从0号下标位置起,拷贝到nums数组,下标也从0开始,拷贝的长度为数组长
            System.arraycopy(ret, 0, nums, 0, nums.length);
        }
}
