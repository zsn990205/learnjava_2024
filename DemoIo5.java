package io;

import java.io.File;

//创建目录
public class DemoIo5 {
    public static void main(String[] args) {
        File f = new File("d:/java");
        //创建一个目录
        boolean ret = f.mkdir();
        //创建多级目录
        //d:/java109/aaa/bbb/ccc
        boolean ret2 = f.mkdirs();
        System.out.println("ret = " + ret);
    }
}
