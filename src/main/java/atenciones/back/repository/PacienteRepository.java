package atenciones.back.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import atenciones.back.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    
} 
