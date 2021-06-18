package eu.bbv.stream;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;

import java.time.Duration;

@EnableKafkaStreams
@SpringBootApplication
public class Stream {
    public static void main(String[] args) {
        SpringApplication.run(Stream.class, args);
    }

    @Bean
    public KStream<String, String> kStream(StreamsBuilder kStreamBuilder) {
        KStream<String, String> stream = kStreamBuilder.stream("stream", Consumed.with(Serdes.String(), Serdes.String()));
        stream
                .mapValues((ValueMapper<String, Integer>) Integer::parseInt)
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Integer()))
                .windowedBy(TimeWindows.of(Duration.ofMillis(10000)))
                .reduce(Integer::sum, Named.as("windowSum"))
                .toStream()
                .map((windowedId, value) -> new KeyValue<>(windowedId.key(), value))
                .to("trx", Produced.with(Serdes.String(), Serdes.Integer()));

        stream.print(Printed.toSysOut());

        return stream;
    }
}
