package atenciones.back.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@PropertySource("classpath:application.properties")
public class RabbitMQConfig {

    @Value("${app.rabbitmq.queue.alertas}")
    private String alertasQueueName;

    @Bean
    public Queue alertasQueue() {
        return new Queue(alertasQueueName, true);
    }
}