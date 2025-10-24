module co.edu.uniquindio.fx10.parcialnumero2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.fx10.parcialnumero2.model to javafx.base;
    opens co.edu.uniquindio.fx10.parcialnumero2.controller to javafx.fxml;

    exports co.edu.uniquindio.fx10.parcialnumero2;
}