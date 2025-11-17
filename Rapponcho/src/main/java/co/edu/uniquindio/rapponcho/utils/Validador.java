
package co.edu.uniquindio.rapponcho.utils;

import java.util.regex.Pattern;

/**
 * Clase mejorada de validaciones con soporte para alertas automáticas
 * Mantiene compatibilidad con código existente
 */
public class Validador {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern TELEFONO_PATTERN =
            Pattern.compile("^[0-9]{10}$");

    // ========== VALIDACIONES BÁSICAS (SIN ALERTAS) ==========

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

    // ========== VALIDACIONES CON ALERTAS AUTOMÁTICAS ==========

    /**
     * Valida email y muestra alerta si es inválido
     * @return true si es válido, false si no
     */
    public static boolean validarEmailConAlerta(String email) {
        if (!validarEmail(email)) {
            AlertHelper.mostrarErrorValidacion(
                    "El correo electrónico no es válido.\n" +
                            "Formato esperado: ejemplo@dominio.com"
            );
            return false;
        }
        return true;
    }

    /**
     * Valida teléfono y muestra alerta si es inválido
     * @return true si es válido, false si no
     */
    public static boolean validarTelefonoConAlerta(String telefono) {
        if (!validarTelefono(telefono)) {
            AlertHelper.mostrarErrorValidacion(
                    "El número de teléfono debe tener exactamente 10 dígitos.\n" +
                            "Ejemplo: 3001234567"
            );
            return false;
        }
        return true;
    }

    /**
     * Valida texto no vacío y muestra alerta si está vacío
     * @param nombreCampo Nombre del campo para el mensaje
     * @return true si es válido, false si no
     */
    public static boolean validarTextoNoVacioConAlerta(String texto, String nombreCampo) {
        if (!validarTextoNoVacio(texto)) {
            AlertHelper.mostrarErrorValidacion(
                    "El campo '" + nombreCampo + "' no puede estar vacío."
            );
            return false;
        }
        return true;
    }

    /**
     * Valída usuario y muestra alerta si es inválido
     * @return true si es válido, false si no
     */
    public static boolean validarUsuarioConAlerta(String usuario) {
        if (!validarUsuarioYContrasena(usuario)) {
            AlertHelper.mostrarErrorValidacion(
                    "El nombre de usuario debe tener al menos 4 caracteres.\n" +
                            "Puede contener letras, números y guiones bajos."
            );
            return false;
        }
        return true;
    }

    /**
     * Valida contraseña y muestra alerta si es inválida
     * @return true si es válida, false si no
     */
    public static boolean validarContrasenaConAlerta(String contrasena) {
        if (!validarUsuarioYContrasena(contrasena)) {
            AlertHelper.mostrarErrorValidacion(
                    "La contraseña debe tener al menos 4 caracteres.\n" +
                            "Se recomienda usar una combinación de letras y números."
            );
            return false;
        }
        return true;
    }

    /**
     * Valida que dos contraseñas coincidan
     * @return true si coinciden, false si no
     */
    public static boolean validarContrasenasCoinciden(String contrasena, String confirmacion) {
        if (!contrasena.equals(confirmacion)) {
            AlertHelper.mostrarErrorValidacion(
                    "Las contraseñas no coinciden.\n" +
                            "Por favor, verifica que ambas sean idénticas."
            );
            return false;
        }
        return true;
    }

    /**
     * Valida peso y muestra alerta si es inválido
     * @return true si es válido, false si no
     */
    public static boolean validarPesoConAlerta(double peso) {
        if (!validarPeso(peso)) {
            AlertHelper.mostrarErrorValidacion(
                    "El peso debe ser mayor a 0 kg y no superar los 100 kg.\n" +
                            "Peso ingresado: " + peso + " kg"
            );
            return false;
        }
        return true;
    }

    /**
     * Valida dimensiones y muestra alerta si son inválidas
     * @return true si es válido, false si no
     */
    public static boolean validarDimensionesConAlerta(double valor, String nombreDimension) {
        if (!validarDimensiones(valor)) {
            AlertHelper.mostrarErrorValidacion(
                    "La dimensión '" + nombreDimension + "' debe ser mayor a 0 cm y no superar los 200 cm.\n" +
                            "Valor ingresado: " + valor + " cm"
            );
            return false;
        }
        return true;
    }

    /**
     * Valida formulario completo de registro
     * @return true si todo es válido, false si hay errores
     */
    public static boolean validarFormularioRegistro(
            String nombre, String telefono, String email,
            String usuario, String contrasena, String confirmacion) {

        if (!validarTextoNoVacioConAlerta(nombre, "Nombre")) return false;
        if (!validarTelefonoConAlerta(telefono)) return false;
        if (!validarEmailConAlerta(email)) return false;
        if (!validarUsuarioConAlerta(usuario)) return false;
        if (!validarContrasenaConAlerta(contrasena)) return false;
        if (!validarContrasenasCoinciden(contrasena, confirmacion)) return false;

        return true;
    }

    /**
     * Valida formato de documento de identidad
     */
    public static boolean validarDocumento(String documento) {
        return documento != null && documento.matches("^[0-9]{6,15}$");
    }

    /**
     * Valida documento con alerta
     */
    public static boolean validarDocumentoConAlerta(String documento) {
        if (!validarDocumento(documento)) {
            AlertHelper.mostrarErrorValidacion(
                    "El documento de identidad debe contener entre 6 y 15 dígitos.\n" +
                            "Solo se permiten números."
            );
            return false;
        }
        return true;
    }
}