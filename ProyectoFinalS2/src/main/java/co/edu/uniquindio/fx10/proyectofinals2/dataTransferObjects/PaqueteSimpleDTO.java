package co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects;

public class PaqueteSimpleDTO {
        private String idPaquete;
        private double ancho;
        private double alto;
        private double largo;
        private double pesoKg;
        private double volumenM3;
        private String dimensionesFormateadas;

        public PaqueteSimpleDTO(String idPaquete, double ancho, double alto,
                          double largo, double pesoKg) {
            this.idPaquete = idPaquete;
            this.ancho = ancho;
            this.alto = alto;
            this.largo = largo;
            this.pesoKg = pesoKg;
            this.volumenM3 = calcularVolumen();
            this.dimensionesFormateadas = formatearDimensiones();
        }

        private double calcularVolumen() {
            // Convertir de cm³ a m³
            return (ancho * alto * largo) / 1_000_000.0;
        }

        private String formatearDimensiones() {
            return String.format("%.1f x %.1f x %.1f cm", ancho, alto, largo);
        }

        // Getters
        public String getIdPaquete() { return idPaquete; }
        public double getAncho() { return ancho; }
        public double getAlto() { return alto; }
        public double getLargo() { return largo; }
        public double getPesoKg() { return pesoKg; }
        public double getVolumenM3() { return volumenM3; }
        public String getDimensionesFormateadas() { return dimensionesFormateadas; }

        public String getInfoCompleta() {
            return String.format("%s - %s - %.2f kg",
                    idPaquete, dimensionesFormateadas, pesoKg);
        }
    }

