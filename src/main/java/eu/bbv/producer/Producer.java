package eu.bbv.producer;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Random;

@SpringBootApplication
public class Producer {

    public static void main(String[] args) {
        SpringApplication.run(Producer.class, args);
    }

    @Bean
    public ApplicationRunner runner(KafkaTemplate<String, String> template) {
        String[] cities = new String[]{"Berlin", "München", "Zurich"};
        Random generator = new Random();
        return args -> {
            while (true) {
                for (String city : cities) {
                    final Integer nextInt = generator.nextInt(10);
                    template.send("stream", city, nextInt.toString());
                    Thread.sleep(1000);
                }
            }
        };
    }
}
