package co.edu.uniquindio.rapponcho.model.Factory;

import co.edu.uniquindio.rapponcho.model.Usuario;
import co.edu.uniquindio.rapponcho.model.Persona;
import co.edu.uniquindio.rapponcho.utils.GeneradorID;

public class UsuarioFactory extends PersonaFactory {
    @Override
    public Persona crearPersona(String nombre, String telefono,
                                String correo, String usuario,
                                String contrasena) {
        String id = GeneradorID.generarIDUsuario();
        return new Usuario(id, nombre, telefono, correo, usuario, contrasena);
    }
}
