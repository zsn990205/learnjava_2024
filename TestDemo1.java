package java_0308;



import java.util.Scanner;

public class TestDemo1 {
    public static boolean isNumOrCharacter(char s) {
        //如果传入的字符是0-9/a-z返回true
        if(s >= '0' && s <= '9' || s >= 'a' && s <= 'z') {
            return true;
        }
        return false;
    }

    public boolean isPalindrome(String s) {
        s = s.toLowerCase();
        //定义一个j和j下标 当i和j下标相遇的时候是回文
        int i =0;
        int j = s.length() - 1;
        //i和j不相遇的时候就一直循环
        while(i < j) {
            //当i的位置不合法的时候一直走到合法位置
            while(i < j && !isNumOrCharacter(s.charAt(i))) {
                i++;
            }
            //当j的位置不合法的时候一直走到合法位置
            while(i < j && !isNumOrCharacter(s.charAt(j))) {
                j--;
            }
            if(s.charAt(i) == s.charAt(j)) {
                i++;
                j--;
            } else {
                return false;
            }
       }
        return true;
    }

    //字符串中第一个唯一的字符
    public static int firstUniqChar(String s) {
        int[] count = new int[26];
        for(int i = 0; i < s.length(); i++) {
            //遍历整个字符串
            char str = s.charAt(i);
            count[str-'a']++;
        }
        for(int i = 0; i < s.length(); i++) {
            char str = s.charAt(i);
            if(count[str - 'a'] == 1) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String s = "hello world";
        int index = s.lastIndexOf(" ");
        String ret = s.substring(index+1);
        System.out.println(ret);
        System.out.println(index);
        System.out.println(s.length());
    }

    public static void main2(String[] args) {
        //hello world找到最后一个空格对应的字符串进行遍历并求取长度
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            //找到最后一个空格的下标
            int index = str.lastIndexOf(' ');
            //从最后一个下标的下一个数字开始截取
            String str2 = str.substring(index + 1);
            System.out.println(str2.length());

        }
    }

    public static void main1(String[] args) {
        //System.out.println('b'-'a');
        String s = "bcab";
        int ret = firstUniqChar(s);
        System.out.println(ret);
    }
}
