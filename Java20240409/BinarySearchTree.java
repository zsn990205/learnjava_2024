package Java20240409;

public class BinarySearchTree {
    static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }

    }
        public TreeNode root;

        //查找结点key
        public boolean search(int key) {
            TreeNode cur = root;
            while(cur != null) {
                if(cur.val < key) {
                    cur = cur.right;
                } else if(cur.val > key) {
                    cur = cur.left;
                } else {
                    return true;
                }
            }
            return false;
    }

    //二叉搜索树不允许插入相同的数字(如果有直接返回false)
        public boolean insert(int val) {
            if(root == null) {
                root = new TreeNode(val);
                return true;
            }
            TreeNode cur = root;
            TreeNode parent = null;
            while(cur != null) {
                if(cur.val < val) {
                    parent = cur;
                    cur = cur.right;
                } else if(cur.val > val) {
                    parent = cur;
                    cur = cur.left;
                } else {
                    return false;
                }
            }
            TreeNode node = new TreeNode(val);
            if(parent.val > val) {
               //插入到左边
                parent.left = node;
            } else {
                //插入到右边
                parent.right = node;
            }
            return true;
    }

    //删除节点
        public void remove(int key) {
            TreeNode cur = root;
            TreeNode parent = null;
            while(cur != null) {
                if(cur.val < key) {
                    //当前数据在右边
                    parent = cur;
                    cur = cur.right;
                } else if(cur.val > key) {
                    //当前的数据在左边
                    parent = cur;
                    cur = cur.left;
                } else {
                    //开始删除数据
                    removeNode(cur,parent);
                }
            }
    }

    private void removeNode(TreeNode cur, TreeNode parent) {
        if(cur.left == null) {
            if(cur == root) {
                root = cur.right;
            } else if(cur == parent.left) {
                parent.left = cur.right;
            } else {
                parent.right = cur.right;
            }
        } else if(cur.right == null) {
            if(cur == root) {
                root = cur.left;
            } else if(cur == parent.left) {
                parent.left = cur.left;
            } else {
                parent.right = cur.left;
            }
        } else {
            TreeNode targetParent = cur;
            TreeNode target = cur.right;
            while(target.left != null) {
                targetParent = target;
                target = target.left;
            }
            //当代码走到这个位置的时候表示target的左为空了,此时就找到了准备删除
            //交换要删除的元素和找到的目标元素
            cur.val = target.val;
            if(targetParent.left == cur) {
                targetParent.left = target.right;
            } else {
                targetParent.right = target.right;
            }
         }
    }
}
