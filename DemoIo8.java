package io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

//writer
public class DemoIo8 {
    public static void main(String[] args) throws IOException {
        try(Writer writer = new FileWriter("D:/test2.txt",true)) {
                writer.write("我在学习文件io");

        }
    }
}
