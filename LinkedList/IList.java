package LinkedList;

public interface IList {
    //头插法
    void addFirst(int data);
    //尾插法
    void addLast(int data);
    //任意位置插入,第一个数据节点为0下标
    void addIndex(int index,int data);
    //查找关键字key是否在链表中
    boolean contains(int key);
    //删除第一次出现关键字key的节点
    void remove(int key);
    //删除所有出现关键字key的节点
    void removeAll(int key);
    //得到单链表的长度
    int size();
    //清空单链表
    void clear();
    //显示这个单链表
    void display();
}
