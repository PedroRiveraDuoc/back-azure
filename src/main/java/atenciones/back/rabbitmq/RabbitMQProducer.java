package atenciones.back.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.queue.alertas}")
    private String queueName;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensajeAlerta(String mensaje) {
        rabbitTemplate.convertAndSend(queueName, mensaje);
        System.out.println("✅ [PRODUCER] Mensaje enviado a la cola '" + queueName + "': " + mensaje);
    }
}
