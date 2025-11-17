package co.edu.uniquindio.rapponcho.model;

public enum ZonaDireccion {

        ARMENIA(10, 5, "630001"),
        CALARCA(14, 9, "631001"),
        CIRCASIA(8, 10, "631020"),
        CORDOBA(4, 2, "632001"),
        FILANDIA(3, 8, "634001"),
        GENOVA(2, 1, "632060"),
        LA_TEBAIDA(12, 2, "633001"),
        MONTENEGRO(7, 3, "631020"),
        PIJAO(5, 1, "632060"),
        QUIMBAYA(6, 5, "634020"),
        SALENTO(4, 9, "631020"),
        BUENAVISTA(3, 2, "632040");

        private final double coordenadaX;
        private final double coordenadaY;
        private final String codigoPostal;

        ZonaDireccion(double coordenadaX, double coordenadaY, String codigoPostal) {
            this.coordenadaX = coordenadaX;
            this.coordenadaY = coordenadaY;
            this.codigoPostal = codigoPostal;
        }

        public double getCoordenadaX() {
            return coordenadaX;
        }

        public double getCoordenadaY() {
            return coordenadaY;
        }

        public String getCodigoPostal() {
            return codigoPostal;
        }

        @Override
        public String toString() {
            String nombre = name().charAt(0) + name().substring(1).toLowerCase().replace("_", " ");
            return nombre + " (C.P. " + codigoPostal + ")";
        }
    }



