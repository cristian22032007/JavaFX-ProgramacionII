package co.edu.uniquindio.poo.parcial3_p2.model;

import java.util.List;

public class Paciente {
    private String id;
    private String nombre;
    private List<Cita> listCitasPaciente;

    public Paciente(String id, String nombre, List<Cita> listCitasPaciente) {
        this.id = id;
        this.nombre = nombre;
        this.listCitasPaciente = listCitasPaciente;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Cita> getListCitasPaciente() {
        return listCitasPaciente;
    }
}

