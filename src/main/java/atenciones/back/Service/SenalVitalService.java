package atenciones.back.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import atenciones.back.rabbitmq.RabbitMQProducer;
import atenciones.back.model.SenalVital;
import atenciones.back.model.Paciente;
import atenciones.back.repository.ServiceRepository;

@Service
public class SenalVitalService {

    private final ServiceRepository serviceRepository;
    private final RabbitMQProducer rabbitMQProducer;

    @Value("${app.rabbitmq.exchange}") // Propiedad para el exchange
    private String exchange;
    
    @Value("${app.rabbitmq.queue.alertas}") // Propiedad para la cola
    private String queueName;

    public SenalVitalService(ServiceRepository serviceRepository, RabbitMQProducer rabbitMQProducer) {
        this.serviceRepository = serviceRepository;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    public SenalVital crearSenalVital(SenalVital senalVital) {
        SenalVital nuevaSenal = serviceRepository.save(senalVital);

        if (esAnomalia(nuevaSenal)) {
            String mensaje = generarMensajeAlertaLegible(nuevaSenal);
            rabbitMQProducer.enviarMensajeAlerta(mensaje);
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

    private boolean esAnomalia(SenalVital senal) {
        return senal.getTemperatura() < 36.0 || senal.getTemperatura() > 38.5 ||
                senal.getPulso() < 50 || senal.getPulso() > 120 ||
                senal.getRitmoRespiratorio() < 12 || senal.getRitmoRespiratorio() > 30;
    }

    private String generarMensajeAlertaLegible(SenalVital senal) {
        Paciente paciente = senal.getPaciente();

        return String.format("""
            ================================
            ===== ALERTA MÉDICA =====
            Paciente:    %s %s
            ID:          %d
            Fecha:       %s
            ---------------------------------
            PARÁMETROS ANORMALES:
            %s
            ---------------------------------
            VALORES REGISTRADOS:
            Temperatura:    %.1f°C [Rango normal: 36.0 - 38.5]
            Pulso:          %d lpm [Rango normal: 50 - 120]
            Ritmo Resp.:    %d rpm [Rango normal: 12 - 30]
            Estado:         %s
            ================================
            """,
            paciente.getNombre(),
            paciente.getApellido(),
            paciente.getId(),
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            obtenerParametrosAnomalos(senal),
            senal.getTemperatura(),
            senal.getPulso(),
            senal.getRitmoRespiratorio(),
            senal.getPacienteEstado()
        );
    }

    private String obtenerParametrosAnomalos(SenalVital senal) {
        List<String> anomalias = new ArrayList<>();
        
        if (senal.getTemperatura() < 36.0) anomalias.add("- Temperatura BAJA (Hipotermia)");
        if (senal.getTemperatura() > 38.5) anomalias.add("- Temperatura ALTA (Hipertermia)");
        if (senal.getPulso() < 50) anomalias.add("- Pulso BAJO (Bradicardia)");
        if (senal.getPulso() > 120) anomalias.add("- Pulso ALTO (Taquicardia)");
        if (senal.getRitmoRespiratorio() < 12) anomalias.add("- Respiración LENTA (Bradipnea)");
        if (senal.getRitmoRespiratorio() > 30) anomalias.add("- Respiración RÁPIDA (Taquipnea)");
        
        return !anomalias.isEmpty() ? String.join("\n", anomalias) : "No se detectaron anomalías";
    }

    // Métodos opcionales (dejados en comentarios por si los quieres reutilizar)
    /*
    private LocalDateTime generarTimestamp() {
        return LocalDateTime.now();
    }
    
    private String formatoMensajePersonalizado(SenalVital senal) {
        return String.format("[%s] Alerta en paciente %d: %s", 
            generarTimestamp(), 
            senal.getPaciente().getId(), 
            senal.getPacienteEstado());
    }
    */
}
