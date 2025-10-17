package co.edu.uniquindio.fx10.parcialnumero2.model;

public class Inmueble implements InmuebleIT {
    private String tipo;
    private String ciudad;
    private int numeroHabitantes;
    private int numeroPisos;
    private double precio;

    private Inmueble (Builder builder){
        this.tipo = builder.tipo;
        this.ciudad = builder.ciudad;
        this.numeroHabitantes = builder.numeroHabitantes;
        this.numeroPisos = builder.numeroPisos;
        this.precio = builder.precio;
    }

    @Override
    public String descripcion() {
        return "verificada/o";
    }

    /**
     * Utilizamos el Patron Builder para facilitar la contruccion
     */
    public static class Builder {
        private String tipo;
        private String ciudad;
        private int numeroHabitantes;
        private int numeroPisos;
        private double precio;

        public Builder tipo(String tipo) {this.tipo = tipo;return this;}
        public Builder ciudad(String ciudad) {this.ciudad = ciudad;return this;}
        public Builder numeroHabitante(int numeroHabitante) {this.numeroHabitantes = numeroHabitante;return this;}
        public Builder numeroPiso(int numeroPiso) {this.numeroPisos = numeroPiso;return this;}
        public Builder precio(double precio) {this.precio = precio;return this;}
        public Inmueble build(){return new Inmueble(this);}
    }
    public String getTipo() {
        return tipo;
    }
    public String getCiudad() {
        return ciudad;
    }
    public int getNumeroHabitantes() {
        return numeroHabitantes;
    }
    public int getNumeroPisos() {
        return numeroPisos;
    }
    public double getPrecio() {
        return precio;
    }

}
