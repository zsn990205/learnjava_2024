package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UdpEchoServer {
//服务器设备
//创建一个DatagramSocket对象,后续操作网卡的基础
    private DatagramSocket socket = null;
    public UdpEchoServer(int port) throws SocketException {
        //指定端口号
        socket = new DatagramSocket(port);
        //系统自己调用端口号
        //socket = new DatagramSocket();
    }

    public void start() throws IOException {
        //通过这个方法启动服务器
        System.out.println("服务器启动!");
        while(true) {
            //服务器是长时间运行的,客户端很多所以使用while(true)
            //1.读取请求并解析
            DatagramPacket requestPacket = new DatagramPacket(new byte[4096],4096);
            socket.receive(requestPacket);
            //当前完成receive的时候就是数据以二进制的形式存在DatagramPacket中了
            //要想把数据显示出来,需要把这个转成字符串的形式
            String request = new String(requestPacket.getData(),0,
                    requestPacket.getLength());
            //2.根据请求计算响应
            //回显服务器 请求是啥响应就是啥
            String response = process(request);
            //3.响应写回客户端
            //搞一个响应对象 datagrampacket
            //往datagrampacket构造刚刚的数据 再通过send返回
            DatagramPacket responsePacket = new DatagramPacket(response.getBytes(),
                    response.getBytes().length, requestPacket.getSocketAddress());
            socket.send(responsePacket);
            //4.打印一个日志,打印出数据交互的详情
            System.out.printf("[%s:%d] req=%s,resp=%s\n",requestPacket.getAddress().toString(),
                    requestPacket.getPort(),request,response);
        }
    }

    public String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        UdpEchoServer server = new UdpEchoServer(9090);
        server.start();

    }
}
