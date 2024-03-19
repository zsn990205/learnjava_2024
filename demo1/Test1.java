package demo1;

abstract class Animal {
    public String name;
    public int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

interface Fly {
    void fly();
        }
interface Run {
    void run();
}
interface Swim {
    void swim();
}

class Dog extends Animal implements Run {
    public Dog(String name, int age) {
        super(name, age);
    }

    @Override
    public void run() {
        System.out.println(this.name+"正在跑");
    }
}

class Frog extends Animal implements Run,Swim {
    public Frog(String name, int age) {
        super(name, age);
    }

    @Override
    public void run() {
        System.out.println(this.name+"正在呱呱叫着跑");
    }

    @Override
    public void swim() {
        System.out.println(this.name+"正在蛙泳");
    }
}

class Duck extends Animal implements Run,Swim,Fly {
    public Duck(String name, int age) {
        super(name, age);
    }

    @Override
    public void fly() {
        System.out.println(this.name+"正在展翅高飞");
    }

    @Override
    public void run() {
        System.out.println(this.name+"正在迅速奔跑");
    }

    @Override
    public void swim() {
        System.out.println(this.name+"正在河里高傲的游泳");
    }
}
public class Test {
    public static void main(String[] args) {
        Dog dog = new Dog("也行",10);
        Frog frog = new Frog("蛙蛙",3);
        Duck duck = new Duck("鸭鸭",2);
    }
}
