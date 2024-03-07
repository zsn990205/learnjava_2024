import java.util.Arrays;

//二维数组学习
public class java0302 {
    public static void main(String[] args) {
        //不规则的二维数组
        int[][] arr = {{1,2,3},{4,5}};
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }
    }

    public static void main2(String[] args) {
        int[][] arr = {{1,2,3},{4,5,6}};
        for(int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println(Arrays.deepToString(arr));

        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 3; j++) {
                System.out.print(arr[i][j]+" ");
            }
            System.out.println();
        }
    }

    public static void main1(String[] args) {
        //不可或缺的是行,列可空
        int[][] arr = {{1,2,3},{4,5,6}};
        int[][] arr2 = new int[2][3];
        int[][] arr3 = new int[][]{{1,2,3},{4,5,6}};

    }
}
