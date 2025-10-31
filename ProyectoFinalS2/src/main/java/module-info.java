module co.edu.uniquindio.fx10.proyectofinals2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens co.edu.uniquindio.fx10.proyectofinals2 to javafx.fxml;
    exports co.edu.uniquindio.fx10.proyectofinals2;
    exports co.edu.uniquindio.fx10.proyectofinals2.controllers;
    opens co.edu.uniquindio.fx10.proyectofinals2.controllers to javafx.fxml;
}