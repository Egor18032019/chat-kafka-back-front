package com.kafka.demo.utils;

public class KafkaConstants {
    public static final String KAFKA_TOPIC = "kafka-chat";
    public static final String GROUP_ID = "kafka-sandbox";

//    public static final String KAFKA_BROKER = "kafka-1:9092";//через общий докер
//    public static final String KAFKA_BROKER = "localhost:9092";//в ручную
    public static final String KAFKA_BROKER = "172.19.0.2:9092";//через местный докер
}
