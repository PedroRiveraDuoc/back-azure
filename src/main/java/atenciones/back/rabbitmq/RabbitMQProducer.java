package atenciones.back.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class RabbitMQProducer {
    private final RabbitTemplate rabbitTemplate;

    // 1. Nueva propiedad para el exchange (necesaria para el envío)
    @Value("${app.rabbitmq.exchange}")
    private String exchangeName; 

    @Value("${app.rabbitmq.queue.alertas}")
    private String queueName; 

    // 2. Nueva propiedad para la routing key (necesaria para el binding)
    @Value("${app.rabbitmq.routingkey}")
    private String routingKey; // [AGREGADO] Parámetro esencial

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void enviarMensajeAlerta(String mensaje) {
        // 3. Logs más informativos con detalles técnicos
        System.out.println("Iniciando envío de mensaje - Exchange: " + exchangeName
                + " | Routing Key: " + routingKey
                + " | Mensaje: " + mensaje);

        // 4. Envío correcto con los 3 parámetros requeridos
        rabbitTemplate.convertAndSend(
                exchangeName, // Nombre del exchange
                routingKey, // Routing key (NO usar queueName aquí)
                mensaje // Cuerpo del mensaje
        );

        // 5. Mensaje de confirmación con formato JSON
        System.out.println(String.format(
            "Envío exitoso a RabbitMQ:\n" +
            "{\n" +
            "    \"exchange\": \"%s\",\n" +
            "    \"routingKey\": \"%s\",\n" +
            "    \"messageLength\": %d\n" +
            "}",
            exchangeName,
            routingKey,
            mensaje.length()
        ));
    }
}