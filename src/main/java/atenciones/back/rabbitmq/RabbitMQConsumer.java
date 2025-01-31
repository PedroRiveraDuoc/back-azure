package atenciones.back.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "${app.rabbitmq.queue.alertas}")
    public void recibirMensaje(String mensaje) {
        System.out.println("Mensaje recibido desde RabbitMQ: " + mensaje);
    }

    @PostConstruct
    public void init() {
        System.out.println("RabbitMQ Consumer iniciado correctamente...");
    }
}