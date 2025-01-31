package atenciones.back.Service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductorService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void enviarMensaje(String mensaje) {
        rabbitTemplate.convertAndSend("alertasQueue", mensaje);
    }
}
