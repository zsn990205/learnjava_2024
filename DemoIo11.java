package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

//使用字节流转字符流的方法
public class DemoIo11 {
    public static void main(String[] args) {
        try(InputStream inputStream = new FileInputStream("D:/test2.txt")) {
            //此时scanner是从文件中读取了
            Scanner scanner = new Scanner(inputStream);
            String s = scanner.next();
            System.out.println(s);
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}
