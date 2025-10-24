package co.edu.uniquindio.fx10.sistemagestiopizza.controller;


import co.edu.uniquindio.fx10.sistemagestiopizza.App;
import co.edu.uniquindio.fx10.sistemagestiopizza.model.*;
import co.edu.uniquindio.fx10.sistemagestiopizza.repository.OrderSistem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import java.io.IOException;

public class CreateOrder {

        @FXML
        private ComboBox<String> cboTipoComida;

        @FXML
        private CheckBox chkExtraQueso;

        @FXML
        private CheckBox chkExtraSalsa;

        @FXML
        private CheckBox chkBebida;

        @FXML
        private Label lblDescripcion;

        @FXML
        private Label lblPrecioTotal;

        @FXML
        private Button btnAgregarOrden;

        @FXML
        private Button btnVolver;

        // Singleton
        private OrderSistem ordenSistema;

        // La orden actual que estamos construyendo
        private Order ordenActual;

        private HomeSistem homeSistem;

        @FXML
        public void initialize() {
            ordenSistema = OrderSistem.getInstance();
            // Llenar el ComboBox con los tipos de comida
            cboTipoComida.getItems().addAll("Hamburguesa", "Pizza", "Sushi");
            cboTipoComida.setValue("Pizza"); // Valor por defecto

            // Actualizar vista cuando cambie la selección
            cboTipoComida.setOnAction(e -> actualizarOrden());
            chkExtraQueso.setOnAction(e -> actualizarOrden());
            chkExtraSalsa.setOnAction(e -> actualizarOrden());
            chkBebida.setOnAction(e -> actualizarOrden());

            // Mostrar la orden inicial
            actualizarOrden();
        }

        @FXML
        private void actualizarOrden() {
            // PASO 1: Crear comida base usando FACTORY
            String tipoComida = cboTipoComida.getValue();
            ordenActual = OrderFactory.createOrder(tipoComida);

            // PASO 2: Aplicar DECORATORS según los checkboxes seleccionados
            if (chkExtraQueso.isSelected()) {
                ordenActual = new ExtraCheeseDecorator(ordenActual);
            }

            if (chkExtraSalsa.isSelected()) {
                ordenActual = new ExtraSauceDecorator(ordenActual);
            }

            if (chkBebida.isSelected()) {
                ordenActual = new DrinkDecorator(ordenActual);
            }

            // PASO 3: Mostrar descripción y precio actualizado
            lblDescripcion.setText(ordenActual.getDescription());
            lblPrecioTotal.setText(String.format("$%.2f", ordenActual.getCost()));
        }

        @FXML
        private void agregarOrden() {
            // Guardar la orden en el Singleton
            ordenSistema.addOrder(ordenActual);

            // Mostrar confirmación
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Orden Agregada");
            alert.setHeaderText(null);
            alert.setContentText("¡Orden agregada exitosamente!\n\n" +
                    ordenActual.getDescription() + "\n" +
                    "Total: $" + String.format("%.2f", ordenActual.getCost()));
            alert.showAndWait();

            // Limpiar selección para nueva orden
            limpiarFormulario();
        }

        private void limpiarFormulario() {
            cboTipoComida.setValue("Pizza");
            chkExtraQueso.setSelected(false);
            chkExtraSalsa.setSelected(false);
            chkBebida.setSelected(false);
            actualizarOrden();
        }
        public HomeSistem getHomeSistem() {
            return homeSistem;
        }
        public void setHomeSistem(HomeSistem homeSistem) {
            this.homeSistem = homeSistem;
        }
        @FXML
        private void volverAlInicio() throws IOException {
                    Parent inicio;
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/fx10/sistemagestiopizza/Home.fxml"));
                    inicio = loader.load();
                    App.getPrimaryStage().getScene().setRoot(inicio);
            }

}
