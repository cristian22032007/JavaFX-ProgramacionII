package co.edu.uniquindio.rapponcho.model;


import java.util.ArrayList;
import java.util.List;

public class Usuario extends Persona {
    private List<Direccion> direcciones;
    private List<MetodoPago> metodosPago;
    private List<Envio> envios;

    public Usuario(String id, String nombre, String telefono, String correo, String usuario, String contrasena) {
        super(id, nombre, telefono, correo, usuario, contrasena);
        this.direcciones = new ArrayList<>();
        this.metodosPago = new ArrayList<>();
        this.envios = new ArrayList<>();
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public List<MetodoPago> getMetodosPago() {
        return metodosPago;
    }

    public List<Envio> getEnvios() {
        return envios;
    }


}
