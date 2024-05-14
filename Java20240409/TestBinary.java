package Java20240409;

import java.util.Objects;

public class TestBinary {
    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();
        int[] arr = {5,12,3,2,11,15};
        for(int i = 0; i < arr.length; i++) {
            tree.insert(arr[i]);
        }
        tree.remove(12);
        System.out.println("=====");
    }
}
