package Java20240409;

import java.util.*;
public class HashBuck2<K,V> {
    static class Node<K, V> {
        public K key;
        public V val;
        public Node<K, V> next;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    public static int singleNumber(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            if (!set.contains(nums[i])) {
                set.add(nums[i]);
            } else {
                set.remove(nums[i]);
            }
        }
        for (int i = 0; i < nums.length; i++) {
            if (set.contains(nums[i])) {
                return nums[i];
            }
        }
        return -1;
    }

    public static int numJewelsInStones(String jewels, String stones) {
        int count = 0;
        for (int i = 0; i < jewels.length(); i++) {
            for (int j = 0; j < stones.length(); j++) {
                char ch1 = jewels.charAt(i);
                char ch2 = stones.charAt(j);
                if (ch1 == ch2) {
                    count++;
                }
            }
        }
        return count;
    }

    public static int numJewelsInStones2(String jewels, String stones) {
        HashSet<Character> set = new HashSet<>();
        for (int i = 0; i < jewels.length(); i++) {
            set.add(jewels.charAt(i));
        }
        int count = 0;
        for (int j = 0; j < stones.length(); j++) {
            if (set.contains(stones.charAt(j))) {
                count++;
            }
        }
        return count;
    }

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextInt()) {
                String str1 = scanner.nextLine();
                String str2 = scanner.nextLine();
                func(str1, str2);
            }
        }

        public static void func(String str1, String str2) {
            HashSet<Character> set = new HashSet<>();
            HashSet<Character> set2 = new HashSet<>();
            str2.toUpperCase();  //将字符串转换成大写的数组
            for (int i = 0; i < str2.length(); i++) {
                char ch = str2.charAt(i);
                set.add(ch);
            }

            for (int j = 0; j < str1.length(); j++) {
                char ch1 = str1.charAt(j);
                if (!set.contains(ch1) && !set2.contains(ch1)) {
                    set2.add(ch1);
                    System.out.print(ch1);
                }
            }
        }

        public static void main1(String[] args) {
            String jewels = "z";
            String stones = "ZZ";
            System.out.println(numJewelsInStones2(jewels, stones));
        }
    }

