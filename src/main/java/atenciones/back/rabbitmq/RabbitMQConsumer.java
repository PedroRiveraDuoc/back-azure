package atenciones.back.rabbitmq;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    // @RabbitListener(queues = "${app.rabbitmq.queue.alertas}")
    // public void recibirMensaje(String mensaje) {
    //     System.out.println("Mensaje recibido desde RabbitMQ: " + mensaje);
    // }
       private final List<String> mensajesRecibidos = new ArrayList<>();

    @RabbitListener(queues = "${app.rabbitmq.queue.alertas}")
    public void recibirMensaje(String mensaje) {
        System.out.println("Mensaje recibido desde RabbitMQ: " + mensaje);
        synchronized (mensajesRecibidos) {
            mensajesRecibidos.add(mensaje);
        }
    }

    public List<String> obtenerMensajes() {
        synchronized (mensajesRecibidos) {
            return new ArrayList<>(mensajesRecibidos); // Devolver una copia para evitar concurrencia
        }
    }

    public void limpiarMensajes() {
        synchronized (mensajesRecibidos) {
            mensajesRecibidos.clear();
        }
    }
}