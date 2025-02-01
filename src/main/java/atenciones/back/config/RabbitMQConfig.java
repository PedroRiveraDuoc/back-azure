package atenciones.back.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.queue.alertas}")
    private String alertasQueueName;

    @Value("${app.rabbitmq.exchange}") // Nueva propiedad
    private String exchangeName;

    // 1. Declarar el exchange
    @Bean
    public DirectExchange alertsExchange() {
        return new DirectExchange(exchangeName, true, false); // Durable, no auto-delete
    }

    // 2. Declarar la cola (ya existente)
    @Bean
    public Queue alertasQueue() {
        return new Queue(alertasQueueName, true, false, false);
    }

    // 3. Crear binding entre exchange y cola
    @Bean
    public Binding bindingAlertas() {
        return BindingBuilder.bind(alertasQueue())
            .to(alertsExchange())
            .with("alertas.routingkey"); // Routing key específica
    }
}