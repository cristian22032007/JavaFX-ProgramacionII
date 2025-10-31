package co.edu.uniquindio.fx10.proyectofinals2.model;

import java.util.ArrayList;
import java.util.List;

public class Repartidor extends Persona {
    private String documento;
    private EstadoDisponibilidad estadoDisponibilidad;
    private List<ZonaCobertura> zonaCobertura;
    private List<Envio> enviosAsignados;

    public Repartidor(String id, String nombre, String telefono, String correo,
                      String usuario, String contrasena, String documento) {
        super(id, nombre, telefono, correo, usuario, contrasena);
        this.documento = documento;
        this.estadoDisponibilidad = EstadoDisponibilidad.ACTIVO;
        this.zonaCobertura = new ArrayList<>();
        this.enviosAsignados = new ArrayList<>();
    }

    public String getDocumento() {
        return documento;
    }

    public EstadoDisponibilidad getEstadoDisponibilidad() {
        return estadoDisponibilidad;
    }

    public List<ZonaCobertura> getZonaCobertura() {
        return zonaCobertura;
    }

    public List<Envio> getEnviosAsignados() {
        return enviosAsignados;
    }
}
