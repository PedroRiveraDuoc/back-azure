package atenciones.back.model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Puedes usar otra estrategia según tu base de datos
    @Column(name = "id")
    private Long id;
    @Column(name = "paciente_rut", unique = true, nullable = false)
    @NotNull(message = "El RUT del paciente no puede ser nulo")
    @Pattern(regexp = "^[0-9]+-[0-9kK]$", message = "El formato del RUT es inválido")
    private String paciente_rut;

    @Column(name = "nombre")
    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @Column(name = "apellido")
    @NotNull(message = "El apellido no puede ser nulo")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    private String apellido;
    
    @Column(name = "fecha_nacimiento")
    @NotNull(message = "La fecha de nacimiento no puede ser nula")
    private Date fechaNacimiento;

    @Column(name = "sexo")
    @NotNull(message = "El sexo no puede ser nulo")
    @Pattern(regexp = "^(Masculino|Femenino|Otro)$", message = "El sexo debe ser Masculino, Femenino o Otro")
    private String sexo;
    
    @OneToMany(mappedBy = "paciente")
    private List<SenalVital> senalesVitales;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public List<SenalVital> getSenalesVitales() {
        return senalesVitales;
    }

    public void setSenalesVitales(List<SenalVital> senalesVitales) {
        this.senalesVitales = senalesVitales;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaciente_rut() {
        return paciente_rut;
    }

    public void setPaciente_rut(String paciente_rut) {
        this.paciente_rut = paciente_rut;
    }
}