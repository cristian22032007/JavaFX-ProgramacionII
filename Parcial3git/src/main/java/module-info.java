module co.edu.uniquindio.poo.parcial3_p2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.poo.parcial3_p2 to javafx.fxml;
    exports co.edu.uniquindio.poo.parcial3_p2;
    exports co.edu.uniquindio.poo.parcial3_p2.model;
    opens co.edu.uniquindio.poo.parcial3_p2.model to javafx.fxml;
}