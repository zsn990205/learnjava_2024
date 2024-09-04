package io;

import java.io.*;

//把写的字节流转成字符流
public class DemoIo12 {
    public static void main(String[] args) {
        try(OutputStream outputStream = new FileOutputStream("D:/test2.txt")) {
            //使用printwriter进行包装转化的
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println("你好世界!");
            //使用flush刷新缓冲区
            //确保内容被写入文件中
            printWriter.flush();
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}
