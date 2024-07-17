package io;

import java.io.File;

//删除文件
public class DemoIo3 {
    public static void main(String[] args) throws InterruptedException {
        File f = new File("D:/test.txt");
        //boolean ret = f.delete();
        //System.out.println("ret = "+ ret);

        //下述方法无返回值,和delete方法不同的是它是等进程结束的时候在删除
        f.deleteOnExit();
        Thread.sleep(5000);
        System.out.println("进程结束!");

    }
}
