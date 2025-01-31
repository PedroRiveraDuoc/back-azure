package atenciones.back.Service;

import java.util.List;
import org.springframework.stereotype.Service;
import atenciones.back.rabbitmq.RabbitMQProducer;
import atenciones.back.model.SenalVital;
import atenciones.back.repository.ServiceRepository;

@Service
public class SenalVitalService {

    private final ServiceRepository serviceRepository;
    private final RabbitMQProducer rabbitMQProducer; // Nuevo productor de RabbitMQ

    public SenalVitalService(ServiceRepository serviceRepository, RabbitMQProducer rabbitMQProducer) {
        this.serviceRepository = serviceRepository;
        this.rabbitMQProducer = rabbitMQProducer; // Inyección del productor
    }

    public SenalVital crearSenalVital(SenalVital senalVital) {
        SenalVital nuevaSenal = serviceRepository.save(senalVital);

        // Lógica para detectar anomalías
        if (esAnomalia(nuevaSenal)) {
            String mensaje = "⚠️ ALERTA: Señal fuera de rango -> " + nuevaSenal.toString();
            rabbitMQProducer.enviarMensajeAlerta(mensaje); // Enviar alerta a RabbitMQ
        }

        return nuevaSenal;
    }

    public List<SenalVital> obtenerSenalesVitales() {
        return serviceRepository.findAll();
    }

    public SenalVital obtenerSenalVitalPorId(Long id) {
        return serviceRepository.findById(id).orElse(null);
    }

    public void eliminarSenalVital(Long id) {
        serviceRepository.deleteById(id);
    }

    // Método para detectar si una señal vital es anómala
    private boolean esAnomalia(SenalVital senal) {
        return senal.getTemperatura() < 36.0 || senal.getTemperatura() > 38.5 ||
                senal.getPulso() < 50 || senal.getPulso() > 120 ||
                senal.getRitmoRespiratorio() < 10 || senal.getRitmoRespiratorio() > 25;
    }
}
