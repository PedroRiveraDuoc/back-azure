package atenciones.back.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import atenciones.back.Service.PacienteService;
import atenciones.back.Service.SenalVitalService;
import atenciones.back.model.Paciente;
import atenciones.back.model.SenalVital;
import atenciones.back.rabbitmq.RabbitMQConsumer;
import atenciones.back.rabbitmq.RabbitMQProducer;

@RestController
@CrossOrigin
public class SenalVitalController {

    @Autowired
    private SenalVitalService senalVitalService;
    @Autowired
    private PacienteService pacienteService;
    @Autowired
    private RabbitMQProducer rabbitMQProducer;
    private final RabbitMQConsumer rabbitMQConsumer;

    public SenalVitalController(RabbitMQConsumer rabbitMQConsumer) {
        this.rabbitMQConsumer = rabbitMQConsumer;
    }

    @GetMapping("/mensaje")
    public List<String> obtenerMensajes() {
        return rabbitMQConsumer.obtenerMensajes();
    }

    @DeleteMapping("/borrarMensaje")
    public String limpiarMensajes() {
        rabbitMQConsumer.limpiarMensajes();
        return "Lista de mensajes limpiada correctamente.";
    }

    @PostMapping("/crear/{id}")
    public ResponseEntity<?> crearSenalVital(@RequestBody SenalVital senalVital, @PathVariable long id) {
        // Buscar paciente
        Paciente paciente = pacienteService.obtenerPacientePorId(id).orElse(null);

        if (paciente == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El paciente con ID " + id + " no existe."));
        }

        // Asignar paciente a la señal vital antes de guardarla
        senalVital.setPaciente(paciente);

        // Guardar señal vital con el paciente asignado
        SenalVital nuevaSenal = senalVitalService.crearSenalVital(senalVital);

        // Verificar si hay anomalías
        if (esAnomalia(nuevaSenal)) {
            String mensaje = generarMensajeAlertaLegible(nuevaSenal);

            // Enviar alerta a RabbitMQ
            rabbitMQProducer.enviarMensajeAlerta(mensaje);

            // Devolver JSON en lugar de texto plano
            Map<String, Object> response = new HashMap<>();
            response.put("alerta", "⚠️ ALERTA MÉDICA ⚠️");
            response.put("paciente",
                    nuevaSenal.getPaciente().getNombre() + " " + nuevaSenal.getPaciente().getApellido());
            response.put("temperatura", nuevaSenal.getTemperatura());
            response.put("pulso", nuevaSenal.getPulso());
            response.put("ritmoRespiratorio", nuevaSenal.getRitmoRespiratorio());
            response.put("estado", nuevaSenal.getPacienteEstado());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        // Devolver JSON de la señal vital si no hay anomalías
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSenal);
    }

    private String generarMensajeAlertaLegible(SenalVital senal) {
        return String.format(
                " ALERTA MÉDICA\n" +
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
                senal.getPacienteEstado());
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
