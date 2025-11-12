package co.edu.uniquindio.fx10;

class Usuario implements Observador {
    private String nombre;

    public Usuario(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public void actualizar(String nuevoVideo) {
        System.out.println(nombre + " recibió la notificación: ¡Nuevo video disponible! → " + nuevoVideo);
    }
}
