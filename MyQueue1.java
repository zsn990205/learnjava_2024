package Queue;

public class MyQueue1 {
    static class ListNode {
        public int val;
        public ListNode next;
        public ListNode prev;

        public ListNode(int val) {
            this.val = val;
        }
    }
        public ListNode head;
        public ListNode last;

        public boolean offer(int x) {
            ListNode node = new ListNode(x);
            if(head == null) {
                head = node;
                last = node;
            } else {
                //尾插法
                last.next = node;
                node.prev = last;
                last = last.next;
            }
            return true;
        }

        public int poll() {
            if(head == null) {
                return -1;
            }
            int ret = head.val;
            if(head == last) {
                head.next = null;
                head.prev = null;
                return ret;
            }
            head = head.next;
            head.prev = null;
            return ret;
        }

        public int peek() {
            if(head == null) {
                return -1;
            }
            return head.val;
        }

        public boolean empty() {
            if(head == null) {
                return true;
            }
            return false;
        }

        public int size() {
            if(head == null) {
                return 0;
            }
            int count = 0;
            ListNode cur = head;
            while(cur != null) {
                cur = cur.next;
                count++;
            }
            return count;
        }
    }

