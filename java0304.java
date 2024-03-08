/*
当我们写代码的时候,猫和狗都有姓名和年龄
同时也有共同的特征:吃!,为了减少代码的冗余
我们将她们的共性写成一个类,让其继承这个类即可
优化后的代码就显得很简洁明了了
 */

class Animal {
    public String name;
    public int age;

    static {
        System.out.println("static Animal");
    }
    {
        System.out.println("Animal shili");
    }
    public void eat() {
        System.out.println(this.name + "正在吃狗粮");
    }
    public Animal(String name,int age) {
        this.name = name;
        this.age = age;
        System.out.println("Animal的初始化");
    }
}

class Dog extends Animal{
    static {
        System.out.println("static Animal");
    }
    {
        System.out.println("Dog shili");
    }
    public void eat() {
        System.out.println(this.name + "正在吃狗粮");
    }
    public Dog(String name,int age) {
        super(name,age);
        System.out.println("Dog的初始化");
    }

    public void barf() {
        System.out.println(this.name+"正在汪汪叫");
    }

}

//class Cat extends Animal{
//    public void miao() {
//        System.out.println(this.name+"正在喵喵叫");
//    }
//}


public class java0304 {
    public static void main(String[] args) {
        Dog d = new Dog("也行",10);
    }
}
