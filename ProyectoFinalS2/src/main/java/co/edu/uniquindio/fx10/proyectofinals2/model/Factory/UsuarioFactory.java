package co.edu.uniquindio.fx10.proyectofinals2.model.Factory;

import co.edu.uniquindio.fx10.proyectofinals2.model.Usuario;
import co.edu.uniquindio.fx10.proyectofinals2.model.Persona;
import co.edu.uniquindio.fx10.proyectofinals2.utils.GeneradorID;

public class UsuarioFactory extends PersonaFactory {
    @Override
    public Persona crearPersona(String nombre, String telefono,
                                String correo, String usuario,
                                String contrasena) {
        String id = GeneradorID.generarIDUsuario();
        return new Usuario(id, nombre, telefono, correo, usuario, contrasena);
    }
}
