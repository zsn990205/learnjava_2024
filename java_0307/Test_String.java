package java_0307;


import java.util.Arrays;

public class Test_String {
    public static void main(String[] args) {
        String str = "hdfsdfsd";
        String ret = str.substring(0,3);  //进行截取[ )
        System.out.println(ret);
        System.out.println("============");

        String str2 = "  hdfs  dfsd  ";
        String ret2 = str2.trim();
    }

    public static void main6(String[] args) {
        String str = "hello world";
        String str2 = "196.36.2.1";
        String str3 = "name=张三&age=5";
        String[] ret = str3.split("=|&");
        System.out.println(Arrays.toString(ret));
    }

    public static void main5(String[] args) {
        String str2 = "aabdabcaaaacddbbb";
        String ret = str2.replace("aaa","2222");
        System.out.println(ret);
    }

    public static void main4(String[] args) {
        String str = String.valueOf(19.9);
        String str2 = "hello";
        //转变之后并不会改变原来的值,重新成为一个新的对象
        String ret = str2.toUpperCase();
        char[] ret2 = str2.toCharArray();
        System.out.println(ret);
        String s = String.format("%d-%d-%d",2024,3,7);
    }

    public static void main3(String[] args) {
        String str = "hello";
        int index = str.indexOf("l");
        System.out.println(index);

        String str2 = "aabdabcbbb";
        int index2 = str2.indexOf("abc",3);
        System.out.println(index2);
    }

    public static void main2(String[] args) {
        String str = "hello";
        for(int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            System.out.println(c);
        }

    }

    public static void main1(String[] args) {
        String str = "hello";
        String str2 = new String("hello");
        String str3 = "";      //表示这个str3指向的引用是空的,啥也没储存
        String str4 = null;    //表示这个str4是空的,不指向任何引用
        String str5 = "abc";
        String str6 = "Abc";

        System.out.println(str.length());
        System.out.println(str.equals(str2));
        System.out.println(str.compareTo(str3));
        System.out.println(str5.compareToIgnoreCase(str6));
        System.out.println(str == str2);
    }
}
