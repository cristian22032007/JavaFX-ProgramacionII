package co.edu.uniquindio.fx10.parcialnumero2.model;

/**
 * Utilizamos el Patron Factory para facilitar y crear por tipo
 */

public class InmuebleFactory {
    public static InmuebleIT createInmueble(String tipo, String ciudad, int numeroHabitantes, int numeroPisos, double precio) {
        InmuebleIT inmueble = new Inmueble.Builder()
                .tipo(tipo)
                .ciudad(ciudad)
                .numeroHabitante(numeroHabitantes)
                .numeroPiso(numeroPisos)
                .precio(precio)
                .build();
        return switch (tipo.toLowerCase()) {
            case "casa" -> new CasaDecorator(inmueble);
            case "apartamento" -> new ApartDecorator(inmueble);
            case "finca" -> new FincaDecorator(inmueble);
            case "local" -> new LocalDecorator(inmueble);
            default -> null;
            };
    }
}
