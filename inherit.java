
class Bse {
    public int a = 99;

    public void method() {
        System.out.println("Base中的method方法: ");
    }
}
class Der extends Bse {
    public int a = 199;

    public void method2() {
        System.out.println("Derived中的method方法: ");
    }

    public void method() {
        System.out.println("Derived中的method方法: ");
    }

    public void test() {
        super.method();
        method2();
        System.out.println("==============");
        System.out.println(a);
        System.out.println(this.a);
        System.out.println(super.a);
    }
}

public class inherit {

}
