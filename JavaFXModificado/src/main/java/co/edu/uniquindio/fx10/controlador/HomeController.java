package co.edu.uniquindio.fx10.controlador;

import co.edu.uniquindio.fx10.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class HomeController {
    @FXML
    private Button BtnCrearProducto;
    @FXML
    private Button BtnVerLista;

    @FXML
    private void onBtnCrearProducto() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/fx10/vista/FormularioProducto.fxml"));
            Parent form = loader.load();
            FormularioProductoController controller = loader.getController();
            controller.setHomeController(this);
            App.getPrimaryStage().getScene().setRoot(form);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBtnVerLista() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uniquindio/fx10/vista/Dashboard.fxml"));
            Parent dashboard = loader.load();
            App.getPrimaryStage().getScene().setRoot(dashboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
