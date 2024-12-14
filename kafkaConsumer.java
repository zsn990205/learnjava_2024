package com.atguigu.kafka.comsumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class kafkaConsumer {
    public static void main(String[] args) {
        Map<String,Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG,"atguigu");
        //1.创建消费者对象
        KafkaConsumer<String,String> consumer = new KafkaConsumer<String, String>(consumerConfig);
        consumer.subscribe(Collections.singletonList("test"));
        //2.从生产者中获取数据
        while(true) {
            final ConsumerRecords<String,String> datas = consumer.poll(100);
            for(ConsumerRecord<String,String> data : datas) {
                System.out.println(data);
            }
        }
        //3.关闭消费者资源
        //consumer.close();
    }
}
