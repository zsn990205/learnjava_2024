package LinkedListSubject;


public class Linked_test {
    static class ListNode {
        public int val;
        public ListNode next;   //引用类型

        public ListNode(int val) {
            this.val = val;
        }
    }

    public ListNode head;   //定义一个头节点

    //环形链表2
    public ListNode detectCycle(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while(fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
            if(slow == fast) {
                break;
            }
        }
        //以下这个情况表示无环
        if(fast == null || fast.next == null) {
            return null;
        }
        //相遇的时候一个从头走一个从相遇的地方走
        slow = head;
        while(slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        //相遇的点就是入口点
        return slow;
    }

    //环形链表
    public boolean hasCycle(ListNode head) {
        ListNode fast = head;
        ListNode slow = head;
        while(fast != null && fast.next != null) {
        //如果说fast或者fast.next=null表示此时无环
                fast = fast.next.next;
                slow = slow.next;
                if(fast == slow) {
                    return true;
                }

        }
        return false;
    }

    //相交链表
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        //1.先求两个链表分别的长度
        int countA = 0;
        ListNode curA = headA;
        ListNode curB = headB;
        while(curA != null) {
            countA++;
            curA = curA.next;
        }
        int countB = 0;
        while(curB != null) {
            countB++;
            curB = curB.next;
        }
        int len = countA - countB;
        len = Math.abs(len);     //求出绝对值

        curA = headA;
        curB = headB;
        //2.让长的链表先走len步
        if(countA > countB) {
            for(int i = 0; i < len; i++) {
                curA = curA.next;
            }
        } else {
            for(int i = 0; i < len; i++) {
                curB = curB.next;
            }
        }

        //3.让两个链表同时走
        while(curA != curB) {
            curA = curA.next;
            curB = curB.next;
        }
        return curA;
    }

    //大于x的放在后面,小于x的放在前面
    //保证原链表的顺序不变
    //pHead记录头指针的位置
    public ListNode partition(ListNode pHead,int x) {
        if(head == null) {
            return null;
        }
        //用来记录<x的部分
        ListNode bs = null;
        ListNode be = null;

        //用来记录>=x 的部分
        ListNode as = null;
        ListNode ae = null;
        ListNode cur = pHead;
        while(cur != null) {
            if(cur.val < x) {
                if(bs == null) {
                    //表示正在插入第一个结点
                    bs = cur;
                    be = cur;
                } else {
                    //be永远指向最后一个结点
                    be.next = cur;
                    be = be.next;
                }
            } else {
                if(as == null) {
                    as = cur;
                    ae = cur;
                } else {
                    ae.next = cur;
                    ae = ae.next;
                }
            }
            cur = cur.next;
        }
        //表示此时的值都是大于x的,前半部分没有值
        if(bs == null) {
            return as;
        }
        be.next = as;
        if(as != null) {
            //当链表>x的部分只有一个数据
            ae.next = null;
        }
        return pHead;
    }

    //反转一个链表
    public ListNode reverseList() {
        //空链表
        if(head == null) {
            return null;
        }
        //只有一个节点
        if(head.next == null) {
            return head;
        }
        //走到这个位置表示此时的链表是多个
        ListNode cur = head.next;
        head.next = null;
        while(cur != null) {
            ListNode curNext = cur.next;
            cur.next = head;
            head = cur;
            cur = curNext;
        }
        return head;
    }

    //找倒数第k个结点
    public ListNode KNode(int k) {
        if(k < 0 || head == null) {
            return null;
        }
        ListNode fast = head;
        ListNode slow = head;
        //先让fast走key-1步
        for(int i = 0; i < k - 1; i++) {
            fast = fast.next;
            //表示此时若k大于链表的长度时不合法的输出
            if(fast == null) {
                return null;
            }
        }
        while(fast.next != null) {

            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }

    public ListNode middleNode(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        //奇数结点fast最后为空
        //偶数结点fast.next为空
        while(fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    public ListNode middleNode1(ListNode head) {
        if(head == null) {
            return null;
        }
        if(head.next == null) {
            return head;
        }
        ListNode cur = head;
        int count = 0;
        while(cur != null) {
            if(cur.next != null) {
                count++;
                cur = cur.next;
            }
        }
        ListNode Head = head;
        for (int i = 0; i < count/2; i++) {
            Head = Head.next;
        }
        return Head;
    }

    public ListNode mergeTwoLists(ListNode head1, ListNode head2) {
        //newH是虚拟结点 固定不动的
        ListNode newH = new ListNode(-1);
        ListNode tmpH = newH;
        while(head1 != null && head2 != null) {
            if(head1.val < head2.val) {
                tmpH.next = head1;
                head1 = head1.next;
                tmpH = tmpH.next;
            } else {
                tmpH.next = head2;
                head2 = head2.next;
                tmpH = tmpH.next;
            }
        }
        //说明后面head2的数字都大于head1
        if(head1 != null) {
            tmpH.next = head1;
        }

        if(head2 != null) {
            tmpH.next = head2;
        }

        return newH.next;
    }

    public boolean chkPalindrome() {
        if(head == null && head.next == null) {
            return true;
        }
        //1.先找到中间结点的位置
        ListNode fast = head;
        ListNode slow = head;
        while(fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        //2.把后面的位置进行反转
        ListNode cur = slow.next;
        while(cur != null) {
            ListNode curNext = cur.next;
            cur.next = slow;
            slow = cur;
            cur = curNext;
        }
        //3.从前到后从后到前
        while(head != slow) {
            if(head.val != slow.val) {
                return false;
            }
            if(head.next == slow) {
                return true;
            }
            head = head.next;
            slow = slow.next;
        }
        return true;
    }
}
