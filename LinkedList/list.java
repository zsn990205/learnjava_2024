package LinkedList;


public class list implements IList{
    //定义节点的内部类
    static class ListNode {
        public int val;
        public ListNode next;   //引用类型

        public ListNode(int val) {
            this.val = val;
        }
    }
    public ListNode head;   //定义一个头节点

    public void createList() {
        ListNode node1 = new ListNode(12);
        ListNode node2 = new ListNode(23);
        ListNode node3 = new ListNode(34);
        ListNode node4 = new ListNode(45);
        ListNode node5 = new ListNode(56);

        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node5;

        this.head = node1;    //头节点的位置
    }

    @Override
    public void addFirst(int data) {
        ListNode newHead = new ListNode(data);
            newHead.next = this.head;
            this.head = newHead;
        }

    //尾插法
    @Override
    public void addLast(int data) {
        ListNode node = new ListNode(data);
        ListNode cur = this.head;
        if(head == null) {
            addFirst(data);
        } else {
            while(cur.next != null) {
                cur = cur.next;
            }
            //走到这个位置表示当前的cur.next为空了
            cur.next = node;
        }
    }

    @Override
    public void addIndex(int index, int data) {
        if(index < 0 || index >size()) {
            //throw new myException("index位置不合法");
            return;
        }
        if(index == 0) {
            addFirst(data);
            return;
        }
        if(index == size()) {
            addLast(data);
            return;
        }

        //代码走到这个位置表示就是随机的一个位置
        //此时我们应该标记出随机的位置是哪一个
        //并且找到他的前一个位置
        ListNode node = new ListNode(data);
        ListNode prev = searchPre(index);
        node.next = prev.next;
        prev.next = node;
    }

    private ListNode searchPre(int index) {
        ListNode cur = this.head;
        int count = 0;
        while(count != index - 1) {
            count++;
            cur = cur.next;
        }
        return cur;
    }

    @Override
    public boolean contains(int key) {
        ListNode cur = this.head;
        while(cur != null) {
            if(cur.val == key) {
                return true;
            }
            cur = cur.next;
        }
        return false;
    }

    @Override
    public void remove(int key) {
        ListNode cur = this.head;
        if(this.head == null) {
            return;
        }
        if(this.head.val == key) {
            this.head = this.head.next;
            return;
        }
        while(cur.next != null) {
            if(cur.next.val == key) {
                cur.next = cur.next.next;
                return;
            }
            cur = cur.next;
        }
    }

    @Override
    public void removeAll(int key) {
        if(this.head == null) {
            return;
        }
        ListNode prev = this.head;
        ListNode cur = prev.next;
        while(cur != null) {
            if(cur.val == key) {
                prev.next = cur.next;
                cur = cur.next;
            } else {
                prev = cur;
                cur = cur.next;
            }
        }
        if(head.val == key) {
            head = head.next;
        }
    }

    @Override
    public int size() {
        int count = 0;
        ListNode cur = this.head;
        while(cur != null) {
            count++;
            cur = cur.next;
        }
        return count;
    }

    @Override
    public void clear() {
        ListNode cur = this.head;
        while(cur != null) {
            ListNode curNext = cur.next;
            cur.next = null;
            cur = curNext;
        }
            head = null;
    }

    @Override
    public void display() {
        ListNode cur = this.head;
        while(cur != null) {
            System.out.print(cur.val+" ");
            cur = cur.next;
        }
        System.out.println();
    }
}
