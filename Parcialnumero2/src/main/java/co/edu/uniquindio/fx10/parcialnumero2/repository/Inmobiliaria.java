package co.edu.uniquindio.fx10.parcialnumero2.repository;

import co.edu.uniquindio.fx10.parcialnumero2.model.Inmueble;

import java.util.ArrayList;
import java.util.List;

public class Inmobiliaria {
    private static Inmobiliaria isntancia;
    private List<Inmueble> inmuebles;
    private Inmobiliaria() {
        inmuebles = new ArrayList<Inmueble>();
    }
    public static Inmobiliaria getIsntancia() {
        if (isntancia == null) {
            isntancia = new Inmobiliaria();
        }
        return isntancia;
    }
    public List<Inmueble> getInmuebles() {
        return inmuebles;
    }
    public void addInmueble(Inmueble inmueble) {
        inmuebles.add(inmueble);
    }
    public void removeInmueble(Inmueble inmueble) {
        inmuebles.remove(inmueble);
    }
    public Inmueble buscarInmueble(double precio) {
        return inmuebles.stream().filter(i -> i.getPrecio() == precio).findFirst().orElse(null);

    }
}
