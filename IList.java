package DataStructer01;

public interface IList {
    //新增元素,默认在数组后新增
    public void add(int data);
    //在pos位置新增元素
    public void add(int pos,int data);
    //判断是否包含某个元素
    public boolean contains(int data);
    //查找某个元素对应的位置
    public int indexOf(int toFind);
    //获取pos位置的元素
    public int get(int pos);
    //把pos位置上的元素设置成val
    public void set(int pos,int val);
    //删除第一次出现的关键字key
    public void remove(int key);
    //获取顺序表的长度
    public int size();
    //遍历顺序表中的元素
    public void display();
    //判断顺序表是否已经满了
    public boolean isFull();
    //判断顺序表是否为空
    public boolean isEmpty();
    //数组清空
    public void clear();
}
