package co.edu.uniquindio.rapponcho.model.Factory;

import co.edu.uniquindio.rapponcho.model.Persona;

public abstract class PersonaFactory {
    public abstract Persona crearPersona(String nombre, String telefono,
                                         String correo, String usuario,
                                         String contrasena);
}

