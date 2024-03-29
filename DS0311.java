package DataStructer01;

import java.util.ArrayList;
import java.util.List;

public class DS0311 {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        List<Integer> list1 = list.subList(1,3);
        System.out.println(list1);
        System.out.println("===============");
        list1.set(0,99);
        System.out.println(list1);
        System.out.println(list);

    }

    public static void main2(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(0,99);
        for(int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i)+" ");
        }
        System.out.println();
        System.out.println("================");
        System.out.println(list);
    }

    public static void main1(String[] args) {
        MyArrayList myArrayList = new MyArrayList();
        myArrayList.add(1);
        myArrayList.add(2);
        myArrayList.add(1,199);
        myArrayList.display();
        System.out.println(myArrayList.contains(1));
        System.out.println(myArrayList.contains(199));
        System.out.println(myArrayList.contains(10));
        myArrayList.set(2,15);
        myArrayList.display();
        myArrayList.remove(15);
        myArrayList.display();
        System.out.println("==========");
        myArrayList.clear();
        myArrayList.display();
    }
}
