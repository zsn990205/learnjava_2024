package shuati0320;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode() {}
      TreeNode(int val) { this.val = val; }
      TreeNode(int val, TreeNode left, TreeNode right) {
          this.val = val;
          this.left = left;
          this.right = right;
      }

    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        def(root,list);
        return list;
    }

    private static void def(TreeNode root, List<Integer> list) {
        if(root == null) {
            return;
        }
        //先遍历左子树
        def(root.left,list);
        //再遍历根节点
        list.add(root.val);
        //在遍历右子树
        def(root.right,list);
    }

    public int maxDepth(TreeNode root) {
        if(root == null) {
            return 0;
        }
        int right = maxDepth(root.right);
        int left = maxDepth(root.left);
        return Math.max(right,left) + 1;
    }

    public List<List<Integer>> levelOrder(TreeNode root) {
        //此时需要装的数字应该是[[3],[9,20],[15,7]]这种链表里面套数组的形式
        List<List<Integer>> ret = new ArrayList<>();
        if(root == null) {
            return ret;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        //先把根节点加入
        queue.offer(root);
        while(!queue.isEmpty()) {
            //队列不为空是循环条件
            //当size为空但是队列不为空时,就表示此时已经将根节点的左右子树加入,但是根节点此时已经全部弹出
            List<Integer> list = new ArrayList<>();
            int size = queue.size();
            while(size != 0) {
                //当队列中的元素个数为0的时候表示此时的数字全部出队列
                //弹出的节点需要记录下来
                TreeNode cur = queue.poll();
                //弹出后再加入新的链表中
                list.add(cur.val);
                //此时队列中的元素-1
                size--;
                //层序遍历是有顺序的,必须保证先左子树在右子树
                if(cur.left != null) {
                    queue.offer(cur.left);
                }
                if(cur.right != null) {
                    queue.offer(cur.right);
                }
            }
            ret.add(list);
        }
        return ret;
    }
}


