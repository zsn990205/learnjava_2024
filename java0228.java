
import java.util.Random;
import java.util.Scanner;

public class java0228 {
    //输入乘法口诀表
    public static void main7(String[] args) {
        Scanner sn = new Scanner(System.in);
        int a = sn.nextInt();
        for(int i = 1; i <= a; i++) {
            for(int j = 1; j <= i; j++) {
                System.out.print(j+"*"+i+"="+i*j+" ");
            }
            System.out.println();
        }
    }

    //最多输入三次密码,输入正确显示登陆成功,输入错误重新输入,三次均错直接退出程序
    public static void main6(String[] args) {
        Scanner sn = new Scanner(System.in);
        int count = 3;
        while(count != 0 ) {
            System.out.println("请输入您的密码: "+"你还有"+count+"次机会");
            String password = sn.nextLine();
            if(password.equals("1234")) {
                System.out.println("登录成功!");
                break;
            } else {
                System.out.println("登录失败请您重新输入密码");
                count--;
            }
            }
    }

    public static void main5(String[] args) {
        //打印n*n的表格出来
        //此处打印的是正方形表格,所以输入一个数字即可
        //打印矩形的话要求a,b不等
        Scanner sn = new Scanner(System.in);
        int a = sn.nextInt();
        for(int i = 0; i < a; i++) {
            for(int j = 0; j < a; j++) {
                System.out.print("#");
            }
            System.out.println("");
        }
    }

    //打印题目中要求的图形
    public static void main4(String[] args) {
        Scanner sn = new Scanner(System.in);
        int a = sn.nextInt();
        for(int i = 0; i < a; i++) {
            //坐标都是从0开始的所以此处不取=
            for(int j = 0; j < a; j++) {
                if (i == j || i + j == (a-1)) {
                    System.out.print("*");
                }
                    else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }

    //计算1/1-1/2+1/3+...+1/99-1/100的值
    public static void main3(String[] args) {
        double sum1 = 0;
        double sum2 = 0;
        for(int i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                sum1 += -(1.0 / i);
            } else {
                sum2 += 1.0 / i;
            }
        }
        System.out.println(sum1+sum2);
    }

    //求两个数的最大公约数
    public static void main2(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = a % b;
        while (c != 0) {
            a = b;
            b = c;
            c = a % b;
        }
        System.out.println(b);
    }

    public static void main1(String[] args) {
        //给定一个数字判断该数字是不是素数
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        //num % [2, i-1]
        int i = 2;
        for (; i < num; i++) {
            //不等于是因为,这里任何数%自身都等于0
            if (num % i == 0) {
                System.out.println("您输入的数字不是素数");
                break;
            }
            }
           //代码走到这个位置的时候表示我把num之前的数字都%完了
           //此时若i=num表示他只能被自己整除了
           if (i % num == 0) {
               System.out.println("您输入的数字是素数");
           }
        }
    }

