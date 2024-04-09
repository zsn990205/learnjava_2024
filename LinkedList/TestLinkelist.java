package LinkedList;


public class TestLinkelist {
    public static void main(String[] args) {
        list list1 = new list();
        list1.addFirst(12);
        list1.addFirst(12);
        list1.addFirst(12);
        list1.addIndex(0,12);
        list1.addIndex(1,12);
        list1.addIndex(2,12);
        list1.display();
        list1.removeAll(12);
        list1.display();
    }

    public static void main1(String[] args) {
        list list1 = new list();
        //list1.createList();
        list1.addFirst(12);
        list1.addFirst(23);
        list1.addFirst(34);
        list1.addFirst(45);
        list1.addFirst(56);
        list1.display();
        System.out.println(list1.size());
        System.out.println("测试是否包含某个数");
        System.out.println(list1.contains(45));
        System.out.println(list1.contains(100));
    }
}
