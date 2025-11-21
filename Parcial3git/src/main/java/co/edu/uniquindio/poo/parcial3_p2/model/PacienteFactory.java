package co.edu.uniquindio.poo.parcial3_p2.model;
import java.util.ArrayList;
import java.util.List;

// FACTORY

public class PacienteFactory {

    public static Paciente crearPaciente(String id, String nombre) {
        return new Paciente(id, nombre, new ArrayList<>());
    }

    public static Paciente crearPacienteConCitas(String id, String nombre, List<Cita> citas) {
        return new Paciente(id, nombre, citas);
    }
}

