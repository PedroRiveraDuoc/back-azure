package atenciones.back.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import atenciones.back.model.Paciente;
import atenciones.back.repository.PacienteRepository;

@Service
public class PacienteService {
    
    private PacienteRepository pacienteRepository;

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

}
