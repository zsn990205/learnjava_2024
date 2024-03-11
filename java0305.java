
class Base {
    public int a = 9;
    public int b = 99;
}
class Derived extends Base {
    public int a = 88;
    public void method() {
        System.out.println("a: "+ a);
        System.out.println("b: "+ b);

    }
}

class B {
    public B() {
        func();
    }

    public void func() {
        System.out.println("B,func()");
    }
}
class D extends B {
    public int count = 1;
    public void func() {
        System.out.println("D,func() "+count);
    }
}


public class java0305 {
    public static void main(String[] args) {
        D d = new D();
    }


    public static void main1(String[] args) {
        Derived d = new Derived();
        d.method();
    }
}
