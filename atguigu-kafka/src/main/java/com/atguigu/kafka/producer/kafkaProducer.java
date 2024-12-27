package com.atguigu.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.HashMap;
import java.util.Map;


public class kafkaProducer {
        public static void main(String[] args) {
        Map<String,Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        //1.创建生产者对象
        KafkaProducer<String, String> producer = new KafkaProducer<>(configMap);
        //2.创建数据
        for(int i = 0; i < 10; i++) {
            ProducerRecord<String,String> record = new ProducerRecord<String,String>("test",
                                "key1" + i,"value1" + i);
        //3.通过生产者对象将数据发送到kafka
            producer.send(record);
                }

        //4.关闭生产者对象
        producer.close();
    }
}
