

public class java_fun {
    public static int fib_new(int n ) {
        if(n == 1 || n == 2) {
            return 1;
        }
        int f1 = 1;
        int f2 = 1;
        int f3 = 0;
        for(int i = 3; i <= n; i++) {
            f3 = f1 + f2;
            f1 = f2;
            f2 = f3;
        }
        return f3;
    }


    //n代表的是项数
    //重复计算的次数比较多,不推荐,在这使用循环的方式
    public static int fib(int n) {
        if(n == 0) {
            return 0;
        }
        if(n == 1) {
            return 1;
        }
        return fib(n-1) + fib(n-2);
    }

    public static void main(String[] args) {
        System.out.println(fib_new(5));
        System.out.println(fib_new(6));
    }

    public static void main6(String[] args) {
        System.out.println(ret_sum(567));
    }

    //打印1+2+3+4
    public static int ret_sum(int n) {
        if (n < 10) {
            return n;
        }
            return (n % 10) + ret_sum(n / 10);
    }

    //按顺序打印每一位数
    //1234=>1 2 3 4
    public static void ret(int n) {
        if (n / 10 == 0) {
            System.out.println(n);
            return;
        }
            ret(n / 10);
            System.out.println(n % 10);
    }

    public static void main5(String[] args) {
        ret(1234);
    }

    //方法的重载(即就是方法名相同,但是返回值类型/参数类型/参数个数一定是不同的)
    public static int add(int a, int b) {
        return a + b;
    }

    public static double add(double a, double b) {
        return a + b;
    }

    public static int add(int a, int b, int c) {
        return a + b;
    }

    public static void main4(String[] args) {
        System.out.println(add(1, 2));
        System.out.println(add(1.0, 34.0));
        System.out.println(add(1, 2, 3));
    }

    //交换两个数字
    public static void swap(int[] a) {
        int tmp = a[0];
        a[0] = a[1];
        a[1] = tmp;
    }

    public static void main3(String[] args) {
        int[] arr = {10, 20};
        System.out.println("arr[0]= "+arr[0] + " arr[1]= " + arr[1]);
        swap(arr);
        System.out.println("arr[0]= "+arr[0] + " arr[1]= " + arr[1]);
    }

    //求各个数字的阶乘
    public static int fuc(int n) {
        if (n == 1) {
            return 1;
        }
            return n * fuc((n-1)) ;
    }
    //求各个阶乘的和
    public static int sum_fuc(int n) {
        int sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += fuc(i);
        }
        return sum;
    }

    public static void main2(String[] args) {
        //直接调用即可
        System.out.println(sum_fuc(5));
    }

    public static boolean isLeapYear(int year) {
        //如果是世纪闰年的话%400=0
        //如果是普通闰年的话%4=0 但是%100!=0
        if((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static void main1(String[] args) {
        System.out.println(isLeapYear(2018));
    }
}
