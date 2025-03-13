package com.example.mq.common;

import java.io.*;

//不仅仅应用于message 其他的Java对象也可以使用
//想让对象进行序列化和反序列化 使用serializable接口
public class BinaryTool {
    //把对象序列化成一个字节数组
    public static byte[] toBytes(Object object) throws IOException {
        //相当于一个变长的字节数组
        //可以把object序列化的数据逐渐写到byteArrayOutputStream中 在统一转成byte[]
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                //此处的writeObject会把对象进行序列化 生成的二进制字节数据就会写入到objectOutputStream中
                //由于objectOutputStream又是关联到byteArrayOutputStream,最终就写入到ByteArrayOutputStream
                objectOutputStream.writeObject(object);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    //把字节数组反序列化成一个对象
    public static Object fromBytes(byte[] data) throws ClassNotFoundException, IOException {
//        Object object = null;
//        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data)) {
//            try(ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
//                //此处的readObject是从data这个byte[]中读取数据并进行反序列化
//                object = objectInputStream.readObject();
//            }
//        }
//        return object;
//    }

        Object object = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
                // 此处的 readObject, 就是从 data 这个 byte[] 中读取数据并进行反序列化.
                object = objectInputStream.readObject();
            }
        }
        return object;
    }
}

