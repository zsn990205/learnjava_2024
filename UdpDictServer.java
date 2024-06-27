package network;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class UdpDictServer extends UdpEchoServer{
    private Map<String,String> dict = new HashMap<>();
    public UdpDictServer(int port) throws SocketException {
        super(port);
        //此处可以向表中插入很多英文单词
        dict.put("cat","小猫");
        dict.put("dog","小狗");
        dict.put("pig","小猪");
    }

    //重写process方法,在process方法中完成翻译的操作
    //翻译的本质就是查表
    @Override
    public String process(String request) {
        return dict.getOrDefault(request,"该词在词典中不存在");
    }

    public static void main(String[] args) throws IOException {
        UdpDictServer udpDictServer = new UdpDictServer(9090);
        udpDictServer.start();
    }
}
