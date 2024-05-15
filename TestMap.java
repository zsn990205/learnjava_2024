package Java20240412;

import java.util.*;

class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
}

public class TestMap {
    public Node copyRandomList(Node head) {
        Map<Node, Node> map = new HashMap<>();
        Node cur = head;
        //1.第一次遍历链表,存储对应关系
        while (cur != null) {
            Node node = new Node(cur.val);
            map.put(cur,node);
            cur = cur.next;
        }

        //2.第二次修改链表,修改新链表的指向
        cur = head;
        while(cur != null) {
            map.get(cur).next = map.get(cur.next);    //map.get(k)-->k.val
            map.get(cur).random = map.get(cur.random);
            cur = cur.next;
        }
        //3.返回头节点
        return map.get(head);
    }

    //出现频率最高的单词(相同时按照字典序的大小输出)
    public List<String> topKFrequent(String[] words, int k) {
        //1.先统计单词出现的次数
        Map<String,Integer> map = new HashMap<>();
        for(int i = 0; i < words.length; i++) {
            if(map.get(words[i]) == null) {
                map.put(words[i],1);
            } else {
                int val = map.get(words[i]);
                map.put(words[i],val+1);
            }
        }
        //2.遍历好统计好的Map,把每组map存储到小根堆中
        PriorityQueue<Map.Entry<String,Integer>> minHeap =
                new PriorityQueue<>(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if(o1.getValue().compareTo(o2.getValue()) == 0) {
                    //如果频率相同的时候,建立大根堆
                    return o2.getKey().compareTo(o1.getKey());
                } else {
                    //反之建立小根堆
                    return o1.getValue().compareTo(o2.getValue());
                }
            }
        });
        for(Map.Entry<String,Integer> entry : map.entrySet()) {
            //如果小根堆的大小比k小
            if(minHeap.size() < k) {
                minHeap.offer(entry);  //当小根堆比我想得到的前k个还小,就加入新的一组数据
            } else {
                //我要得到的是最高频率出现的字母
                Map.Entry<String,Integer> top = minHeap.peek();
                if(top.getValue().compareTo(entry.getValue()) < 0) {
                    minHeap.poll();          //你出去
                    minHeap.offer(entry);   //我进来
                } else {
                    if(top.getValue().compareTo(entry.getValue()) == 0) {
                        //如果频率相同,那么按照字典序排列
                        if(top.getKey().compareTo(entry.getKey()) > 0) {
                            //当字典序大的时候,替换成小的
                            minHeap.poll();
                            minHeap.offer(entry);
                        }
                    }
                }
            }
        }
        List<String> ret = new ArrayList<>();
        //返回的值应该是从大到小的,所以在转一次
        for(int i = 0; i < k; i++) {
            Map.Entry<String,Integer> top = minHeap.poll();
            ret.add(top.getKey());
        }
        //对链表进行逆置
        Collections.reverse(ret);
        return ret;
    }

    public static void main(String[] args) {
        int[] arr = {1,2,2,2,3,3,4};
        Map<Integer,Integer> map = new HashMap<>();
        for(int i = 0; i < arr.length; i++) {
            if(map.get(arr[i]) == null) {
                map.put(arr[i],1);
            } else {
                int val = map.get(arr[i]);
                map.put(arr[i],val+1);
            }
        }
        System.out.println(map);
    }

    public static void main1(String[] args) {
        int[] arr = {1,2,2,3,3};
        //使用hashset可以去重
        HashSet<Integer> set = new HashSet<>();
        for(int i = 0; i < arr.length; i++) {
            set.add(arr[i]);
        }
        System.out.print(set);
    }
}
