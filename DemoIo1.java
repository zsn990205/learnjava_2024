package io;

import java.io.File;
import java.io.IOException;

//文件的一些属性方法
public class DemoIo1 {
    public static void main(String[] args) throws IOException {
        File f = new File("./test.txt");
        System.out.println(f.getParent());
        System.out.println(f.getPath());
        System.out.println(f.getName());
        System.out.println(f.getAbsoluteFile());
        System.out.println(f.getCanonicalPath());
    }
}
