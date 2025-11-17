package co.edu.uniquindio.rapponcho.dataTransferObjects;
public class PaqueteSimpleDTO {
        private String idPaquete;
        private double ancho;
        private double alto;
        private double largo;
        private double pesoKg;
        private double volumenM3;
        private String dimensionesFormateadas;

        public PaqueteSimpleDTO(String idPaquete, double ancho, double alto,
                          double largo, double pesoKg, double volumenM3, String dimensionesFormateadas) {
            this.idPaquete = idPaquete;
            this.ancho = ancho;
            this.alto = alto;
            this.largo = largo;
            this.pesoKg = pesoKg;
            this.volumenM3 = volumenM3;
            this.dimensionesFormateadas = dimensionesFormateadas;
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

