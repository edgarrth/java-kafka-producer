package com.example.kafkaproducerpoc.controller;

import com.example.kafkaproducerpoc.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
@RestController
public class ConsumerController {

    // Autowiring Kafka Template
    @Autowired KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "user.topic";
    // Publish messages using the GetMapping
    @PostMapping("/users")
    public String publishMessage(@RequestBody User message)
    {
        // Sending the message
        kafkaTemplate.send(TOPIC, message.getId(), message.toJson());
        kafkaTemplate.flush();

        return "Se publico satisfactoriamente el usuario:" + message.getId();
    }

}
