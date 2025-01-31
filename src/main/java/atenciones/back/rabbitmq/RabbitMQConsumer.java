package atenciones.back.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "${app.rabbitmq.queue.alertas}") 
    public void recibirMensaje(String mensaje) {
        System.out.println("📩 [CONSUMER] Mensaje recibido de la cola: " + mensaje);
    }
}
