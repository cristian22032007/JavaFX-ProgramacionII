package co.edu.uniquindio.poo.parcial3_p2.model;

// BUILDER

public class Medico {

    private String nombre;
    private String id;
    private Especializacion especializacion;

    private Medico(Builder builder) {
        this.nombre = builder.nombre;
        this.id = builder.id;
        this.especializacion = builder.especializacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getId() {
        return id;
    }

    public Especializacion getEspecializacion() {
        return especializacion;
    }


    public static class Builder {

        private String nombre;
        private String id;
        private Especializacion especializacion;

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder especializacion(Especializacion especializacion) {
            this.especializacion = especializacion;
            return this;
        }

        public Medico build() {
            return new Medico(this);
        }
    }
}




