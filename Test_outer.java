package demo4;

class OuterClass {
//外部类
    public int a;
    public static int a2;


    class InnerClass {
//内部类
        public int a3;
        //public static int a3;
        public static final int a4 = 0;

        public void test() {
            System.out.println("InnerClass: 这是内部类的test");
            System.out.println(a);
            System.out.println(a2);
            System.out.println(a3);
            System.out.println(a4);
        }
    }

    public void test() {
        System.out.println("OuterClass: 这是外部类的test");
        InnerClass innerClass = new InnerClass();
        System.out.println(innerClass.a3);
    }
}

class Out {
    public int a;
    public static int a2;

    static class Inner {
        //内部类
        public int a3;
        //public static int a3;
        public static final int a4 = 0;

        public void test() {
            Out out = new Out();
            System.out.println("Inner: 这是内部类的test");
            System.out.println(out.a);
            System.out.println(a2);
            System.out.println(a3);
            System.out.println(a4);
        }
    }
        public void test() {
            System.out.println("Outer: 这是外部类的test");

        }
    }


public class Test {
    public static void main(String[] args) {
        Out.Inner inner = new Out.Inner();
        inner.test();
    }

    public static void main1(String[] args) {
        OuterClass OC = new OuterClass();
        OuterClass.InnerClass innerClass = OC.new InnerClass();
    }
}
