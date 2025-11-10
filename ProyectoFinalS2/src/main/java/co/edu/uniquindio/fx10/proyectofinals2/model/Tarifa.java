package co.edu.uniquindio.fx10.proyectofinals2.model;

public class Tarifa implements ITarifa {
        private String idTarifa;
        private final double tarifaBase = 30000;
        private double costoPorKm;
        private double costoPorKg;
        private double costoPorM3; // volumen


        protected Tarifa(Builder builder) {
            this.idTarifa = builder.idTarifa;
            this.costoPorKm = builder.costoPorKm;
            this.costoPorKg = builder.costoPorKg;
            this.costoPorM3 = builder.costoPorM3;
        }
        public static class Builder {
            private String idTarifa;
            private double costoPorKm;
            private double costoPorKg;
            private double costoPorM3;

            public Builder idTarifa(String idTarifa) {
                this.idTarifa = idTarifa;
                return this;
            }
            public Builder costoPorKm(double costoPorKm) {
                this.costoPorKm = costoPorKm;
                return this;
            }
            public Builder costoPorKg(double costoPorKg) {
                this.costoPorKg = costoPorKg;
                return this;
            }
            public Builder costoPorM3(double costoPorM3) {
                this.costoPorM3 = costoPorM3;
                return this;
            }
            public Tarifa build() {
                return new Tarifa(this);
            }
        }

        /**
         * Calcula el costo base sin servicios adicionales
         */
        @Override
        public double CalcularCosto(double distanciaKm, double pesoKg, double volumenM3) {
            double costo = tarifaBase;
            costo += distanciaKm * costoPorKm;
            costo += pesoKg * costoPorKg;
            costo += volumenM3 * costoPorM3;
            return costo;
        }
        public String getDescripcion() {
            return "Tarifa Base";
        }




    // Getters
        public String getIdTarifa() { return idTarifa; }
        public double getTarifaBase() { return tarifaBase; }
        public double getCostoPorKm() { return costoPorKm; }
        public double getCostoPorKg() { return costoPorKg; }
        public double getCostoPorM3() { return costoPorM3; }

}

