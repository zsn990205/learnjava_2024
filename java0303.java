
//类和对象的学习
class Person{
    //属性  成员变量
    //public 访问修饰限定符
    public String name;  //名字是每个人的共性
    public int age;     //年龄也是

    //静态成员变量
    public static int count = 10;
    //人的行为  成员方法
    public void sleep() {

    }
}


class PetDog {
    public String name;
    public String color;

    public void barks() {
        System.out.println(color + name +"汪汪叫~~~");
    }
}
public class java0303 {
    //static是从上到小执行的(都会执行)
    static int cnt = 6;

    static {
        cnt += 9;
    }

    public static void main1(String[] args) {
        System.out.println("cnt=" + cnt);
    }

    static {
        cnt /= 3;
    }


    class Test {
        public String toString () {
        System.out.println("aaa");
        return "bbb";
    }
    }

}