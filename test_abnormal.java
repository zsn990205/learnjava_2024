package java_0308;


import java.util.Arrays;
import java.util.Scanner;

public class test_abnormal {
     public void merge(int A[], int m, int B[], int n) {
         for(int i = 0; i < n; i++){
             A[m+i] = B[i];
         }
         Arrays.sort(A);
     }

    public void merge3(int A[],int m,int B[],int n) {
        if(A == null && B == null) {
            return;
        }
        for(int i = m; i < A.length; i++) {
            for(int j = 0; j < n; j++) {
                A[i] = B[j];
            }
        }
        Arrays.sort(A);
    }

    public void merge2(int A[],int m,int B[],int n) {
        //m和n分别代表AB数组的长度
        int i = m-1;
        int j = n-1;
        int k = m+n-1;

        //循环的条件是i和j都不为0
        while(i >= 0 && j >= 0) {
            if (A[i] < B[j]) {
                A[k] = B[j];
                k--;
                j--;
            } else {
                A[k] = A[i];
                i--;
                k--;
            }
        }
            while(i >= 0) {
                //j走完了
                A[k] = A[i];
                i--;
                k--;
            }
            while(j >= 0) {
                A[k] = B[j];
                k--;
                j--;
            }
        }


    public static String func(String str) {
        boolean[] flg = new boolean[127];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            //abbasc
            char ch = str.charAt(i);
            if (!flg[ch]) {
                //关于这个字母的下标如果是false代表此时没有字符放入
                stringBuilder.append(ch);
                flg[ch] = true;
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {
            String str = in.nextLine();
            String ret = func(str);
            System.out.println(ret);
        }
        }


    public static void main1(String[] args) {
        Scanner in = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        boolean[] flg = new boolean[128];
        while (in.hasNextLine()) {
            String str = in.nextLine();
            for(int i = 0; i < str.length(); i++) {
                char ch = str.charAt(i);
                if(!flg[ch]) {
                    sb.append(ch);
                    flg[ch] = true;
                }
            }
            System.out.println(sb.toString());
            }

        }

}




