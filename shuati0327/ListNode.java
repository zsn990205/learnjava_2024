package shuati0327;

public class ListNode {
      int val;
      ListNode next;
      ListNode(int x) {
          val = x;
          next = null;
      }

      /*
      相交链表
       */
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
       //1.先去遍历完全A链表和B链表 分别求得A B的长度
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

        int len = Math.abs(countA - countB);

        //此时A B分别已经走到终点了
        curA = headA;
        curB = headB;
        //2.先让快的走len步长
        if(countA > countB) {
            for(int i = 0; i < len; i++) {
                curA = curA.next;
            }
        } else {
            for(int i = 0; i < len; i++) {
                curB = curB.next;
            }
        }

        //3.快的走完len长之后,齐步走相交的位置就是
        while(curA != curB) {
            curA = curA.next;
            curB = curB.next;
        }
        return curA;
    }

    /*
    反转链表
     */
    public ListNode reverseList(ListNode head) {
        ListNode cur = head;
        ListNode newHead = null;
        ListNode prev = null;
        while (cur != null) {
            ListNode curNext = cur.next;
            if (curNext == null) {
                newHead = cur;
            }
            cur.next = prev;
            prev = cur;
            cur = curNext;
        }
        return newHead;
    }

    /*
      环形链表
     */
    public boolean hasCycle(ListNode head) {
        if(head == null) {
            return false;
        }
        ListNode fast = head;
        ListNode slow = head;
        while(fast != null && fast.next != null) {
            //快指针走两步
            fast = fast.next.next;
            if(fast == slow) {
                //假如相遇说明此时链表是有环的
                return true;
            }
            //慢指针走一步
            slow = slow.next;
        }
        //没有环,快的比慢的走的快,所以一直不会相遇,是一条长链表
        return false;
    }

    /*
    回文链表
     */
    public boolean isPalindrome(ListNode head) {
        //1.先找到链表的中间位置记为middle,如果此时链表的总数是偶数,那么就要找到靠右的位置记为middle
        ListNode middle = middleNode(head);
        //2.在对中间位置往后的链表进行翻转
        ListNode head2 = reverseList2(middle);
        //3.从中间位置往后进行比对 查找每一个元素是否相等
        while(head2 != null) {
            if(head2.val != head.val) {
                return false;
            }
            head2 = head2.next;
            head = head.next;
        }
        return true;
    }

    private ListNode reverseList2(ListNode middle) {
        ListNode prev = null;
        ListNode newHead = null;
        ListNode cur = middle;
        while(cur != null) {
            ListNode curNext = cur.next;
            if(curNext == null) {
                newHead = cur;
            }
            cur.next = prev;
            prev = cur;
            cur = curNext;
        }
        return newHead;
    }

    private ListNode middleNode(ListNode head) {
        //问题归结到怎么去找链表的中间节点
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;

    }
}
