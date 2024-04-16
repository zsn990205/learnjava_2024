package BinarryTree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Binarry_tree {
    static class TreeNode {
        public char val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(char val) {
            this.val = val;
        }
    }

    public TreeNode createTree() {
        TreeNode A = new TreeNode('A');
        TreeNode B = new TreeNode('B');
        TreeNode C = new TreeNode('C');
        TreeNode D = new TreeNode('D');
        TreeNode E = new TreeNode('E');
        TreeNode F = new TreeNode('F');
        TreeNode G = new TreeNode('G');
        TreeNode H = new TreeNode('H');
        A.left = B;
        A.right = C;
        B.left = D;
        B.right = E;
        E.right = H;
        C.left = F;
        C.right = G;
        return A;
    }

    //前序
    public void preOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.print(root.val + " ");
        preOrder(root.left);
        preOrder(root.right);
    }

    //中序
    public void inOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        inOrder(root.left);
        System.out.print(root.val + " ");
        inOrder(root.right);
    }

    //后序
    public void postOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        postOrder(root.left);
        postOrder(root.right);
        System.out.print(root.val + " ");
    }

    public int nodeSize;

    public int nodeSize(TreeNode root) {
        if (root == null) {
            return 0;
        }
        nodeSize++;
        nodeSize(root.left);
        nodeSize(root.right);
        return nodeSize;
    }

    public int getLeafNodeCount(TreeNode root) {
        if (root == null) {
            return 0;
        }
        if (root.left == null && root.right == null) {
            return 1;
        }
        return getLeafNodeCount(root.left) + getLeafNodeCount(root.right);
    }

    //求第k层结点数
    public int getLevelNodeCount(TreeNode root, int k) {
        if (root == null) {
            return 0;
        }
        if (k == 1) {
            return 1;
        }
        return getLevelNodeCount(root.left, k - 1) + getLevelNodeCount(root.right, k - 1);
    }

    //求树高
    public int getHight1(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int rootLeft = getHight1(root.left);
        int rootRight = getHight1(root.right);
        if (rootLeft > rootRight) {
            return rootLeft + 1;
        } else {
            return rootRight + 1;
        }
    }

    //检测值为val的元素是否存在
    public TreeNode find(TreeNode root, char val) {
        if (root == null) {
            return null;
        }
        if (root.val == val) {
            return root;
        }
        TreeNode ret1 = find(root.left, val);
        if (ret1 != null) {
            return ret1;
        }
        TreeNode ret2 = find(root.right, val);
        if (ret2 != null) {
            return ret2;
        }
        return null;
    }

    //相同的树
    public boolean isSameTree(TreeNode p, TreeNode q) {
        if ((p != null && q == null) || (p == null && q != null)) {
            return false;
        }
        if (p == null && q == null) {
            return true;
        }
        //代码走到这的时候两个均不为空
        if (p.val != q.val) {
            return false;
        }
        //走到这里的时候均不为空并且p.val=q.val
        return isSameTree(p.right, q.right) && isSameTree(p.left, q.left);
    }

    public boolean isSubtree(TreeNode root, TreeNode subRoot) {
        if (root == null || subRoot == null) {
            return false;
        }
        if (isSameTree(root, subRoot)) {
            return true;
        }
        if (isSubtree(root.left, subRoot)) {
            return true;
        }
        if (isSubtree(root.right, subRoot)) {
            return true;
        }
        return false;
    }

    //翻转二叉树
    public TreeNode mirrorTree(TreeNode root) {
        if (root == null) {
            return null;
        }
        if (root.right == null && root.left == null) {
            return root;
        }
        //交换左右
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        mirrorTree(root.left);
        mirrorTree(root.right);
        return root;
    }

    //求树高
    public int MaxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int rootLeft = MaxDepth(root.left);
        if (rootLeft < 0) {
            return -1;
        }
        int rootRight = MaxDepth(root.right);
        if (rootLeft >= 0 && rootRight >= 0 && Math.abs(rootLeft - rootRight) <= 1) {
            //返回真实的高度
            return Math.max(rootLeft, rootRight) + 1;
        } else {
            return -1;
        }
    }

    //平衡二叉树(时间复杂度为O(N))
    public boolean isBalanced(TreeNode root) {
        if (root == null) {
            return true;
        }
        return MaxDepth(root) >= 0;
    }

    //平衡二叉树(时间复杂度为O(N^2))
    public boolean isBalanced1(TreeNode root) {
        if (root == null) {
            return true;
        }
        int rootLeft = MaxDepth(root.left);
        int rootRight = MaxDepth(root.right);

        int s = Math.abs(rootLeft - rootRight);
        if (s <= 1 && isBalanced1(root.left) && isBalanced1(root.right)) {
            return true;
        }
        return false;
    }

    //对称二叉树
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        return isChildSymmetric(root.left, root.right);
    }

    private boolean isChildSymmetric(TreeNode leftTree, TreeNode rightTree) {
        //两棵树有一个为空那肯定是不平衡的
        if ((leftTree != null && rightTree == null) || (leftTree == null && rightTree != null)) {
            return false;
        }
        if (leftTree == null && rightTree == null) {
            return true;
        }
        //走到这个位置表示两个树肯定都不为空,那就比较值是否是相同的
        if (leftTree.val == rightTree.val) {
            return isChildSymmetric(leftTree.left, rightTree.right) &&
                    isChildSymmetric(leftTree.right, rightTree.left);
        }
        return false;
    }

    public static int i;

    //二叉树遍历
    public TreeNode createTree(String str) {
        TreeNode root = null;
        if (str.charAt(i) != '#') {
            root = new TreeNode(str.charAt(i));
            i++;
            root.left = createTree(str);
            root.right = createTree(str);
        } else {
            i++;
        }
        return root;
    }

    //层序遍历
    //使用队列
    public void levelOrder2(TreeNode root) {
        if (root == null) {
            return;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            System.out.print(cur.val + " ");
            if (cur.left != null) {
                queue.offer(cur.left);
            }
            if (cur.right != null) {
                queue.offer(cur.right);
            }
        }

    }

    //判断一棵树是不是完全二叉树
    public boolean isCompleteTree(TreeNode root) {
        if (root == null) {
            return true;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode cur = queue.poll();
            if (cur != null) {
                queue.offer(root.left);
                queue.offer(root.right);
            } else {
                break;
            }

        }
        //判断队列中是否有非空的元素
        while (!queue.isEmpty()) {
            TreeNode cur = queue.peek();
            if (cur == null) {
                queue.poll();
            } else {
                return false;
            }
        }
        return true;
    }

//    public List<List<Integer>> levelOrder(TreeNode root) {
//        List<List<Integer>> ret = new ArrayList<>();
//        if(root == null) {
//            return ret;
//        }
//        Queue<TreeNode> queue = new LinkedList<>();
//        queue.offer(root);
//
//        while(!queue.isEmpty()) {
//            int size = queue.size();
//            List<Integer> list = new ArrayList<>();
//            while(size != 0) {
//                TreeNode cur = queue.poll();
//                list.add(cur.val);
//                size--;
//                if(cur.left != null) {
//                    queue.offer(cur.left);
//                }
//                if(cur.right != null) {
//                    queue.offer(cur.right);
//                }
//            }
//           ret.add(list);
//        }
//        return ret;
//    }

    //二叉树的最近公共祖先
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null) {
            return null;
        }
        if(root == p || root == q) {
            return root;
        }
        TreeNode leftTree = lowestCommonAncestor(root.left,p,q);
        TreeNode RightTree = lowestCommonAncestor(root.right,p,q);

        if(leftTree != null && RightTree != null) {
            return root;
        }
        else if(leftTree != null) {
            return leftTree;
        } else {
            return RightTree;
        }
    }

    //从前序与中序中遍历二叉树
//    public int preIndex;
//    public TreeNode buildTree(int[] preorder, int[] inorder) {
//        return buildTreeChild(preorder,inorder,0,inorder.length-1);
//    }
//
//    private TreeNode buildTreeChild(int[] preorder,int[] inorder,int inbegin,int inend) {
//        if(inbegin > inend) {
//            return null;    //此时没有左树/右数
//        }
//        TreeNode root = new TreeNode(preorder[preIndex]);
//        int rootIndex = findIndex(inorder,inbegin,inend,preorder[preIndex]);
//        if(rootIndex == -1) {
//            return null;    //表示没找到下标为index的结点
//        }
//        //找到后就递归下一个结点
//        preIndex++;
//        root.left = buildTreeChild(preorder,inorder,inbegin,rootIndex-1);
//        root.right = buildTreeChild(preorder,inorder,rootIndex+1,inend);
//        return root;
//    }
//
//    private int findIndex(int[] inorder, int inbegin, int inend, int key) {
//        for(int i = inbegin; i <= inend; i++) {
//            if(inorder[i] == key) {
//                return i;
//            }
//        }
//        return -1;
//    }

    private StringBuilder sb = new StringBuilder();
    public String tree2str(TreeNode t) {
        if(t == null) {
            return "";
        }

        TreeHelp(t);
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    private void TreeHelp(TreeNode root) {
        if(root == null) {
            return;
        }
        sb.append("(");
        sb.append(root.val);
        TreeHelp(root.left);
        if(root.left == null && root.right != null) {
            sb.append("()");
        }
        TreeHelp(root.right);
        sb.append(")");
    }
}
