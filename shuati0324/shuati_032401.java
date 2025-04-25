package shuati0324;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
leetcode438题
找到字符串中所有的字母异位词
 */
public class shuati_032401 {
    public List<Integer> findAnagrams(String s, String p) {
        //长度相同,字母的随机组合,字母相同
        List<Integer> ret = new ArrayList<>();
        int sLen = s.length();
        int pLen = p.length();
        if(sLen < pLen) {
            //当我们要寻找匹配的字符串比已知字符串短的时候直接ret
            return ret;
        }
        //将已知字符串转成数组
        char[] pArr = p.toCharArray();
        //进行排序
        Arrays.sort(pArr);
        for(int i = 0; i < sLen - pLen + 1; i++) {
            //i的循环下标如下: 比如此时s的长度为10,但p的总长度为3,此时就找到s的倒数三位字母即可
            //也就是(sLen-x)=pLen求解x的值,而i循环的值 <y y的位置是取不到的所以+1
            //也就是x=sLen-pLen+1
            //对寻找匹配的字符串进行拼接,总长度为3

            //substring是左闭右开区间
            String str = s.substring(i,i + pLen);
            //将其转成数组
            char[] sArray = str.toCharArray();
            //对其进行排序
            Arrays.sort(sArray);
            if(Arrays.equals(pArr,sArray)) {
                ret.add(i);
            }
        }
        return ret;
    }
}
