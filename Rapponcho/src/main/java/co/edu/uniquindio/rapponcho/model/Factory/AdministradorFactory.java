package co.edu.uniquindio.rapponcho.model.Factory;


import co.edu.uniquindio.rapponcho.model.Administrador;
import co.edu.uniquindio.rapponcho.model.Persona;
import co.edu.uniquindio.rapponcho.utils.GeneradorID;

public class AdministradorFactory extends PersonaFactory {
    @Override
    public Persona crearPersona(String nombre, String telefono,
                                String correo, String usuario,
                                String contrasena) {
        String id = GeneradorID.generarIDUsuario(); // Pueden compartir formato de ID
        return new Administrador(id, nombre, telefono, correo, usuario, contrasena);
    }
}