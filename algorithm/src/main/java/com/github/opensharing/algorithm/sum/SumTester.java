package com.github.opensharing.algorithm.sum;

/**
 * 两个倒序链表求和
 */
public class SumTester {

    public static void main(String[] args) {
        //243: 3->4->2
        ListNode l1 = new ListNode(3, new ListNode(4, new ListNode(2)));
        //564: 4->6->5
        ListNode l2 = new ListNode(4, new ListNode(6, new ListNode(5)));

        //807: 7->0->8
        ListNode l3 = addTwoNumbers(l1, l2);

        ListNode next = l3;
        do {
            System.out.print(next.val);
        } while ((next = next.next) != null);
    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {

        return addTwoNumbers(0, l1, l2);
    }

    public static ListNode addTwoNumbers(int up, ListNode l1, ListNode l2) {
        if (l1 == null && l2 == null) {
            return up > 0 ? new ListNode(up) : null;
        }

        int sum = (l1 == null ? 0 : l1.val)  +  (l2 == null ? 0 : l2.val) + up;
        int nextUp = sum >= 10 ? 1 : 0;
        int curr = sum % 10;

        return new ListNode(curr,
                addTwoNumbers(nextUp, (l1 == null ? null : l1.next), (l2 == null ? null : l2.next))
        );
    }

    public static class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }
}
