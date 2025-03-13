package com.example.mq;

import com.example.mq.common.Consumer;
import com.example.mq.mqserver.VirtualHost;
import com.example.mq.mqserver.core.BasicProperties;
import com.example.mq.mqserver.core.ExchangeType;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
public class VirtualHostsTests {
    private VirtualHost virtualHost = null;

    @BeforeEach
    public void setUp() {
        //因为要进行相关的数据库操作 所以得把相应的服务启动起来
        MqApplication.context = SpringApplication.run(MqApplication.class);
        virtualHost = new VirtualHost("default");
    }

    @AfterEach
    public void tearDown() throws IOException {
        MqApplication.context.close();
        //把硬盘的目录删除
        File dataDir = new File("./data");
        //一鼓作气 一起删除掉
        FileUtils.deleteDirectory(dataDir);
        virtualHost = null;
    }

    @Test
    public void testExchangeDeclare() {
        boolean ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.Direct,
                true, false, null);
        Assertions.assertTrue(ok);
    }

    @Test
    public void testExchangeDelete() {
        boolean ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.Direct,
                true, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.exchangeDelete("testExchange");
        Assertions.assertTrue(ok);
    }

    @Test
    public void testQueueDeclare() {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);
    }

    @Test
    public void testQueueDelete() {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.queueDelete("testQueue");
        Assertions.assertTrue(ok);
    }

    @Test
    public void testQueueBind() {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.Direct,
                true, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.queueBind("testQueue", "testExchange", "testBindingKey");
        Assertions.assertTrue(ok);
    }

    @Test
    public void testQueueUnbind() {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.Direct,
                true, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.queueBind("testQueue", "testExchange", "testBindingKey");
        Assertions.assertTrue(ok);

        ok = virtualHost.queueUnBinding("testQueue", "testExchange");
        Assertions.assertTrue(ok);
    }

    @Test
    public void testBasicPublish() {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.Direct,
                true, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.basicPublish("testExchange", "testQueue", null,
                "hello".getBytes());
        Assertions.assertTrue(ok);
    }

    // 先订阅队列, 后发送消息
    @Test
    public void testBasicConsume1() throws InterruptedException {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);
        ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.Direct,
                true, false, null);
        Assertions.assertTrue(ok);

        // 先订阅队列
        ok = virtualHost.basicConsume("testConsumerTag", "testQueue", true, new Consumer() {
                @Override
                public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) {
                    // 消费者自身设定的回调方法.
                    System.out.println("messageId=" + basicProperties.getMessageId());
                    System.out.println("body=" + new String(body, 0, body.length));

                    Assertions.assertEquals("testQueue", basicProperties.getRoutingKey());
                    Assertions.assertEquals(1, basicProperties.getDeliverMode());
                    Assertions.assertArrayEquals("hello".getBytes(), body);
                }

        });
        Assertions.assertTrue(ok);
        Thread.sleep(500);

        // 再发送消息
        ok = virtualHost.basicPublish("testExchange", "testQueue", null,
                "hello".getBytes());
        Assertions.assertTrue(ok);
    }

    // 先发送消息, 后订阅队列.
    @Test
    public void testBasicConsume2() throws InterruptedException {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);
        ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.Direct,
                true, false, null);
        Assertions.assertTrue(ok);

        // 先发送消息
        ok = virtualHost.basicPublish("testExchange", "testQueue", null,
                "hello".getBytes());
        Assertions.assertTrue(ok);

        // 再订阅队列
        ok = virtualHost.basicConsume("testConsumerTag", "testQueue", true, new Consumer() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) {
                // 消费者自身设定的回调方法.
                System.out.println("messageId=" + basicProperties.getMessageId());
                System.out.println("body=" + new String(body, 0, body.length));

                Assertions.assertEquals("testQueue", basicProperties.getRoutingKey());
                Assertions.assertEquals(1, basicProperties.getDeliverMode());
                Assertions.assertArrayEquals("hello".getBytes(), body);
            }
        });
        Assertions.assertTrue(ok);

        Thread.sleep(500);
    }

    @Test
    public void testBasicConsumeFanout() throws InterruptedException {
        boolean ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.FANOUT, false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.queueDeclare("testQueue1", false, false, false, null);
        Assertions.assertTrue(ok);
        ok = virtualHost.queueBind("testQueue1", "testExchange", "");
        Assertions.assertTrue(ok);

        ok = virtualHost.queueDeclare("testQueue2", false, false, false, null);
        Assertions.assertTrue(ok);
        ok = virtualHost.queueBind("testQueue2", "testExchange", "");
        Assertions.assertTrue(ok);

        // 往交换机中发布一个消息
        ok = virtualHost.basicPublish("testExchange", "", null, "hello".getBytes());
        Assertions.assertTrue(ok);

        Thread.sleep(500);

        // 两个消费者订阅上述的两个队列.
        ok = virtualHost.basicConsume("testConsumer1", "testQueue1", true, new Consumer() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) {
                System.out.println("consumerTag=" + consumerTag);
                System.out.println("messageId=" + basicProperties.getMessageId());
                Assertions.assertArrayEquals("hello".getBytes(), body);
            }
        });
        Assertions.assertTrue(ok);

        ok = virtualHost.basicConsume("testConsumer2", "testQueue2", true, new Consumer() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) {
                System.out.println("consumerTag=" + consumerTag);
                System.out.println("messageId=" + basicProperties.getMessageId());
                Assertions.assertArrayEquals("hello".getBytes(), body);
            }
        });
        Assertions.assertTrue(ok);

        Thread.sleep(500);
    }

    @Test
    public void testBasicConsumeTopic() throws InterruptedException {
        boolean ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.TOPIC, false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.queueDeclare("testQueue", false, false, false, null);
        Assertions.assertTrue(ok);

        ok = virtualHost.queueBind("testQueue", "testExchange", "aaa.*.bbb");
        Assertions.assertTrue(ok);

        ok = virtualHost.basicPublish("testExchange", "aaa.ccc.bbb", null, "hello".getBytes());
        Assertions.assertTrue(ok);

        ok = virtualHost.basicConsume("testConsumer", "testQueue", true, new Consumer() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) {
                System.out.println("consumerTag=" + consumerTag);
                System.out.println("messageId=" + basicProperties.getMessageId());
                Assertions.assertArrayEquals("hello".getBytes(), body);
            }
        });
        Assertions.assertTrue(ok);

        Thread.sleep(500);
    }

    @Test
    public void testBasicAck() throws InterruptedException {
        boolean ok = virtualHost.queueDeclare("testQueue", true,
                false, false, null);
        Assertions.assertTrue(ok);
        ok = virtualHost.exchangeDeclare("testExchange", ExchangeType.Direct,
                true, false, null);
        Assertions.assertTrue(ok);

        // 先发送消息
        ok = virtualHost.basicPublish("testExchange", "testQueue", null,
                "hello".getBytes());
        Assertions.assertTrue(ok);

        // 再订阅队列 [要改的地方, 把 autoAck 改成 false]
        ok = virtualHost.basicConsume("testConsumerTag", "testQueue", false, new Consumer() {
            @Override
            public void handleDelivery(String consumerTag, BasicProperties basicProperties, byte[] body) {
                // 消费者自身设定的回调方法.
                System.out.println("messageId=" + basicProperties.getMessageId());
                System.out.println("body=" + new String(body, 0, body.length));

                Assertions.assertEquals("testQueue", basicProperties.getRoutingKey());
                Assertions.assertEquals(1, basicProperties.getDeliverMode());
                Assertions.assertArrayEquals("hello".getBytes(), body);

                // [要改的地方, 新增手动调用 basicAck]
                boolean ok = virtualHost.basicAck("testQueue", basicProperties.getMessageId());
                Assertions.assertTrue(ok);
            }
        });
        Assertions.assertTrue(ok);

        Thread.sleep(500);
    }
}
