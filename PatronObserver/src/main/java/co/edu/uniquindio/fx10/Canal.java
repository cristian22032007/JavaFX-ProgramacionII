package co.edu.uniquindio.fx10;

import java.util.ArrayList;
import java.util.List;

public class Canal {
    private List<Observador> suscriptores = new ArrayList<>();
    private String nuevoVideo;

    public void agregarSuscriptor(Observador o) {
        suscriptores.add(o);
    }

    public void eliminarSuscriptor(Observador o) {
        suscriptores.remove(o);
    }

    public void subirVideo(String video) {
        this.nuevoVideo = video;
        notificarSuscriptores();
    }

    private void notificarSuscriptores() {
        for (Observador o : suscriptores) {
            o.actualizar(nuevoVideo);
        }
    }
}

