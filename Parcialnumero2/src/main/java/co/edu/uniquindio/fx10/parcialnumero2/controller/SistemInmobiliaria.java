package co.edu.uniquindio.fx10.parcialnumero2.controller;

import co.edu.uniquindio.fx10.parcialnumero2.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class SistemInmobiliaria {
    @FXML
    private Button BtnCreateInmueble;
    @FXML
    private Button BtnSeeInmueble;

    @FXML
    public void onBtnCreateInmueble() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("co.edu.uniquindio.fx10.parcialnumero2.CreateInmueble.fxml"));
        Parent root = loader.load();
        App.getPrimaryStage().getScene().setRoot(root);
    }

    @FXML
    public void onBtnSeeInmueble() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("co.edu.unqiudio.fx10.parcialnumero2.SeeInmueble.fxml"));
        Parent root = loader.load();
        App.getPrimaryStage().getScene().setRoot(root);
    }

}
