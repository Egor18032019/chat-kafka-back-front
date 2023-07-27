package com.kafka.demo.consumer;

import com.kafka.demo.model.Message;
import com.kafka.demo.utils.KafkaConstants;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {
    final SimpMessagingTemplate template;

    public MessageListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    @KafkaListener(
            topics = KafkaConstants.KAFKA_TOPIC,
            groupId = KafkaConstants.GROUP_ID
    )
    public void listen(Message message) {
        System.out.println("Отправка сообщений всем пользователям");
        template.convertAndSend("/topic/group", message);
    }
}