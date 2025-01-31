package atenciones.back.Service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductorService {
    private final RabbitTemplate rabbitTemplate;
    private final String alertasQueueName;

    public ProductorService(RabbitTemplate rabbitTemplate,
            @Value("${app.rabbitmq.queue.alertas}") String alertasQueueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.alertasQueueName = alertasQueueName;
    }

    public void enviarMensaje(String mensaje) {
        rabbitTemplate.convertAndSend(alertasQueueName, mensaje);
    }
}