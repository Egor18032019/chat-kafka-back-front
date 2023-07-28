package com.kafka.demo.controller;

import com.kafka.demo.model.Message;
import com.kafka.demo.model.store.GarbageRepository;
import com.kafka.demo.utils.KafkaConstants;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class ChatController {
    //TODO вынести все адреса в утилиты
    private final KafkaTemplate<String, Message> kafkaTemplate;
    private final GarbageRepository garbageRepository;

    public ChatController(KafkaTemplate<String, Message> kafkaTemplate, GarbageRepository garbageRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.garbageRepository = garbageRepository;
    }

    @PostMapping(value = "/api/send", consumes = "application/json", produces = "application/json")
    public void sendMesscage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now().toString());
        CompletableFuture<SendResult<String, Message>> future = kafkaTemplate.send(KafkaConstants.KAFKA_TOPIC, message.getSender(), message);
        try {
            future.get();
            //TODO в этом случаи нам нужны ошибки или ошибки вместе с message  в бд складывать ?
        } catch (InterruptedException e) {
            //если текущий поток был прерван
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            garbageRepository.add(message);
            //если future завершено в исключительных случаях
            throw new RuntimeException(e);
        } catch (CancellationException e) {
            //если future было отменено
            garbageRepository.add(message);
            throw new RuntimeException(e);
        }

        //        kafkaTemplate.flush();
        //TODO отдавать ответ в виде ResponseBody

    }

    //    -------------- WebSocket API ----------------
    @MessageMapping("/sendMessage")
    @SendTo("/topic/group")
    public Message broadcastGroupMessage(@Payload Message message) {
        //Sending this message to all the subscribers
        return message;
    }

    @MessageMapping("/newUser")
    @SendTo("/topic/group")
    public Message addUser(@Payload Message message,
                           SimpMessageHeaderAccessor headerAccessor) {
        // Add user in web socket session
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", message.getSender());
        return message;
    }

}


