package atenciones.back.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import atenciones.back.Service.PacienteService;
import atenciones.back.Service.SenalVitalService;
import atenciones.back.model.Paciente;
import atenciones.back.model.SenalVital;
import atenciones.back.rabbitmq.RabbitMQProducer;
import atenciones.back.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;

@RestController
@CrossOrigin
public class SenalVitalController {

    @Autowired
    private SenalVitalService senalVitalService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @PostMapping("/crear/{id}")
    public ResponseEntity<SenalVital> crearSenalVital(@RequestBody SenalVital senalVital, @PathVariable long id) {
        Paciente paciente = pacienteService.obtenerPacientePorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con ID: " + id));

        SenalVital nuevaSenal = senalVitalService.crearSenalVital(senalVital);
        // Asignar el paciente a la señal vital
        senalVital.setPaciente(paciente);
        if (esAnomalia(nuevaSenal)) {
            // 1️⃣ Generar mensaje de alerta
            String mensaje = generarMensajeAlertaLegible(nuevaSenal);

            // 2️⃣ Enviar alerta a RabbitMQ
            rabbitMQProducer.enviarMensajeAlerta(mensaje);

            // 3️⃣ Guardar alerta en archivo JSON
            guardarAlertaEnArchivo(nuevaSenal);
        }

        // Guardar la señal vital

        return new ResponseEntity<>(nuevaSenal, HttpStatus.CREATED);
    }
      private String generarMensajeAlertaLegible(SenalVital senal) {
        return String.format(
                "⚠️ ALERTA MÉDICA ⚠️\n" +
                "Paciente: %s %s\n" +
                "ID: %d\n" +
                "Fecha: %s\n" +
                "Temperatura: %.1f°C\n" +
                "Pulso: %d lpm\n" +
                "Ritmo Resp.: %d rpm\n" +
                "Estado: %s\n",
                senal.getPaciente().getNombre(),
                senal.getPaciente().getApellido(),
                senal.getPaciente().getId(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                senal.getTemperatura(),
                senal.getPulso(),
                senal.getRitmoRespiratorio(),
                senal.getPacienteEstado()
        );
    }

    private void guardarAlertaEnArchivo(SenalVital senal) {
        // 📌 Implementar la lógica para guardar en JSON
        senalVitalService.guardarAlertaEnArchivo(senal);
    }
    private boolean esAnomalia(SenalVital senal) {
        // 📌 Implementar la lógica de detección de anomalías
        return senal.getTemperatura() < 36.0 || senal.getTemperatura() > 38.5 ||
               senal.getPulso() < 50 || senal.getPulso() > 120 ||
               senal.getRitmoRespiratorio() < 12 || senal.getRitmoRespiratorio() > 30;
    }

    @GetMapping("/todos")
    public ResponseEntity<List<SenalVital>> obtenerTodasSenalesVitales() {
        List<SenalVital> senalesVitales = senalVitalService.obtenerSenalesVitales();
        return new ResponseEntity<>(senalesVitales, HttpStatus.OK);
    }

    @GetMapping("/mostrarSenalVital/{id}")
    public ResponseEntity<SenalVital> mostrarSenalVitalPorId(@PathVariable Long id) {
        SenalVital senalVital = senalVitalService.obtenerSenalVitalPorId(id);
        if (senalVital != null) {
            return new ResponseEntity<>(senalVital, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/eliminarSenalVital/{id}")
    public ResponseEntity<Void> eliminarSenalVital(@PathVariable Long id) {
        senalVitalService.eliminarSenalVital(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
