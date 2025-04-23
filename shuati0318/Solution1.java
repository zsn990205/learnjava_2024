package shuati0318;


import java.util.*;

public class Solution1 {
    public static List<List<String>> groupAnagrams(String[] strs) {
        int len = strs.length;
        Map<String,List<String>> map = new HashMap<>();

        for(int i = 0; i < len; i++) {
            //将字符串换成字母
            //比如"eat"-> [e,a,t]
            char[] ch = strs[i].toCharArray();
            //对其进行排序
            //排序得到的结果是[a,e,t]
            Arrays.sort(ch);
            //再将其转成字符串类型
            String str = String.valueOf(ch);

            if(!map.containsKey(str)) {
                //假如当前不存在就加入map中
                map.put(str,new ArrayList<>());
            }

            //直接对字符进行加入
            map.get(str).add(strs[i]);
        }
        return new ArrayList<>(map.values());
    }

    public static void main(String[] args) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("aet",new ArrayList<>());

        System.out.println("==========");
        map.get("aet").add("eat");

    }
}
