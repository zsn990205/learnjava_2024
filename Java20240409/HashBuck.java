package Java20240409;

//哈希---拉链法
public class HashBuck {
    static class Node {
        public int key;
        public int val;
        public Node next;

        public Node(int key,int val) {
            this.key = key;
            this.val = val;
        }
    }

    public Node[] arr;
    public int usedSize;  //存放了多少个数据

    public HashBuck() {
        arr = new Node[10];
    }

    public void put(int key,int val) {
        int index = key % arr.length;
        Node cur = arr[index];
        while(cur != null) {
            if(cur.key == key) {
                cur.val = val;
                return;
            }
            cur = cur.next;
        }
        Node node = new Node(key,val);
        node.next = arr[index];
        arr[index] = node;
        usedSize++;
    }

    //扩容
    public void resize() {
        Node[] newArr =  new Node[2*arr.length];
        for(int i = 0; i < arr.length; i++) {
            Node cur = arr[i];
            while(cur != null) {
                int newIndex = cur.key % newArr.length;  //新数组的下标
                //采用头插法插入
                //因为是头插法,所以需要记录cur.next否则会找成新插入的结点
                Node tmp = cur.next;
                cur.next = newArr[newIndex];
                newArr[newIndex] = cur;
                cur = tmp;
            }
        }
        arr = newArr;
    }

    public int get(int key) {
        int index = key % arr.length;
        Node cur = arr[index];
        while(cur != null) {
            if(cur.key == key) {
                return cur.val;
            }
            cur = cur.next;
        }
        return -1;
    }

    public static void main(String[] args) {
        HashBuck hashBuck = new HashBuck();
        hashBuck.put(1,11);
        hashBuck.put(2,22);
        hashBuck.put(3,99);
        hashBuck.put(4,99);
        hashBuck.put(5,99);
        hashBuck.put(6,99);
        hashBuck.put(7,99);
        hashBuck.put(8,99);
        hashBuck.put(9,99);

        System.out.println(hashBuck.get(9));
    }
}
