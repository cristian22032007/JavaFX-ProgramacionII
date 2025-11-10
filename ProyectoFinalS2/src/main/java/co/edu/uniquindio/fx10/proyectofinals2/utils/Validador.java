package co.edu.uniquindio.fx10.proyectofinals2.utils;

import java.util.regex.Pattern;

public class Validador {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern TELEFONO_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    public static boolean validarEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean validarTelefono(String telefono) {
        return telefono != null && TELEFONO_PATTERN.matcher(telefono).matches();
    }

    public static boolean validarTextoNoVacio(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean validarNumeroPositivo(double numero) {
        return numero > 0;
    }

    public static boolean validarPeso(double peso) {
        return peso > 0 && peso <= 100; // Máximo 100 kg
    }

    public static boolean validarDimensiones(double valor) {
        return valor > 0 && valor <= 200; // Máximo 200 cm
    }

    public static boolean validarUsuarioYContrasena(String valor) {
        return valor != null && valor.length() >= 4;
    }
}
