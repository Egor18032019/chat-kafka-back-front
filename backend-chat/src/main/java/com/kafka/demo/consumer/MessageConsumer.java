package com.kafka.demo.consumer;

import com.kafka.demo.utils.KafkaConstants;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    final SimpMessagingTemplate template;

    public MessageConsumer(SimpMessagingTemplate template) {
        this.template = template;
    }

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listen(ConsumerRecord<?,?> message) {
        System.out.println("Отправка сообщений всем пользователям");
        System.out.println(message.toString());
        if (message.value() != null) {
            template.convertAndSend("/topic/group", message.value());
        }
    }
}
//            message.setTimestamp(LocalDateTime.now().toString());
