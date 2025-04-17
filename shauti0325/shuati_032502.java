package shauti0325;

import java.util.Arrays;
import java.util.LinkedList;

/*
合并区间
 */
public class shuati_032502 {
    public int[][] merge(int[][] intervals) {
        LinkedList<int[]> ret = new LinkedList();
        //对数组进行排序,保证相隔数组之间是相邻的
        Arrays.sort(intervals,(o1, o2) -> Integer.compare(o1[0], o2[0]));
        ret.add(intervals[0]);
        for(int i = 1; i < intervals.length; i++) {
            if(intervals[i][0] <= ret.getLast()[1]) {
                //如果此时的链表中的最后一对数字的起始位置<=数组i位置的起始位置的话表示此时数组是有重复的
                //那么此时的链表的新的起始位置就是
                int start = ret.getLast()[0];
                //此时链表的结束位置必须是两个中最大的
                //ret.getLast()[1]中[]的下标必须为1,因为此时表示链表的最后一个数组
                //而链表中最后一个数组中最大的肯定是1下标的数组
                int end = Math.max(ret.getLast()[1],intervals[i][1]);
                //此时删除原来链表中的数组
                ret.removeLast();
                //将新数组加入 新数组的起始位置分别是上面校验的起始位置
                //ret中加入的是一维数组
                ret.add(new int[]{start,end});
            } else {
                //如果不相邻直接加入即可
                ret.add(intervals[i]);
            }
        }
        return ret.toArray(new int[ret.size()][]);
    }

}
