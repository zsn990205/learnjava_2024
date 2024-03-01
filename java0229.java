import java.util.Random;
import java.util.Scanner;

public class java0229 {
    //获取一个数二进制序列中所有的奇数和偶数位
    public static void main(String[] args) {
        int n = 7;
        for (int i = 31; i >= 0; i-= 2) {
            System.out.print(((n >>> i) & 1) +" ");
        }
        System.out.println();
        for (int i = 30; i >= 0; i-= 2) {
            System.out.print(((n >>> i) & 1) +" ");
        }
    }

    //数字返回参数二进制是1的个数
    public static void main2(String[] args) {
         int n = 6;
         int count = 0;
         while (n != 0) {
             count++;
             n = n & (n-1);
         }
        System.out.println(count);
    }

    //求自幂数.水仙花数.四叶玫瑰数.五角星数.六合数
    //153=1^3+5^3+3^3
    public static void main1(String[] args) {
        for(int i = 0; i <= 999999; i++) {
            //计算当前有几位数字(用于之后的几次方)
            //如果是直接用i的话,后面经过计算i的值可能为0就不好计算了
            //在此处使用tmp来保存原本的i的值
            int tmp = i;
            int count = 0;
            while(tmp != 0) {
                count++;
                tmp = tmp / 10;
            }
            tmp = i;  //在这个位置tmp已经除完了可能都是0了,
                      // 此时重新将i的值赋给tmp,是为了计算其余数为了幂次方
            int sum = 0;
            while(tmp != 0) {
                sum += Math.pow(tmp % 10,count);  //计算余数准备次方
                tmp /= 10;  //计算每一位的余数
            }
            if(sum == i) {
                System.out.println(i);
            }
        }

    }

}
