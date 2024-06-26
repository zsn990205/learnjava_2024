package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TcpEchoServer {
    private ServerSocket serverSocket = null;

    public TcpEchoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("服务器启动!");
        while(true) {
            //通过accept方法把内核中已经建立好的连接拿到应用程序中
            //建立连接的细节是内核自动完成的应用程序只需要"捡现成"的
            Socket clientSocket = serverSocket.accept();
            //通过这个方法处理当前的连接
            //此处不应该直接调用processConnection,会导致服务器无法创建多个线程
            //使用多线程的方式调用
            Thread t = new Thread(() -> {
                try {
                    processConnection(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();

        }
    }

    public void processConnection(Socket clientSocket) throws IOException {
        //进入这个方法先打印日志,表示此时客户端连上了
        //Adress是ip地址
        System.out.printf("[%s:%d] 客户端上线!\n",clientSocket.getInetAddress(),
                clientSocket.getPort());
        //进行数据交互
        try(InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream()) {
            //读操作-->输入
            //写操作-->输出
            //使用try catch包裹是为了防止忘记关闭文件资源
            //由于客户端发来的数据可能有很多所以使用循环
            while(true) {
                Scanner scanner = new Scanner(inputStream);
                if(!scanner.hasNext()) {
                    System.out.printf("[%s:%d] 客户端下线!\n",
                            clientSocket.getInetAddress(),clientSocket.getPort());
                    break;
                }
                //如果有就读取请求
                //1.读取请求并解析,以next为读取请求的方式,next遇到空白符就读取结束
                String request = scanner.next();
                //2.读取请求计算响应
                String response = process(request);
                //3.把响应写回客户端
                //使用printWriter包裹一下outputStream
                PrintWriter printWriter = new PrintWriter(outputStream);
                //这里的打印是写入到clientSocket中
                printWriter.println(response);
                //显示刷新缓冲区
                printWriter.flush();
                //4.打印此次交互的内容
                System.out.printf("[%s:%d] req=%s,resp=%s\n",clientSocket.getInetAddress(),
                        clientSocket.getPort(),request,response);
            }

        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            clientSocket.close();
        }
    }

    public String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        TcpEchoServer tcpEchoServer = new TcpEchoServer(9090);
        tcpEchoServer.start();
    }
}
