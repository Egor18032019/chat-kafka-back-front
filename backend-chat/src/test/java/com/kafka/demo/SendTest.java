package com.kafka.demo;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonSerializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.kafka.demo.utils.KafkaConstants.KAFKA_TOPIC;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.CLIENT_ID_CONFIG;

public class SendTest {
    private static final Logger log = LoggerFactory.getLogger(SendTest.class);//TODO сделать логи
    private final Duration timeout = Duration.ofMillis(2_000);

    @BeforeAll
    public static void init() throws ExecutionException, InterruptedException, TimeoutException {
        KafkaBase.start(List.of(new NewTopic(KAFKA_TOPIC, 1, (short) 1)));
    }

    @Test
    void isKafkaWork() {
        // Cоздаем продюсора он отправляет в кафку и потом консьюмеров ловим

    }

    @Test
    void isFrontWork() {
// Селениум и проверка подгрузился ли фронт ?
        // или GET http://localhost:8080/ и получили ответ 200 ??

    }

    @Test
    void isBackWork() {
        //  отправили одно сообщение
        putValuesToKafka();
        var myConsumer = new MyConsumer(KafkaBase.getBootstrapServers());
        ConsumerRecords<Long, StringValue> records = myConsumer.getConsumer().poll(timeout);
        for (ConsumerRecord<Long, StringValue> kafkaRecord : records) {
            try {
                var key = kafkaRecord.key();
                var value = kafkaRecord.value();
                System.out.println("value " + value + " " + key);
            } catch (Exception ex) {
                System.out.println("херня какая-то " + records.toString());
            }
        }
    }

    private void putValuesToKafka() {
        Properties props = new Properties();
        props.put(CLIENT_ID_CONFIG, "myKafkaTestProducer");
        props.put(BOOTSTRAP_SERVERS_CONFIG, KafkaBase.getBootstrapServers());
        props.put(ACKS_CONFIG, "0");
        props.put(LINGER_MS_CONFIG, 1);
        props.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        var kafkaProducer = new KafkaProducer<Long, Object>(props);
        kafkaProducer.send(new ProducerRecord<>(KAFKA_TOPIC, "{\n" +
                "  \"sender\": \"IDEA\",\n" +
                "  \"content\": \"Hello !\"\n" +
                "}"));

    }
}
