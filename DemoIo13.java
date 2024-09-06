package io;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

//写一个代码实现小程序
public class DemoIo13 {
    public static void main(String[] args) {
        //1.先让用户指定一个要扫描的目录
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您要扫描的目录: ");
        String path = scanner.next();
        //判断一下用户输入的目录是否是合法目录
        File rootPath = new File(path);
        if(!rootPath.isDirectory()) {
            System.out.println("您输入的目录非法!请检查后重新输入");
            return;
        }
        //2.再让用户指定一个要查询的关键词
        System.out.println("请输入要查询的关键词: ");
        String word = scanner.next();
        //3.可以进行递归扫描了
        scanDir(rootPath,word);
    }

    private static void scanDir(File rootPath, String word) {
        //1.先列出rootPath中所有的文件和目录
        File[] files = rootPath.listFiles();
        //System.out.println(Arrays.toString(files));
        if(files == null) {
            return;
        }
        //2.针对数组中的文件做出相应的处理
        for(File f : files) {
            //加个打印日志
            //记录当前扫描到哪个位置
            System.out.println("当前扫描到文件: " + f.getAbsoluteFile());
            if(f.isFile()) {
                //是普通文件,检查文件是否需要删除
                checkDelete(f,word);
            } else {
                //是目录
                //继续递归的搜索
                scanDir(f, word);
            }
        }
    }

    private static void checkDelete(File f, String word) {
        if(!f.getName().contains(word)) {
            //表示当前的文件不需要删除
            return;
        }
        System.out.println("当前的文件为" + f.getAbsolutePath()+"请确认是否要删除(Y/N)");
        Scanner scanner = new Scanner(System.in);
        String s = scanner.next();
        if(s.equals("Y") || s.equals("y")) {
            //表示当前的文件需要删除
            f.delete();
            System.out.println("删除完毕!");
        } else {
            //表示当前的文件不需要删除
            System.out.println("取消删除");
        }
     }
}
