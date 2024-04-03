package LinkedList;

public class LinkedList2 implements IList{
    static class ListNode {
        public int val;
        public ListNode head;
        public ListNode prev;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
        }
    }

    public ListNode head;  //标记头
    public ListNode last; //标记尾

    @Override
    public void addFirst(int data) {
        ListNode node = new ListNode(data);
        //如果头节点为空
        if(head == null) {
            head = node;
            last = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
    }

    @Override
    public void addLast(int data) {
        ListNode node = new ListNode(data);
        if(head == null) {
            head = node;
            last = node;
        } else {
            last.next = node;
            node.prev = last;
            last = node;
        }
    }

    @Override
    public void addIndex(int index, int data) {
        //检查index是否合法
        if(index < 0 || index > size()) {
            return;
        }
        ListNode node = new ListNode(data);
        ListNode cur = head;
        if(index == 0) {
            addFirst(data);
            return;
        } else if(index == size()) {
            addLast(data);
            return;
        } else {
            for(int i = 0; i < index; i++) {
                cur = cur.next;
            }
            node.next = cur;
            node.prev = cur.prev;
            cur.prev.next = node;
            cur.prev = node;
        }
    }

    @Override
    public boolean contains(int key) {
        ListNode cur = head;
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
        ListNode cur = head;
        while(cur != null) {
            if(cur.val == key) {
                if(cur == head) {
                    if(head != null) {
                        head.prev = null;
                    }
                    head = head.next;
                    last = null;
                } else if(cur.next == null) {
                    cur.prev.next = null;
                    last = last.prev;
                } else {
                    cur.prev.next = cur.next;
                    cur.next.prev = cur.prev;
                }
                return;
            } else {
                cur = cur.next;
            }
        }
    }

    @Override
    public void removeAll(int key) {
        ListNode cur = head;
        while(cur != null) {
            if(cur.val == key) {
                if(cur == head) {
                    if(head != null) {
                        head.prev = null;
                    }
                    head = head.next;
                } else if(cur.next == null) {
                    cur.prev.next = null;
                    last = last.prev;
                } else {
                    cur.prev.next = cur.next;
                    cur.next.prev = cur.prev;
                }
            }
                cur = cur.next;
            }
        }


    @Override
    public int size() {
        int count = 0;
        ListNode cur = head;
        while(cur != null) {
            count++;
            cur = cur.next;
        }
        return count;
    }

    @Override
    public void clear() {
        ListNode cur = head;
        while(cur != null) {
            ListNode tmp = cur.next;
            cur.prev = null;
            cur.next = null;
            cur = tmp;
        }
            last = null;
            head = null;

    }

    @Override
    public void display() {
        ListNode cur = head;
        while(cur != null) {
            System.out.print(cur.val+" ");
            cur = cur.next;
        }
        System.out.println();
    }
}
