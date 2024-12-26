package com.atguigu.kafka.test;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdminTopicTest {
    public static void main(String[] args) {

        Map<String,Object> confMap = new HashMap<>();
        confMap.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        //1.创建管理员对象
        Admin admin = Admin.create(confMap);

        String topicName = "test1";
        int partitionCount = 1;
        short replicationCount = 1;
        NewTopic topic1 = new NewTopic(topicName,partitionCount,replicationCount);

        String topicName1 = "test2";
        int partitionCount1 = 2;
        short replicationCount1 = 2;
        NewTopic topic2 = new NewTopic(topicName1,partitionCount1,replicationCount1);

        //2.创建主题
        CreateTopicsResult result = admin.createTopics(
                Arrays.asList());

        //3.关闭管理者对象
        admin.close();
    }
}
