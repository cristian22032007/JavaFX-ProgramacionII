module co.edu.uniquindio.fx10.proyectofinals2 {
    // Módulos JavaFX requeridos
    requires javafx.controls;
    requires javafx.fxml;

    // Abrir paquetes para JavaFX FXML
    opens co.edu.uniquindio.fx10.proyectofinals2 to javafx.fxml;
    opens co.edu.uniquindio.fx10.proyectofinals2.controllers to javafx.fxml;
    opens co.edu.uniquindio.fx10.proyectofinals2.model to javafx.fxml;
    opens co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects to javafx.fxml;

    // Exportar paquetes necesarios
    exports co.edu.uniquindio.fx10.proyectofinals2;
    exports co.edu.uniquindio.fx10.proyectofinals2.controllers;
    exports co.edu.uniquindio.fx10.proyectofinals2.model;
    exports co.edu.uniquindio.fx10.proyectofinals2.services;
    exports co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects;
    exports co.edu.uniquindio.fx10.proyectofinals2.utils;
    exports co.edu.uniquindio.fx10.proyectofinals2.reposytorie;
}