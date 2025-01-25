package atenciones.back.controllers;

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
import atenciones.back.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;

@RestController
@CrossOrigin
public class SenalVitalController {

    @Autowired
    private SenalVitalService senalVitalService;
    @Autowired
    private PacienteService pacienteService;

    @PostMapping("/crear/{id}")
    public ResponseEntity<SenalVital> crearSenalVital(@RequestBody SenalVital senalVital, @PathVariable long id) {
        Paciente paciente = pacienteService.obtenerPacientePorId(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con ID: " + id));

        // Asignar el paciente a la señal vital
        senalVital.setPaciente(paciente);

        // Guardar la señal vital
        SenalVital nuevaSenal = senalVitalService.crearSenalVital(senalVital);

        return new ResponseEntity<>(nuevaSenal, HttpStatus.CREATED);
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
