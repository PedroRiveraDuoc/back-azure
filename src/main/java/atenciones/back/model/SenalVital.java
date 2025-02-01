package atenciones.back.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class SenalVital {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Long id_servicio;

    @Column(name = "temperatura")
    @NotNull(message = "La temperatura no puede ser nula")
    @DecimalMin(value = "35.0", message = "La temperatura debe ser mayor o igual a 35.0")
    @DecimalMax(value = "42.0", message = "La temperatura debe ser menor o igual a 42.0")
    private Double temperatura;

    @Column(name = "pulso")
    @NotNull(message = "El pulso no puede ser nulo")
    @Min(value = 40, message = "El pulso debe ser al menos 40")
    @Max(value = 180, message = "El pulso no puede ser mayor a 180")
    private Integer pulso;

    @Column(name = "ritmo_respiratorio")
    @NotNull(message = "El ritmo respiratorio no puede ser nulo")
    @Min(value = 12, message = "El ritmo respiratorio debe ser al menos 12")
    @Max(value = 30, message = "El ritmo respiratorio no puede ser mayor a 30")
    private Integer ritmoRespiratorio;

    @Column(name = "presion_arterial")
    @NotNull(message = "La presión arterial no puede ser nula")
    @Pattern(regexp = "^[0-9]{2,3}/[0-9]{2,3}$", message = "El formato de la presión arterial debe ser válido (ej: 120/80)")
    private String presionArterial;

    @Column(name = "comentario")
    @Size(max = 255, message = "El comentario no puede exceder los 255 caracteres")
    private String comentario;

    @Column(name = "paciente_estado")
    @NotNull(message = "El estado del paciente no puede ser nulo")
    @Pattern(regexp = "^(Estable|Crítico|En observación)$", message = "El estado debe ser Estable, Crítico o En observación")
    private String pacienteEstado;

    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    @JsonIgnore
    private Paciente paciente;

    @Override
    public String toString() {
        return String.format(
                "SenalVital[id=%d, temp=%.1f, pulso=%d, ritmo=%d, estado=%s]",
                id_servicio, temperatura, pulso, ritmoRespiratorio, pacienteEstado);
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Integer getPulso() {
        return pulso;
    }

    public void setPulso(Integer pulso) {
        this.pulso = pulso;
    }

    public Integer getRitmoRespiratorio() {
        return ritmoRespiratorio;
    }

    public void setRitmoRespiratorio(Integer ritmoRespiratorio) {
        this.ritmoRespiratorio = ritmoRespiratorio;
    }

    public String getPresionArterial() {
        return presionArterial;
    }

    public void setPresionArterial(String presionArterial) {
        this.presionArterial = presionArterial;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Paciente getPaciente() {
        return paciente;
    }
    
    public String getPacienteEstado() {
        return pacienteEstado;
    }

    public void setPacienteEstado(String pacienteEstado) {
        this.pacienteEstado = pacienteEstado;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Long getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(Long id_servicio) {
        this.id_servicio = id_servicio;
    }
}