package com.kafka.demo.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.demo.model.Message;
import com.kafka.demo.model.store.GarbageRepository;
import com.kafka.demo.utils.KafkaConstants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static com.kafka.demo.configuration.JsonDeserializer.OBJECT_MAPPER;
import static com.kafka.demo.configuration.JsonDeserializer.TYPE_REFERENCE;

@EnableKafka
@Configuration
public class ConsumerConfiguration {

    private final GarbageRepository garbageRepository;

    public ConsumerConfiguration(GarbageRepository garbageRepository) {
        this.garbageRepository = garbageRepository;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Message> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Message> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigurations(),
                new StringDeserializer(),
                new JsonDeserializer<>(garbageRepository));
    }

    @Bean
    public Map<String, Object> consumerConfigurations() {
        Map<String, Object> configurations = new HashMap<>();
        //устанавливаем адрес сервера, на котором работает Kafka.
        configurations.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.KAFKA_BROKER);
        //        для установки идентификатора группы потребителей Kafka.
        configurations.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaConstants.GROUP_ID);
        configurations.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configurations.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configurations.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configurations.put( OBJECT_MAPPER, new ObjectMapper());
        configurations.put(TYPE_REFERENCE, new TypeReference<Message>() {});
//        configurations.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "обработка на стороне консумера");
//        configurations.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,ждать ?);

        // «earliest», чтобы получить все значения в очереди с самого начала.
        // «latest», чтобы получить только самое последнее значение.
        return configurations;
    }
}

/*
    {"sender": "Console","content": "Hello bro!"}

 */