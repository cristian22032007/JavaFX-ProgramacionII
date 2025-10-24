package co.edu.uniquindio.fx10.parcialnumero2.controller;

import co.edu.uniquindio.fx10.parcialnumero2.App;
import co.edu.uniquindio.fx10.parcialnumero2.model.Inmueble;
import co.edu.uniquindio.fx10.parcialnumero2.model.InmuebleFactory;
import co.edu.uniquindio.fx10.parcialnumero2.repository.Inmobiliaria;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegistrarInmueble {
    @FXML
    private ComboBox<String> cboxTipoinmueble;
    @FXML
    private TextField txtCiudad;
    @FXML
    private TextField txtNmrHabitantes;
    @FXML
    private TextField txtNmrPisos;
    @FXML
    private TextField txtPrecio;
    @FXML
    private Button btnAgregar;

    private Inmobiliaria inmuebleSistem;


    public void initialize() {
        inmuebleSistem = Inmobiliaria.getIsntancia();
        // Llenar el ComboBox con los tipos de comida
        cboxTipoinmueble.getItems().addAll("Casa", "Apartamento", "Finca", "Local");
        cboxTipoinmueble.setValue("Casa"); // Valor por defecto



    }

    @FXML
    private void agregarInmueble () {
        String tipo = cboxTipoinmueble.getValue();
        if (!validarCampos()) {
            return;
        }
        try {

            String Ciudad = txtCiudad.getText().trim();
            int NumeroDeHabitantes = Integer.parseInt(txtNmrHabitantes.getText().trim());
            int NumeroDePisos = Integer.parseInt(txtNmrPisos.getText().trim());
            double precio = Double.parseDouble(txtPrecio.getText().trim());

            Inmueble inm = InmuebleFactory.createInmueble(tipo, Ciudad, NumeroDeHabitantes, NumeroDePisos, precio);
            inmuebleSistem.addInmueble(inm);
            mostrarAlerta("Éxito", "Inmueble creado correctamente", Alert.AlertType.INFORMATION);

            // Volver al inicio
            volverAlInicio();

        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Los numeros de habitantes, el numero de psiso y  el precio deben ser valores numéricos válidos", Alert.AlertType.ERROR);
        }
    }
        /**
         * Valida que los campos del formulario estén completos
         */
    private boolean validarCampos () {
        if (txtCiudad.getText().trim().isEmpty()) {
                mostrarAlerta("Error de validación", "La ciudad es obligatoria", Alert.AlertType.WARNING);
                return false;
            }
        if (txtNmrHabitantes.getText().trim().isEmpty()) {
                mostrarAlerta("Error de validación", "El numero de habitantes es obligatorio", Alert.AlertType.WARNING);
                return false;
            }
        if (txtNmrPisos.getText().trim().isEmpty()) {
                mostrarAlerta("Error de validación", "El numero de pisos es obligatorio", Alert.AlertType.WARNING);
                return false;
            }
        if (txtPrecio.getText().trim().isEmpty()) {
                mostrarAlerta("Error de validación", "El precio es obligatorio", Alert.AlertType.WARNING);
                return false;
            }
        return true;
        }
        @FXML
    private void volverAlInicio () {
        try {
            Parent inicio;
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/fx10/parcialnumero2/Home.fxml"));
            inicio = loader.load();
            App.getPrimaryStage().getScene().setRoot(inicio);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo volver al inicio", Alert.AlertType.ERROR);
            e.printStackTrace();
    }
}
        /**
         * Muestra una alerta al usuario
         */
    private void mostrarAlerta (String titulo, String mensaje, Alert.AlertType tipo){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}


