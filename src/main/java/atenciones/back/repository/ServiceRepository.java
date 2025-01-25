package atenciones.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import atenciones.back.model.Paciente;
import atenciones.back.model.SenalVital;

public interface ServiceRepository extends JpaRepository<SenalVital, Long> {
    // Paciente findByRut(String rut);    
} 
