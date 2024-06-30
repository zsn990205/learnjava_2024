package network;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UdpEchoClient {
    private DatagramSocket socket = null;
    private String sererIp = "";
    private int serverPort = 0;

    public UdpEchoClient(String ip,int port) throws SocketException {
        //创建这个对象端口号不能手定制定,分配
        socket = new DatagramSocket();
        //由于udp不会持有对端的信息就需要在应用程序里把对端的情况记录下来
        //主要记录的是ip和端口号
        sererIp = ip;
        serverPort = port;
    }

    public void start() throws IOException {
        System.out.println("客户端启动!");
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("->");
            //1.从控制台读取数据作为请求
            String request = scanner.next();
            //2.把请求内容构造成datagrampacket
            DatagramPacket requestPacket = new DatagramPacket(request.getBytes(),
                    request.getBytes().length, InetAddress.getByName(sererIp),serverPort);
            socket.send(requestPacket);
            //3.尝试读取服务器返回的响应
            DatagramPacket responsePacket = new DatagramPacket(new byte[4096],4096);
            socket.receive(responsePacket);
            //4.把响应转换成字符串显示出来
            String response = new String(responsePacket.getData(),0,
                    responsePacket.getLength());
            System.out.println(response);
        }
    }

    public static void main(String[] args) throws IOException {
        UdpEchoClient client = new UdpEchoClient("127.0.0.1",9090);
        client.start();
    }
}
