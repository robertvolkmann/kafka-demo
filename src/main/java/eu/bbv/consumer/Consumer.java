package eu.bbv.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class Consumer {

    public static void main(String[] args) {
        SpringApplication.run(Consumer.class, args);
    }

    @KafkaListener(id = "robert",
            topics = "trx")
//            topicPartitions =
//                    {
//                            @TopicPartition(topic = "trx", partitionOffsets = {
//                                    @PartitionOffset(partition = "0-5", initialOffset = "0")
//                            })
//                    },
//            concurrency = "2")
    public void listen(ConsumerRecord<String, Integer> in) {
        System.out.println(in.timestamp());
        System.out.println(in.timestampType().name);
        System.out.println(in.key());
        System.out.println(in.value());
    }
}
