package co.edu.uniquindio.fx10.sistemamotos.repository;

import co.edu.uniquindio.fx10.sistemamotos.model.Moto;

import java.util.ArrayList;
import java.util.List;

public class SistemMoto {
    private static SistemMoto instacia;
    private List<Moto> motos;


    private SistemMoto() {
        motos = new ArrayList<Moto>();
    }
    public static SistemMoto getInstacia() {
        if (instacia == null) {instacia = new SistemMoto();}return instacia;}

    public List<Moto> getMotos() {
        return motos;
    }
    public void setMotos(List<Moto> motos) {
        this.motos = motos;
    }
    public Moto buscarPorCodigo(String placa) {
        return motos.stream().filter(m -> m.getPlaca().equals(placa)).findFirst().orElse(null);
    }
    public void addMoto(Moto moto) {
        motos.add(moto);
    }
}
