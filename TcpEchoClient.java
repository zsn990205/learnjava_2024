package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class TcpEchoClient {
    private Socket socket = null;
    public TcpEchoClient(String serverIp,int serverPort) throws IOException {
        //需要在创建Socket的时候和服务器建立连接,此时就得告诉Socket服务器在哪
        //具体建立连接的代码是内核自动完成的
        //new对象的时候操作系统已经开始三次握手了
        socket = new Socket(serverIp,serverPort);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {
         PrintWriter writer = new PrintWriter(outputStream);
         Scanner scannerNetWork = new Scanner(inputStream);
        while (true) {
            System.out.println("->");
            //1.从控制台读取用户输入的内容
            String request = scanner.next();
            //2.把字符串作为请求发给服务器
            writer.println(request);
            writer.flush();
            //3.从服务器读取响应
            String response = scannerNetWork.next();
            //4.把响应显示到界面上
            System.out.println(response);
        }
    } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        TcpEchoClient tcpEchoClient = new TcpEchoClient("127.0.0.1",9090);
        tcpEchoClient.start();
    }
}
