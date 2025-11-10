package co.edu.uniquindio.fx10.proyectofinals2.model.Factory;

import co.edu.uniquindio.fx10.proyectofinals2.model.Persona;

public abstract class PersonaFactory {
    public abstract Persona crearPersona(String nombre, String telefono,
                                         String correo, String usuario,
                                         String contrasena);
}

