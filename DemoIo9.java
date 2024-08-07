package io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

//inputStream进行读取
public class DemoIo9 {
    public static void main(String[] args) {
        try(InputStream inputStream = new FileInputStream("D:/test2.txt")) {
            byte[] buffer = new byte[1024];
            int n = inputStream.read(buffer);
            for(int i = 0; i < n; i++) {
                //使用16进制的形式进行打印
                System.out.printf("%x\n" , buffer[i]);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
}
