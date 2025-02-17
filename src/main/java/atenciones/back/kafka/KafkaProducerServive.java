package atenciones.back.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServive {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "senales_vitales_topic";

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
    }

}
