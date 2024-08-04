package io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

//outputStream
public class DemoIo10 {
    public static void main(String[] args) {
        try(OutputStream outputStream = new FileOutputStream("d:/test2.txt")) {
            String s = "你好世界";
            outputStream.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
