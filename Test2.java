package demo3;
class Money implements Cloneable {
    public double m = 19.9;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

class Person implements Cloneable{
    public String name;
    public int age;

    Money money = new Money();
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        Person tmp = (Person)super.clone();
        tmp.money = (Money)this.money.clone();
        return tmp;
        //刚开始这里只克隆person了没看到克隆money
        //return super.clone();
    }
}

public class Test {
    public static void main(String[] args) throws CloneNotSupportedException {
        Person person = new Person("张三",10);
        Person person2 = (Person)person.clone();

        System.out.println("person: "+person.money.m);
        System.out.println("person2: "+person2.money.m);
        System.out.println("======================");
        person.money.m = 99.9;
        System.out.println("person: "+person.money.m);
        System.out.println("person2: "+person2.money.m);
    }
}
