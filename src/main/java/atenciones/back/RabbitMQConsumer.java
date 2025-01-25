package atenciones.back;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.impl.AMQImpl.Basic.Deliver;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

public class RabbitMQConsumer {
    private final static String QUEUE_NAME = "myQueue";

    public static void main(String[] argv) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            factory.setUsername("admin");
            factory.setPassword("admin");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            boolean durable = true;

            channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            };

            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
