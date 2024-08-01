package io;

import java.io.File;

//修改源目录的文件名
public class DemoIo6 {
    public static void main(String[] args) {
        //src是源
        //dest是目标
        File srcF = new File("D:/test.txt");
        File destF = new File("D:/test2.txt");
        boolean ret = srcF.renameTo(destF);
        System.out.println("ret = " + ret);
    }
}
