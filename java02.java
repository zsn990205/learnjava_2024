import java.util.Scanner;

public class java02 {
    public static void main(String[] args) {
        //计算数字9出现的次数
        int count = 0;
        for(int i = 1; i <= 100; i++) {
            if(i / 10 == 9) {
                count++;
            }
            //其中99包含2个9 它既是99/10=9,也是99%10=9
            //因此在上边的条件语句+1一次,在下面的条件语句在+1一次
            if(i % 10 == 9){
                count++;
            }
        }
        System.out.println(count);
        }


    public static void main9(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = 0;  //用来计数的
        int sum = 0;  //用来算总和的
        while(scanner.hasNextInt()) {
            int num = scanner.nextInt();
            sum += num;
            count++;
        }
        System.out.println("总和为: "+ sum);
        System.out.println("平均数为: "+ sum / count);
        scanner.close();
    }

    public static void main8(String[] args) {
        //java常见的输入方式
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您的姓名: ");
        String name = scanner.nextLine();
        System.out.println(name);
        System.out.println("------------");
        String name2 = scanner.next();
        System.out.println(name2);
    }

    public static void main7(String[] args) {
        //找到1-100之间既能被3整除也能被5整除的数据
        int i = 1;
        while(i <= 100) {
            if (i % 3 == 0 && i % 5 == 0) {
                System.out.println(i);
            }
            i++;
        }
    }

    public static void main6(String[] args) {
        //求1!+2!+...+5!
        int sum = 0;
        int i = 1;
        while(i <= 5) {
            //每次重新算新的数字的阶乘
            int a = 1;
            int ret = 1;
            //算阶乘
            while(a <= i) {
                ret *= a;
                a ++;
            }
            sum += ret;
            i ++;
        }
        System.out.println(sum);
    }

    public static void main5(String[] args) {
        //求某一个数字的阶乘
        int a = 1;
        int ret = 1;
        while(a <= 4) {
            ret *= a;
            a += 1;
        }
        System.out.println(ret);
    }

    public static void main4(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int year = scanner.nextInt();
        if(year % 400 == 0 && year % 4 == 0) {
            System.out.println("是闰年");
        } else {
            System.out.println("不是闰年");
        }
    }

    public static void main3(String[] args) {
        System.out.println(11.5 % 2);
        int a = 4;
        double b = 5.0;
        a += b;
        double f = a + b;
        System.out.println(f);
        System.out.println(a);
    }

    public static void main2(String[] args) {
        int a = 10;
        System.out.println(a);
        String b = "1";
        System.out.println(b);
        String str = String.valueOf(a);
        System.out.println(str);
    }

    public static void main1(String[] args) {
         int b = 20;
         int a = 10;
        System.out.println("a = "+ a +","+"b = " + b);
    }
}
