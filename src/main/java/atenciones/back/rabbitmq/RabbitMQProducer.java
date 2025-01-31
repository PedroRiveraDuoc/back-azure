package atenciones.back.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class RabbitMQProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.queue.alertas}")
    private String alertasQueueName;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensajeAlerta(String mensaje) {
        System.out.println("Enviando mensaje a RabbitMQ: " + mensaje);
        rabbitTemplate.convertAndSend(alertasQueueName, mensaje);
        System.out.println("Mensaje enviado a RabbitMQ: " + mensaje);
    }
}