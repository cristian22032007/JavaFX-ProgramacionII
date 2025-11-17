package co.edu.uniquindio.rapponcho.model.Factory;


import co.edu.uniquindio.rapponcho.model.Repartidor;
import co.edu.uniquindio.rapponcho.model.Persona;
import co.edu.uniquindio.rapponcho.utils.GeneradorID;

public class RepartidorFactory extends PersonaFactory {
    private String documento;

    public RepartidorFactory(String documento) {
        this.documento = documento;
    }

    @Override
    public Persona crearPersona(String nombre, String telefono,
                                String correo, String usuario,
                                String contrasena) {
        String id = GeneradorID.generarIDRepartidor();
        return new Repartidor(id, nombre, telefono, correo, usuario, contrasena, documento);
    }
}