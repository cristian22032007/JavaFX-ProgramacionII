package co.edu.uniquindio.fx10.proyectofinals2.model;

public abstract class Persona {
    private String id;
    private String nombre;
    private String telefono;
    private String correo;
    private String usuario;
    private String contrasena;

    public Persona(String id, String nombre, String telefono, String correo, String usuario, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }
}
