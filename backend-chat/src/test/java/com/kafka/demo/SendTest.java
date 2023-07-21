package com.kafka.demo;

import org.apache.kafka.clients.admin.NewTopic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.kafka.demo.utils.KafkaConstants.KAFKA_TOPIC;

public class SendTest {
    @BeforeAll
    public static void init() throws ExecutionException, InterruptedException, TimeoutException {
        KafkaBase.start(List.of(new NewTopic( KAFKA_TOPIC, 1, (short) 1)));
    }
    @Test
    void isAppWork() {



    }

    @Test
    void handlerTest() {



    }
}
