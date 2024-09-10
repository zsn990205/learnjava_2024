package io;

import java.io.File;
import java.util.Scanner;

public class DemoIo14 {
    public static void main(String[] args) {
        //1.首先让用户输入查找的文件目录
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入您要扫描的目录:");
        String path = scanner.next();
        File rootPath = new File(path);
        //判断输入的路径是否合法
        if(!rootPath.isDirectory()) {
            System.out.println("您输入的路径不是合法目录,请重新输入");
            return;
        }
        //2.请用户输入指定的文件名称
        System.out.println("请输入您要查找的文件名称");
        String word = scanner.next();
        //3.针对文件进行递归查找
        scanDirPath(rootPath,word);
    }

    private static void scanDirPath(File rootPath, String word) {
        File[] files = rootPath.listFiles();
        if(files == null) {
            return;
        }
        for(File f : files) {
            System.out.println("当前的文件目录是:"+f.getAbsolutePath());
            if(f.isFile()) {
                //如果是普通文件的话就判断是否需要删除
                checkDelete(f,word);
            } else {
                //不是普通文件,是目录文件,进行递归查找
                scanDirPath(f, word);
            }
        }
    }

    private static void checkDelete(File f, String word) {
        if(!f.getName().contains(word)) {
            return;
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请判断当前的文件您是否需要删除(Y/N)");
            String s = scanner.next();
            if(s.equals("Y") || s.equals("y")) {
                f.delete();
                System.out.println("删除完毕");
            } else {
                //表示当前的文件不需要删除
                System.out.println("取消删除");
            }
        }
    }
}
