package co.edu.uniquindio.rapponcho.model;

public class Administrador extends Persona {
    public Administrador(String id, String nombre, String telefono, String correo, String usuario, String contrasena) {
        super(id, nombre, telefono, correo, usuario, contrasena);
    }
    // Sin atributos adicionales, solo hereda de Persona
}