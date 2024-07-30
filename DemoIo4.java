package io;

import java.io.File;
import java.util.Arrays;

//显示文件列表
public class DemoIo4 {
    public static void main(String[] args) {
        File f = new File("D:");
        //D盘下的一些文件列表
        String[] files = f.list();
        //如果直接打印files的话其实是它的hashcode值并不是地址
        System.out.println(Arrays.toString(files));
    }
}
