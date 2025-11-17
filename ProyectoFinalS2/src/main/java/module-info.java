module co.edu.uniquindio.fx10.proyectofinals2 {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Abrir paquetes para JavaFX FXML y Reflection
    opens co.edu.uniquindio.fx10.proyectofinals2 to javafx.fxml, javafx.graphics;
    opens co.edu.uniquindio.fx10.proyectofinals2.controllers to javafx.fxml;
    opens co.edu.uniquindio.fx10.proyectofinals2.model to javafx.base;
    opens co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects to javafx.base;

    // Exportar paquetes principales
    exports co.edu.uniquindio.fx10.proyectofinals2;
    exports co.edu.uniquindio.fx10.proyectofinals2.controllers;
    exports co.edu.uniquindio.fx10.proyectofinals2.model;
    exports co.edu.uniquindio.fx10.proyectofinals2.model.Factory;
    exports co.edu.uniquindio.fx10.proyectofinals2.model.AdapterDTO;
    exports co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects;
    exports co.edu.uniquindio.fx10.proyectofinals2.services;
    exports co.edu.uniquindio.fx10.proyectofinals2.reposytorie;
    exports co.edu.uniquindio.fx10.proyectofinals2.utils;
}