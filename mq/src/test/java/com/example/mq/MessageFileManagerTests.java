package com.example.mq;

import com.example.mq.common.MqException;
import com.example.mq.mqserver.core.MSGQueue;
import com.example.mq.mqserver.core.Message;
import com.example.mq.mqserver.datacenter.MessageFileManager;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
public class MessageFileManagerTests {
    private MessageFileManager messageFileManager = new MessageFileManager();
    private static final String queueName1 = "testQueue1";
    private static final String queueName2 = "testQueue2";

    //用例执行之前的准备工作
    @BeforeEach
    public void setUp() throws IOException {
        //准备阶段 创建出两个队列 以备后用(测试)
        messageFileManager.createQueueFiles(queueName1);
        messageFileManager.createQueueFiles(queueName2);
    }

    //用例执行之后的收尾工作
    @AfterEach
    public void tearDown() throws IOException {
        //收尾阶段 把刚才的队列干掉
        messageFileManager.destroyQueueFiles(queueName1);
        messageFileManager.destroyQueueFiles(queueName2);
    }

    @Test
    public void testCreateFiles() throws IOException {
        //创建队列文件已经在上面setUp执行过了 此处只需要验证是否存在
        File queueDataFile1 = new File("./data/" + queueName1 + "/queue_data.txt");
        //保证既存在 还得是普通目录
        Assertions.assertEquals(true, queueDataFile1.isFile());
        File queueStatFile1 = new File("./data/" + queueName1 + "/queue_stat.txt");
        Assertions.assertEquals(true, queueStatFile1.isFile());

        File queueDataFile2 = new File("./data/" + queueName2 + "/queue_data.txt");
        Assertions.assertEquals(true, queueDataFile2.isFile());
        File queueStatFile2 = new File("./data/" + queueName2 + "/queue_stat.txt");
        Assertions.assertEquals(true, queueStatFile2.isFile());
    }

    @Test
    public void testReadWriteStat() {
        MessageFileManager.Stat stat = new MessageFileManager.Stat();
        stat.totalCount = 100;
        stat.validCount = 50;
        // 此处使用 Spring 帮我们封装好的 反射 的工具类.
        ReflectionTestUtils.invokeMethod(messageFileManager, "writeStat", queueName1, stat);

        // 写入完毕之后, 再调用一下读取, 验证读取的结果和写入的数据是一致的.
        MessageFileManager.Stat newStat = ReflectionTestUtils.invokeMethod(messageFileManager, "readStat", queueName1);
        Assertions.assertEquals(100, newStat.totalCount);
        Assertions.assertEquals(50,newStat.validCount);
        System.out.println("测试完成!");
    }

    @Test
    public void testSendMessage() throws IOException, ClassNotFoundException, MqException {
        //构造出消息和队列
        Message message = createTestMessage("testMessage");
        //此处创建的queue对象的name 不能随便写 只能用queueName1和queueName2
        //需要保证这个队列对象对应的目录和文件啥的都存在才行
        MSGQueue queue = createTestQueue(queueName1);
        //调用发送消息方法
        messageFileManager.sendMessage(queue,message);

        //检查统计文件
        MessageFileManager.Stat stat = ReflectionTestUtils.invokeMethod(messageFileManager,"readStat",queueName1);
        Assertions.assertEquals(1, stat.totalCount);
        Assertions.assertEquals(1,stat.validCount);

        //检查data文件
        LinkedList<Message> messages = messageFileManager.loadAllMessageFromQueue(queueName1);
        Assertions.assertEquals(1,messages.size());
        Message currentMessage = messages.get(0);
        Assertions.assertEquals(message.getMessageId(),currentMessage.getMessageId());
        Assertions.assertEquals(message.getDeliverMode(),currentMessage.getDeliverMode());
        Assertions.assertEquals(message.getRoutingKey(),currentMessage.getRoutingKey());
        //比较两个字节数组的内容是否相同 不能直接用assertEquals
        Assertions.assertArrayEquals(message.getBody(),currentMessage.getBody());

        System.out.println("message: " + currentMessage);
    }

    private MSGQueue createTestQueue(String queueName) {
        MSGQueue queue = new MSGQueue();
        queue.setName(queueName);
        queue.setDurable(true);
        queue.setAutoDelete(false);
        queue.setExclusive(false);
        return queue;
    }

    private Message createTestMessage(String content) {
        Message message = Message.createMessageWithId("testRoutingKey",null,content.getBytes());
        return message;
    }

    @Test
    public void testLoadAllMessageFromQueue() throws IOException, ClassNotFoundException, MqException {
        //往消息队列里插入100条消息 验证100条消息从文件中读取时是否和之前一致
        MSGQueue queue = createTestQueue(queueName1);
        List<Message> exceptedMessages = new LinkedList<>();
        for(int i = 0; i < 100; i++) {
            Message message = createTestMessage("testMessage " + i);
            messageFileManager.sendMessage(queue,message);
            exceptedMessages.add(message);
        }
        //读取所有消息
        LinkedList<Message> actualMessages = messageFileManager.loadAllMessageFromQueue(queueName1);
        Assertions.assertEquals(exceptedMessages.size(),actualMessages.size());
        for(int i = 0; i < exceptedMessages.size(); i++) {
            Message expectedMessage = exceptedMessages.get(i);
            Message actualMessage = actualMessages.get(i);
            System.out.println("[" + i + "] actualMessage=" + actualMessage);
            Assertions.assertEquals(expectedMessage.getMessageId(),actualMessage.getMessageId());
            Assertions.assertEquals(expectedMessage.getRoutingKey(),actualMessage.getRoutingKey());
            Assertions.assertEquals(expectedMessage.getDeliverMode(),actualMessage.getDeliverMode());
            Assertions.assertArrayEquals(expectedMessage.getBody(),actualMessage.getBody());
            //检查消息是否为有效消息
            Assertions.assertEquals(0x1,actualMessage.getIsValid());
        }
    }

    @Test
    public void testDeleteMessage() throws IOException, MqException, ClassNotFoundException {
        // 创建队列, 写入 10 个消息. 删除其中的几个消息. 再把所有消息读取出来, 判定是否符合预期.
        MSGQueue queue = createTestQueue(queueName1);
        List<Message> expectedMessages = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            Message message = createTestMessage("testMessage" + i);
            messageFileManager.sendMessage(queue, message);
            expectedMessages.add(message);
        }

        // 删除其中的三个消息
        messageFileManager.deleteMessage(queue, expectedMessages.get(7));
        messageFileManager.deleteMessage(queue, expectedMessages.get(8));
        messageFileManager.deleteMessage(queue, expectedMessages.get(9));

        // 对比这里的内容是否正确.
        LinkedList<Message> actualMessages = messageFileManager.loadAllMessageFromQueue(queueName1);
        Assertions.assertEquals(7, actualMessages.size());
        for (int i = 0; i < actualMessages.size(); i++) {
            Message expectedMessage = expectedMessages.get(i);
            Message actualMessage = actualMessages.get(i);
            System.out.println("[" + i + "] actualMessage=" + actualMessage);

            Assertions.assertEquals(expectedMessage.getMessageId(), actualMessage.getMessageId());
            Assertions.assertEquals(expectedMessage.getRoutingKey(), actualMessage.getRoutingKey());
            Assertions.assertEquals(expectedMessage.getDeliverMode(), actualMessage.getDeliverMode());
            Assertions.assertArrayEquals(expectedMessage.getBody(), actualMessage.getBody());
            Assertions.assertEquals(0x1, actualMessage.getIsValid());
        }
    }

    @Test
    public void testGC() throws IOException, MqException, ClassNotFoundException {
        // 先往队列中写 100 个消息. 获取到文件大小.
        // 再把 100 个消息中的一半, 都给删除掉(比如把下标为偶数的消息都删除)
        // 再手动调用 gc 方法, 检测得到的新的文件的大小是否比之前缩小了.
        MSGQueue queue = createTestQueue(queueName1);
        List<Message> expectedMessages = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            Message message = createTestMessage("testMessage" + i);
            messageFileManager.sendMessage(queue, message);
            expectedMessages.add(message);
        }

        // 获取 gc 前的文件大小
        File beforeGCFile = new File("./data/" + queueName1 + "/queue_data.txt");
        long beforeGCLength = beforeGCFile.length();

        // 删除偶数下标的消息
        for (int i = 0; i < 100; i += 2) {
            messageFileManager.deleteMessage(queue, expectedMessages.get(i));
        }

        // 手动调用 gc
        messageFileManager.gc(queue);

        // 重新读取文件, 验证新的文件的内容是不是和之前的内容匹配
        LinkedList<Message> actualMessages = messageFileManager.loadAllMessageFromQueue(queueName1);
        Assertions.assertEquals(50, actualMessages.size());
        for (int i = 0; i < actualMessages.size(); i++) {
            // 把之前消息偶数下标的删了, 剩下的就是奇数下标的元素了.
            // actual 中的 0 对应 expected 的 1
            // actual 中的 1 对应 expected 的 3
            // actual 中的 2 对应 expected 的 5
            // actual 中的 i 对应 expected 的 2 * i + 1
            Message expectedMessage = expectedMessages.get(2 * i + 1);
            Message actualMessage = actualMessages.get(i);

            Assertions.assertEquals(expectedMessage.getMessageId(), actualMessage.getMessageId());
            Assertions.assertEquals(expectedMessage.getRoutingKey(), actualMessage.getRoutingKey());
            Assertions.assertEquals(expectedMessage.getDeliverMode(), actualMessage.getDeliverMode());
            Assertions.assertArrayEquals(expectedMessage.getBody(), actualMessage.getBody());
            Assertions.assertEquals(0x1, actualMessage.getIsValid());
        }
        // 获取新的文件的大小
        File afterGCFile = new File("./data/" + queueName1 + "/queue_data.txt");
        long afterGCLength = afterGCFile.length();
        System.out.println("before: " + beforeGCLength);
        System.out.println("after: " + afterGCLength);
        Assertions.assertTrue(beforeGCLength > afterGCLength);
    }
}
