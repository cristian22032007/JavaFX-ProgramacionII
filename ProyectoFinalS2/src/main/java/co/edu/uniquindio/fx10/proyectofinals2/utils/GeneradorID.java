package co.edu.uniquindio.fx10.proyectofinals2.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

    public class GeneradorID {
        private static final AtomicInteger contadorUsuario = new AtomicInteger(1000);
        private static final AtomicInteger contadorEnvio = new AtomicInteger(1);
        private static final AtomicInteger contadorPago = new AtomicInteger(1);
        private static final AtomicInteger contadorDireccion = new AtomicInteger(1);
        private static final AtomicInteger contadorRepartidor = new AtomicInteger(100);
        private static final AtomicInteger contadorPaquete = new AtomicInteger(1);
        private static final AtomicInteger contadorIncidencia = new AtomicInteger(1);

        public static String generarIDIncidencia() {
            return "INC" + String.format("%06d", contadorIncidencia.incrementAndGet());
        }

        public static String generarIDUsuario() {
            return "USR" + contadorUsuario.incrementAndGet();
        }

        public static String generarIDEnvio() {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            return "ENV" + timestamp + String.format("%04d", contadorEnvio.incrementAndGet());
        }

        public static String generarIDPago() {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            return "PAG" + timestamp + contadorPago.incrementAndGet();
        }

        public static String generarIDDireccion() {
            return "DIR" + String.format("%05d", contadorDireccion.incrementAndGet());
        }

        public static String generarIDRepartidor() {
            return "REP" + contadorRepartidor.incrementAndGet();
        }

        public static String generarIDPaquete() {
            return "PKG" + String.format("%06d", contadorPaquete.incrementAndGet());
        }

        public static String generarIDTarifa() {
            return "TAR" + System.currentTimeMillis();
        }
    }

