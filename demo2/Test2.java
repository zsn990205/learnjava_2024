package demo2;

//class student implements Comparable<student> {
//    public String name;
//    public int age;
//
//    public student(String name, int age) {
//        this.name = name;
//        this.age = age;
//    }
//
//    //需要重写compareto方法
//    @Override
//    public int compareTo(student o) {
//        if(this.age > o.age) {
//            return 1;
//        } else if(this.age < age) {
//            return -1;
//        } else {
//            return 0;
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "student{" +
//                "name='" + name + '\'' +
//                ", age=" + age +
//                '}';
//    }
//}

import java.util.Comparator;

class student {
    public String name;
    public int age;

    public student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

//自己实现一个比较器(比如说我们想根据年龄比较对象)
class AgeComparator implements Comparator<student> {

    @Override
    public int compare(student o1, student o2) {
        return o1.age - o2.age;
    }
}
//根据姓名比较
class NameComparator implements Comparator<student> {
    @Override
    public int compare(student o1, student o2) {
        return o1.name.compareTo(o2.name);
    }
}

public class Test2 {
    public static void main(String[] args) {
        student student1 = new student("张三",10);
        student student2 = new student("李四",4);
        //System.out.println(student1.compareTo(student2));

        AgeComparator ageComparator = new AgeComparator();
        System.out.println(ageComparator.compare(student1, student2));

        NameComparator nameComparator = new NameComparator();
        System.out.println(nameComparator.compare(student1,student2));
    }
}
