package co.edu.uniquindio.fx10.parcialnumero2.model;

/**
 * Utilizamos el Patron Factory para facilitar y crear por tipo
 */

public class InmuebleFactory {
    public static Inmueble createInmueble(String tipo, String ciudad, int numeroHabitantes, int numeroPisos, double precio) {
        switch (tipo.toLowerCase()){
            case "apartamento":
                return new ApartDecorator.Builder()
                        .tipo(tipo)
                        .ciudad(ciudad)
                        .numeroHabitante(numeroHabitantes)
                        .numeroPiso(numeroPisos)
                        .precio(precio).build();
            case "casa":
                return new CasaDecorator.Builder()
                        .tipo(tipo)
                        .ciudad(ciudad)
                        .numeroHabitante(numeroHabitantes)
                        .numeroPiso(numeroPisos)
                        .precio(precio).build();
            case "local":
                return new LocalDecorator.Builder()
                        .tipo(tipo)
                        .ciudad(ciudad)
                        .numeroHabitante(numeroHabitantes)
                        .numeroPiso(numeroPisos)
                        .precio(precio).build();
            case "finca":
                return new FincaDecorator.Builder()
                        .tipo(tipo)
                        .ciudad(ciudad)
                        .numeroHabitante(numeroHabitantes)
                        .numeroPiso(numeroPisos)
                        .precio(precio).build();

        }
        return null;
    }
}
