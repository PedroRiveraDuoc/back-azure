package atenciones.back.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import atenciones.back.model.Paciente;
import atenciones.back.model.SenalVital;
import atenciones.back.repository.PacienteRepository;

@Service
public class PacienteService {
    
    private PacienteRepository pacienteRepository;

    @Autowired
    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente crearPaciente(Paciente paciente) {
        return pacienteRepository.save(paciente);
    }
    public List<Paciente> obtenerPacientes() {
        return pacienteRepository.findAll();
    }
    public Optional<Paciente> obtenerPacientePorId(Long id) {
        return pacienteRepository.findById(id);
    }
    public void eliminarPaciente(Long id) {
        pacienteRepository.deleteById(id);
    }
        
    

    //  public List<SenalVital> obtenerSenalesVitalesPorRut(String rut) {
    //     Paciente paciente = pacienteRepository.findByRut(rut);
    //     if (paciente != null) {
    //         return paciente.getSenalesVitales();
    //     }
    //     return null; 
    // }

}
