package co.edu.uniquindio.rapponcho.dataTransferObjects;
import java.util.List;

public class RepartidorSimpleDTO {
    private String id;
    private String nombre;
    private String documento;
    private String telefono;
    private String estadoDisponibilidad;
    private List<String> zonasCobertura;
    private int enviosAsignados;

    public RepartidorSimpleDTO(String id, String nombre, String documento, String telefono,
                         String estadoDisponibilidad, List<String> zonasCobertura,
                         int enviosAsignados) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.telefono = telefono;
        this.estadoDisponibilidad = estadoDisponibilidad;
        this.zonasCobertura = zonasCobertura;
        this.enviosAsignados = enviosAsignados;
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDocumento() { return documento; }
    public String getTelefono() { return telefono; }
    public String getEstadoDisponibilidad() { return estadoDisponibilidad; }
    public List<String> getZonasCobertura() { return zonasCobertura; }
    public int getEnviosAsignados() { return enviosAsignados; }
}


