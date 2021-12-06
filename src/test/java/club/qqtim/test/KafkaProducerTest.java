package club.qqtim.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaProducerTest {




    public static void main(String[] args){

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");
        // 2. 根据参数创建KafkaProducer实例（生产者）
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        // 3. 创建ProducerRecord实例（消息）
        ProducerRecord<String, String> record = new ProducerRecord<>("topic-demo", "hello kafka");
        // 4. 发送消息
        producer.send(record);
        // 5. 关闭生产者示例
        producer.close();

    }

}
