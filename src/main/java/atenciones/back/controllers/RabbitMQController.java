package atenciones.back.controllers;

import atenciones.back.rabbitmq.RabbitMQProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rabbitmq")
public class RabbitMQController {

    private final RabbitMQProducer rabbitMQProducer;

    public RabbitMQController(RabbitMQProducer rabbitMQProducer) {
        this.rabbitMQProducer = rabbitMQProducer;
    }

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarMensaje(@RequestBody String mensaje) {
        rabbitMQProducer.enviarMensajeAlerta(mensaje);
        return ResponseEntity.ok("Mensaje enviado a RabbitMQ: " + mensaje);
    }
}
