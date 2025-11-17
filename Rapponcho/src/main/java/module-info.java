module co.edu.uniquindio.rapponcho {
    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Abrir paquetes para JavaFX FXML y Reflection
    opens co.edu.uniquindio.rapponcho to javafx.fxml, javafx.graphics;
    opens co.edu.uniquindio.rapponcho.controllers to javafx.fxml;
    opens co.edu.uniquindio.rapponcho.model to javafx.base;
    opens co.edu.uniquindio.rapponcho.dataTransferObjects to javafx.base;

    // Exportar paquetes principales
    exports co.edu.uniquindio.rapponcho;
    exports co.edu.uniquindio.rapponcho.controllers;
    exports co.edu.uniquindio.rapponcho.model;
    exports co.edu.uniquindio.rapponcho.model.Factory;
    exports co.edu.uniquindio.rapponcho.model.AdapterDTO;
    exports co.edu.uniquindio.rapponcho.dataTransferObjects;
    exports co.edu.uniquindio.rapponcho.services;
    exports co.edu.uniquindio.rapponcho.reposytorie;
    exports co.edu.uniquindio.rapponcho.utils;
}