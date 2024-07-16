package io;

import java.io.File;
import java.io.IOException;

//创建文件
public class DemoIo2 {
    public static void main(String[] args) throws IOException {
        //此时我们创建的这个文件并不存在
        File f = new File("D:/test.txt");
        //此处肯定返回的是false
        System.out.println(f.exists());
        //文件都不存在那么目录的返回也是false
        System.out.println(f.isDirectory());  //判断是否是目录
        //同理返回的是false
        System.out.println(f.isFile());  //判断是否是文件

        //创建文件
        f.createNewFile();
        //创建文件后再看文件是否存在
        System.out.println(f.exists());
        System.out.println(f.isDirectory());
        System.out.println(f.isFile());
    }
}
