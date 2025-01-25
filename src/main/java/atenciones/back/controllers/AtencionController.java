package atenciones.back.controllers;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import atenciones.back.Service.PacienteService;
import atenciones.back.model.Paciente;
import atenciones.back.model.SenalVital;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.sql.DataSource;
import java.sql.Connection;

import java.sql.Statement;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@CrossOrigin
public class AtencionController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/mensaje")
    public String listaUsuarios() {
        System.out.println("backend llamando");
        return "{\"mensaje\":\"Integracion backend\"}";
    }

    @PostMapping("/crearPaciente")
    public ResponseEntity<Paciente> crearPaciente(@RequestBody Paciente paciente) {
        Paciente pacientes = pacienteService.crearPaciente(paciente);

        return new ResponseEntity<>(pacientes, HttpStatus.CREATED);

    }

    @GetMapping("/mostrarPaciente")
    public List<Paciente> mostrarPaciente() {
        return pacienteService.obtenerPacientes();
    }

    @GetMapping("/mostrarPaciente/{id}")
    public Optional<Paciente> mostrarPacientePorId(@PathVariable Long id) {
        return pacienteService.obtenerPacientePorId(id);
    }
     @DeleteMapping("/eliminarPaciente/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        pacienteService.eliminarPaciente(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
