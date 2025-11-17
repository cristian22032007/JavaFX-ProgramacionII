package co.edu.uniquindio.rapponcho.dataTransferObjects;
import java.util.ArrayList;
import java.util.List;

public class UsuarioSimpleDTO {
        private String id;
        private String nombre;
        private String telefono;
        private String correo;
        private String usuario;
        private List<DireccionSimpleDTO> direcciones;
        private int cantidadEnvios;

        public UsuarioSimpleDTO(String id, String nombre, String telefono, String correo,
                          String usuario, List<DireccionSimpleDTO> direcciones, int cantidadEnvios) {
            this.id = id;
            this.nombre = nombre;
            this.telefono = telefono;
            this.correo = correo;
            this.usuario = usuario;
            this.direcciones = direcciones != null ? direcciones : new ArrayList<>();
            this.cantidadEnvios = cantidadEnvios;
        }
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getUsuario() { return usuario; }
    public List<DireccionSimpleDTO> getDirecciones() { return direcciones; }
    public int getCantidadEnvios() { return cantidadEnvios; }
}

