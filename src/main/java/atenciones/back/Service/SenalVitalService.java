package atenciones.back.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import atenciones.back.model.Paciente;
import atenciones.back.model.SenalVital;
import atenciones.back.repository.ServiceRepository;
@Service
public class SenalVitalService {
    
    private ServiceRepository serviceRepository;

    public SenalVitalService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }
    public SenalVital crearSenalVital(SenalVital senalVital) {
        return serviceRepository.save(senalVital);
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


}
