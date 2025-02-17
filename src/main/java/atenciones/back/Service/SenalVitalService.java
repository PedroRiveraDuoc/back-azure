package atenciones.back.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;

import atenciones.back.model.SenalVital;
import atenciones.back.model.Paciente;
import atenciones.back.repository.ServiceRepository;

@Service
public class SenalVitalService {

    private final ServiceRepository serviceRepository;    


    public SenalVitalService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
        
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

    // Métodos privados para el manejo de alertas médicas 
    private boolean esAnomalia(SenalVital senal) {
        return senal.getTemperatura() < 36.0 || senal.getTemperatura() > 38.5 ||
                senal.getPulso() < 50 || senal.getPulso() > 120 ||
                senal.getRitmoRespiratorio() < 12 || senal.getRitmoRespiratorio() > 30;
    }

    private String generarMensajeAlertaLegible(SenalVital senal) {
        Paciente paciente = senal.getPaciente();

        return String.format(
                "================================\n" +
                        "===== ALERTA MÉDICA =====\n" +
                        "Paciente:    %s %s\n" +
                        "ID:          %d\n" +
                        "Fecha:       %s\n" +
                        "---------------------------------\n" +
                        "PARÁMETROS ANORMALES:\n" +
                        "%s\n" +
                        "---------------------------------\n" +
                        "VALORES REGISTRADOS:\n" +
                        "Temperatura:    %.1f°C [Rango normal: 36.0 - 38.5]\n" +
                        "Pulso:          %d lpm [Rango normal: 50 - 120]\n" +
                        "Ritmo Resp.:    %d rpm [Rango normal: 12 - 30]\n" +
                        "Estado:         %s\n" +
                        "================================",
                paciente.getNombre(),
                paciente.getApellido(),
                paciente.getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                obtenerParametrosAnomalos(senal),
                senal.getTemperatura(),
                senal.getPulso(),
                senal.getRitmoRespiratorio(),
                senal.getPacienteEstado());
    }

    private String obtenerParametrosAnomalos(SenalVital senal) {
        List<String> anomalias = new ArrayList<>();

        if (senal.getTemperatura() < 36.0)
            anomalias.add("- Temperatura BAJA (Hipotermia)");
        if (senal.getTemperatura() > 38.5)
            anomalias.add("- Temperatura ALTA (Hipertermia)");
        if (senal.getPulso() < 50)
            anomalias.add("- Pulso BAJO (Bradicardia)");
        if (senal.getPulso() > 120)
            anomalias.add("- Pulso ALTO (Taquicardia)");
        if (senal.getRitmoRespiratorio() < 12)
            anomalias.add("- Respiración LENTA (Bradipnea)");
        if (senal.getRitmoRespiratorio() > 30)
            anomalias.add("- Respiración RÁPIDA (Taquipnea)");

        return !anomalias.isEmpty() ? String.join("\n", anomalias) : "No se detectaron anomalías";
    }

    
}
