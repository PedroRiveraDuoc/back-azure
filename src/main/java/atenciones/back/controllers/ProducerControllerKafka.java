package atenciones.back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import atenciones.back.kafka.KafkaProducerServive;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class ProducerControllerKafka {
    @Autowired
    private KafkaProducerServive kafkaProducerServive;

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        kafkaProducerServive.sendMessage(message);
        return "Message sent to the Kafka Topic test-topic" + message;
    }
    
}
