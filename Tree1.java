package BinarryTree;

public class Tree1 {
    public static void main(String[] args) {
        Binarry_tree binarry_tree = new Binarry_tree();
        Binarry_tree.TreeNode root = binarry_tree.createTree();
        binarry_tree.preOrder(root);
        System.out.println();
        binarry_tree.inOrder(root);
        System.out.println();
        binarry_tree.postOrder(root);
        System.out.println();
        binarry_tree.levelOrder2(root);
        System.out.println();
        System.out.println("叶子结点个数: "+binarry_tree.getLeafNodeCount(root));
        System.out.println("第层结点个数: "+binarry_tree.getLevelNodeCount(root,2));
        System.out.println("树的高度: "+binarry_tree.getHight1(root));
        System.out.println("树的高度: "+binarry_tree.find(root,'A'));

    }
}
