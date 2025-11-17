package co.edu.uniquindio.fx10.proyectofinals2.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

// ========== IMPORTS JAVA CORE ==========
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

// ========== IMPORTS DEL PROYECTO ==========
import co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects.*;
import co.edu.uniquindio.fx10.proyectofinals2.model.*;
import co.edu.uniquindio.fx10.proyectofinals2.model.AdapterDTO.DTOAdapter;
import co.edu.uniquindio.fx10.proyectofinals2.services.*;
import co.edu.uniquindio.fx10.proyectofinals2.utils.AlertHelper;
import co.edu.uniquindio.fx10.proyectofinals2.utils.Validador;

/**
 * Controlador principal del panel de administración
 * Gestiona usuarios, repartidores, envíos y métricas del sistema
 */
public class AdminMainController {

    // ========== HEADER ==========
    @FXML private Label lblBienvenida;

    // ========== TAB USUARIOS ==========
    @FXML private TableView<UsuarioSimpleDTO> tableUsuarios;
    @FXML private TableColumn<UsuarioSimpleDTO, String> colUsuarioId;
    @FXML private TableColumn<UsuarioSimpleDTO, String> colUsuarioNombre;
    @FXML private TableColumn<UsuarioSimpleDTO, String> colUsuarioCorreo;
    @FXML private TableColumn<UsuarioSimpleDTO, String> colUsuarioTelefono;
    @FXML private TableColumn<UsuarioSimpleDTO, Integer> colUsuarioEnvios;
    @FXML private TableColumn<UsuarioSimpleDTO, Void> colUsuarioAcciones;

    // ========== TAB REPARTIDORES ==========
    @FXML private TableView<RepartidorSimpleDTO> tableRepartidores;
    @FXML private TableColumn<RepartidorSimpleDTO, String> colRepartidorId;
    @FXML private TableColumn<RepartidorSimpleDTO, String> colRepartidorNombre;
    @FXML private TableColumn<RepartidorSimpleDTO, String> colRepartidorDocumento;
    @FXML private TableColumn<RepartidorSimpleDTO, String> colRepartidorTelefono;
    @FXML private TableColumn<RepartidorSimpleDTO, String> colRepartidorEstado;
    @FXML private TableColumn<RepartidorSimpleDTO, Integer> colRepartidorEnvios;
    @FXML private TableColumn<RepartidorSimpleDTO, Void> colRepartidorAcciones;

    // ========== TAB ENVÍOS ==========
    @FXML private ComboBox<EstadoEnvio> cmbFiltroEstadoEnvio;
    @FXML private TableView<EnvioDetalleDTO> tableEnvios;
    @FXML private TableColumn<EnvioDetalleDTO, String> colEnvioId;
    @FXML private TableColumn<EnvioDetalleDTO, String> colEnvioUsuario;
    @FXML private TableColumn<EnvioDetalleDTO, String> colEnvioRepartidor;
    @FXML private TableColumn<EnvioDetalleDTO, String> colEnvioOrigen;
    @FXML private TableColumn<EnvioDetalleDTO, String> colEnvioDestino;
    @FXML private TableColumn<EnvioDetalleDTO, String> colEnvioEstado;
    @FXML private TableColumn<EnvioDetalleDTO, Void> colEnvioAcciones;

    // ========== TAB MÉTRICAS ==========
    @FXML private Label lblTotalUsuarios;
    @FXML private Label lblTotalRepartidores;
    @FXML private Label lblTotalEnvios;
    @FXML private Label lblIngresosTotales;
    @FXML private Label lblEnviosSolicitados;
    @FXML private Label lblEnviosAsignados;
    @FXML private Label lblEnviosEnRuta;
    @FXML private Label lblEnviosEntregados;
    @FXML private Label lblEnviosIncidencias;
    @FXML private ListView<String> listTopRepartidores;
    @FXML private ListView<String> listTopUsuarios;

    // ========== SERVICIOS ==========
    private final AdministradorService adminService;
    private final UsuarioService usuarioService;
    private final RepartidorService repartidorService;
    private final EnvioService envioService;

    // ========== ESTADO ==========
    private Administrador adminActual;

    public AdminMainController() {
        this.adminService = new AdministradorService();
        this.usuarioService = new UsuarioService();
        this.repartidorService = new RepartidorService();
        this.envioService = new EnvioService();
    }

    @FXML
    public void initialize() {
        configurarTablasUsuarios();
        configurarTablasRepartidores();
        configurarTablasEnvios();
        configurarFiltros();
    }

    public void setAdministrador(Administrador admin) {
        this.adminActual = admin;
        lblBienvenida.setText("Panel de Administración - " + admin.getNombre());
        cargarDatosIniciales();
    }

    // ========== CONFIGURACIÓN TABLAS ==========

    private void configurarTablasUsuarios() {
        colUsuarioId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUsuarioNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colUsuarioCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colUsuarioTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colUsuarioEnvios.setCellValueFactory(new PropertyValueFactory<>("cantidadEnvios"));

        // Agregar botones de acción
        colUsuarioAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnVer = new Button("👁 Ver");
            private final Button btnEliminar = new Button("🗑");

            {
                btnVer.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-cursor: hand;");
                btnEliminar.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");

                btnVer.setOnAction(e -> {
                    UsuarioSimpleDTO usuario = getTableView().getItems().get(getIndex());
                    mostrarDetalleUsuario(usuario);
                });

                btnEliminar.setOnAction(e -> {
                    UsuarioSimpleDTO usuario = getTableView().getItems().get(getIndex());
                    eliminarUsuario(usuario);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox box = new javafx.scene.layout.HBox(5, btnVer, btnEliminar);
                    setGraphic(box);
                }
            }
        });
    }

    private void configurarTablasRepartidores() {
        colRepartidorId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRepartidorNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colRepartidorDocumento.setCellValueFactory(new PropertyValueFactory<>("documento"));
        colRepartidorTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colRepartidorEstado.setCellValueFactory(new PropertyValueFactory<>("estadoDisponibilidad"));
        colRepartidorEnvios.setCellValueFactory(new PropertyValueFactory<>("enviosAsignados"));

        colRepartidorAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnVer = new Button("👁");
            private final Button btnCambiarEstado = new Button("🔄");
            private final Button btnEliminar = new Button("🗑");

            {
                btnVer.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-cursor: hand;");
                btnCambiarEstado.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand;");
                btnEliminar.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");

                btnVer.setOnAction(e -> {
                    RepartidorSimpleDTO rep = getTableView().getItems().get(getIndex());
                    mostrarDetalleRepartidor(rep);
                });

                btnCambiarEstado.setOnAction(e -> {
                    RepartidorSimpleDTO rep = getTableView().getItems().get(getIndex());
                    cambiarDisponibilidadRepartidor(rep);
                });

                btnEliminar.setOnAction(e -> {
                    RepartidorSimpleDTO rep = getTableView().getItems().get(getIndex());
                    eliminarRepartidor(rep);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox box = new javafx.scene.layout.HBox(5, btnVer, btnCambiarEstado, btnEliminar);
                    setGraphic(box);
                }
            }
        });
    }

    private void configurarTablasEnvios() {
        colEnvioId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdEnvio()));
        colEnvioUsuario.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getUsuario().getNombre()));
        colEnvioRepartidor.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRepartidorNombre()));
        colEnvioOrigen.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getOrigen().getMunicipio()));
        colEnvioDestino.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDestino().getMunicipio()));
        colEnvioEstado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEstado()));

        colEnvioAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnAsignar = new Button("👤");
            private final Button btnCambiarEstado = new Button("🔄");
            private final Button btnCancelar = new Button("❌");

            {
                btnAsignar.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand;");
                btnCambiarEstado.setStyle("-fx-background-color: #667eea; -fx-text-fill: white; -fx-cursor: hand;");
                btnCancelar.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");

                btnAsignar.setOnAction(e -> {
                    EnvioDetalleDTO envio = getTableView().getItems().get(getIndex());
                    asignarRepartidorAEnvio(envio);
                });

                btnCambiarEstado.setOnAction(e -> {
                    EnvioDetalleDTO envio = getTableView().getItems().get(getIndex());
                    cambiarEstadoEnvio(envio);
                });

                btnCancelar.setOnAction(e -> {
                    EnvioDetalleDTO envio = getTableView().getItems().get(getIndex());
                    cancelarEnvio(envio);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    javafx.scene.layout.HBox box = new javafx.scene.layout.HBox(5, btnAsignar, btnCambiarEstado, btnCancelar);
                    setGraphic(box);
                }
            }
        });
    }

    private void configurarFiltros() {
        cmbFiltroEstadoEnvio.setItems(FXCollections.observableArrayList(EstadoEnvio.values()));
        cmbFiltroEstadoEnvio.setPromptText("Todos los estados");
    }

    // ========== CARGA DE DATOS ==========

    private void cargarDatosIniciales() {
        actualizarTablaUsuarios();
        actualizarTablaRepartidores();
        actualizarTablaEnvios();
        actualizarMetricas();
    }

    @FXML
    private void handleActualizarUsuarios(ActionEvent event) {
        actualizarTablaUsuarios();
        AlertHelper.mostrarInfo("Actualizado", "Lista de usuarios actualizada");
    }

    private void actualizarTablaUsuarios() {
        try {
            List<Usuario> usuarios = adminService.listarUsuarios();
            List<UsuarioSimpleDTO> usuariosDTO = usuarios.stream()
                    .map(DTOAdapter::toDTO)
                    .collect(Collectors.toList());

            tableUsuarios.setItems(FXCollections.observableArrayList(usuariosDTO));
        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudieron cargar los usuarios: " + e.getMessage());
        }
    }

    @FXML
    private void handleActualizarRepartidores(ActionEvent event) {
        actualizarTablaRepartidores();
        AlertHelper.mostrarInfo("Actualizado", "Lista de repartidores actualizada");
    }

    private void actualizarTablaRepartidores() {
        try {
            List<Repartidor> repartidores = adminService.listarRepartidores();
            List<RepartidorSimpleDTO> repartidoresDTO = repartidores.stream()
                    .map(DTOAdapter::toDTO)
                    .collect(Collectors.toList());

            tableRepartidores.setItems(FXCollections.observableArrayList(repartidoresDTO));
        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudieron cargar los repartidores: " + e.getMessage());
        }
    }

    @FXML
    private void handleActualizarEnvios(ActionEvent event) {
        actualizarTablaEnvios();
        AlertHelper.mostrarInfo("Actualizado", "Lista de envíos actualizada");
    }

    private void actualizarTablaEnvios() {
        try {
            List<Envio> envios = adminService.listarEnvios();
            List<EnvioDetalleDTO> enviosDTO = envios.stream()
                    .map(DTOAdapter::toDTO)
                    .collect(Collectors.toList());

            tableEnvios.setItems(FXCollections.observableArrayList(enviosDTO));
        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudieron cargar los envíos: " + e.getMessage());
        }
    }

    @FXML
    private void handleFiltrarEnvios(ActionEvent event) {
        EstadoEnvio estadoSeleccionado = cmbFiltroEstadoEnvio.getValue();

        try {
            List<Envio> envios;
            if (estadoSeleccionado == null) {
                envios = adminService.listarEnvios();
            } else {
                envios = envioService.filtrarPorEstado(estadoSeleccionado);
            }

            List<EnvioDetalleDTO> enviosDTO = envios.stream()
                    .map(DTOAdapter::toDTO)
                    .collect(Collectors.toList());

            tableEnvios.setItems(FXCollections.observableArrayList(enviosDTO));
        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudieron filtrar los envíos: " + e.getMessage());
        }
    }

    // ========== GESTIÓN DE USUARIOS ==========

    private void mostrarDetalleUsuario(UsuarioSimpleDTO usuarioDTO) {
        try {
            Usuario usuario = adminService.consultarUsuario(usuarioDTO.getId());

            StringBuilder detalles = new StringBuilder();
            detalles.append("ID: ").append(usuario.getId()).append("\n");
            detalles.append("Nombre: ").append(usuario.getNombre()).append("\n");
            detalles.append("Correo: ").append(usuario.getCorreo()).append("\n");
            detalles.append("Teléfono: ").append(usuario.getTelefono()).append("\n");
            detalles.append("Usuario: ").append(usuario.getUsuario()).append("\n\n");

            detalles.append("Direcciones registradas: ").append(usuario.getDirecciones().size()).append("\n");
            detalles.append("Métodos de pago: ").append(usuario.getMetodosPago().size()).append("\n");
            detalles.append("Total de envíos: ").append(usuario.getEnvios().size()).append("\n");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Detalles del Usuario");
            alert.setHeaderText("Información completa");
            alert.setContentText(detalles.toString());
            alert.getDialogPane().setPrefWidth(450);
            alert.showAndWait();

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudo obtener el detalle: " + e.getMessage());
        }
    }

    private void eliminarUsuario(UsuarioSimpleDTO usuarioDTO) {
        boolean confirmar = AlertHelper.mostrarConfirmacion(
                "Eliminar Usuario",
                "¿Estás seguro de que deseas eliminar al usuario " + usuarioDTO.getNombre() + "?\n\n" +
                        "Esta acción no se puede deshacer y solo es posible si no tiene envíos activos."
        );

        if (confirmar) {
            try {
                adminService.eliminarUsuario(usuarioDTO.getId());
                AlertHelper.mostrarExito("Eliminado", "Usuario eliminado exitosamente");
                actualizarTablaUsuarios();
                actualizarMetricas();
            } catch (Exception e) {
                AlertHelper.mostrarError("Error", e.getMessage());
            }
        }
    }

    // ========== GESTIÓN DE REPARTIDORES ==========

    @FXML
    private void handleNuevoRepartidor(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Repartidor");
        dialog.setHeaderText("Registrar nuevo repartidor en el sistema");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre completo");

        TextField txtDocumento = new TextField();
        txtDocumento.setPromptText("Documento de identidad");

        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("Teléfono (10 dígitos)");

        TextField txtCorreo = new TextField();
        txtCorreo.setPromptText("correo@ejemplo.com");

        TextField txtUsuario = new TextField();
        txtUsuario.setPromptText("Usuario para login");

        PasswordField txtContrasena = new PasswordField();
        txtContrasena.setPromptText("Contraseña");

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(txtNombre, 1, 0);
        grid.add(new Label("Documento:"), 0, 1);
        grid.add(txtDocumento, 1, 1);
        grid.add(new Label("Teléfono:"), 0, 2);
        grid.add(txtTelefono, 1, 2);
        grid.add(new Label("Correo:"), 0, 3);
        grid.add(txtCorreo, 1, 3);
        grid.add(new Label("Usuario:"), 0, 4);
        grid.add(txtUsuario, 1, 4);
        grid.add(new Label("Contraseña:"), 0, 5);
        grid.add(txtContrasena, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String nombre = txtNombre.getText().trim();
                String documento = txtDocumento.getText().trim();
                String telefono = txtTelefono.getText().trim();
                String correo = txtCorreo.getText().trim();
                String usuario = txtUsuario.getText().trim();
                String contrasena = txtContrasena.getText();

                // Validaciones
                if (!Validador.validarTextoNoVacio(nombre) ||
                        !Validador.validarDocumento(documento) ||
                        !Validador.validarTelefono(telefono) ||
                        !Validador.validarEmail(correo) ||
                        !Validador.validarUsuarioYContrasena(usuario) ||
                        !Validador.validarUsuarioYContrasena(contrasena)) {
                    AlertHelper.mostrarError("Datos Inválidos", "Por favor, completa todos los campos correctamente");
                    return;
                }

                repartidorService.crearRepartidor(nombre, documento, telefono, correo, usuario, contrasena);
                AlertHelper.mostrarExito("Repartidor Creado", "El repartidor se registró exitosamente");
                actualizarTablaRepartidores();
                actualizarMetricas();

            } catch (Exception e) {
                AlertHelper.mostrarError("Error", e.getMessage());
            }
        }
    }

    private void mostrarDetalleRepartidor(RepartidorSimpleDTO repartidorDTO) {
        try {
            Repartidor repartidor = adminService.consultarRepartidor(repartidorDTO.getId());

            StringBuilder detalles = new StringBuilder();
            detalles.append("ID: ").append(repartidor.getId()).append("\n");
            detalles.append("Nombre: ").append(repartidor.getNombre()).append("\n");
            detalles.append("Documento: ").append(repartidor.getDocumento()).append("\n");
            detalles.append("Teléfono: ").append(repartidor.getTelefono()).append("\n");
            detalles.append("Correo: ").append(repartidor.getCorreo()).append("\n");
            detalles.append("Estado: ").append(repartidor.getEstadoDisponibilidad()).append("\n\n");

            detalles.append("Zonas de cobertura: ").append(repartidor.getZonaCobertura().size()).append("\n");
            detalles.append("Envíos asignados: ").append(repartidor.getEnviosAsignados().size()).append("\n");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Detalles del Repartidor");
            alert.setHeaderText("Información completa");
            alert.setContentText(detalles.toString());
            alert.getDialogPane().setPrefWidth(450);
            alert.showAndWait();

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudo obtener el detalle: " + e.getMessage());
        }
    }

    private void cambiarDisponibilidadRepartidor(RepartidorSimpleDTO repartidorDTO) {
        ChoiceDialog<EstadoDisponibilidad> dialog = new ChoiceDialog<>(
                EstadoDisponibilidad.ACTIVO,
                EstadoDisponibilidad.values()
        );
        dialog.setTitle("Cambiar Disponibilidad");
        dialog.setHeaderText("Cambiar estado del repartidor: " + repartidorDTO.getNombre());
        dialog.setContentText("Nuevo estado:");

        Optional<EstadoDisponibilidad> result = dialog.showAndWait();
        result.ifPresent(nuevoEstado -> {
            try {
                repartidorService.cambiarDisponibilidad(repartidorDTO.getId(), nuevoEstado);
                AlertHelper.mostrarExito("Estado Actualizado",
                        "El estado del repartidor se actualizó a: " + nuevoEstado);
                actualizarTablaRepartidores();
            } catch (Exception e) {
                AlertHelper.mostrarError("Error", e.getMessage());
            }
        });
    }

    private void eliminarRepartidor(RepartidorSimpleDTO repartidorDTO) {
        boolean confirmar = AlertHelper.mostrarConfirmacion(
                "Eliminar Repartidor",
                "¿Estás seguro de que deseas eliminar al repartidor " + repartidorDTO.getNombre() + "?\n\n" +
                        "Esta acción solo es posible si no tiene envíos activos asignados."
        );

        if (confirmar) {
            try {
                adminService.eliminarRepartidor(repartidorDTO.getId());
                AlertHelper.mostrarExito("Eliminado", "Repartidor eliminado exitosamente");
                actualizarTablaRepartidores();
                actualizarMetricas();
            } catch (Exception e) {
                AlertHelper.mostrarError("Error", e.getMessage());
            }
        }
    }

    // ========== GESTIÓN DE ENVÍOS ==========

    private void asignarRepartidorAEnvio(EnvioDetalleDTO envioDTO) {
        try {
            List<Repartidor> repartidoresDisponibles = repartidorService.listarDisponibles();

            if (repartidoresDisponibles.isEmpty()) {
                AlertHelper.mostrarAdvertencia("Sin Repartidores",
                        "No hay repartidores disponibles en este momento");
                return;
            }

            ChoiceDialog<Repartidor> dialog = new ChoiceDialog<>(
                    repartidoresDisponibles.get(0),
                    repartidoresDisponibles
            );
            dialog.setTitle("Asignar Repartidor");
            dialog.setHeaderText("Asignar repartidor al envío: " + envioDTO.getIdEnvio());
            dialog.setContentText("Selecciona repartidor:");

            Optional<Repartidor> result = dialog.showAndWait();
            result.ifPresent(repartidor -> {
                try {
                    repartidorService.asignarEnvio(repartidor.getId(), envioDTO.getIdEnvio());
                    AlertHelper.mostrarExito("Asignado",
                            "Envío asignado exitosamente a " + repartidor.getNombre());
                    actualizarTablaEnvios();
                } catch (Exception e) {
                    AlertHelper.mostrarError("Error", e.getMessage());
                }
            });

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudo asignar el repartidor: " + e.getMessage());
        }
    }

    private void cambiarEstadoEnvio(EnvioDetalleDTO envioDTO) {
        ChoiceDialog<EstadoEnvio> dialog = new ChoiceDialog<>(
                EstadoEnvio.ASIGNADO,
                EstadoEnvio.values()
        );
        dialog.setTitle("Cambiar Estado");
        dialog.setHeaderText("Cambiar estado del envío: " + envioDTO.getIdEnvio());
        dialog.setContentText("Nuevo estado:");

        Optional<EstadoEnvio> result = dialog.showAndWait();
        result.ifPresent(nuevoEstado -> {
            try {
                envioService.actualizarEstado(envioDTO.getIdEnvio(), nuevoEstado);
                AlertHelper.mostrarExito("Estado Actualizado",
                        "El estado del envío se actualizó a: " + nuevoEstado);
                actualizarTablaEnvios();
                actualizarMetricas();
            } catch (Exception e) {
                AlertHelper.mostrarError("Error", e.getMessage());
            }
        });
    }

    private void cancelarEnvio(EnvioDetalleDTO envioDTO) {
        boolean confirmar = AlertHelper.mostrarConfirmacion(
                "Cancelar Envío",
                "¿Estás seguro de que deseas cancelar el envío " + envioDTO.getIdEnvio() + "?\n\n" +
                        "Esta acción no se puede deshacer."
        );

        if (confirmar) {
            try {
                adminService.cancelarEnvio(envioDTO.getIdEnvio());
                AlertHelper.mostrarExito("Cancelado", "Envío cancelado exitosamente");
                actualizarTablaEnvios();
                actualizarMetricas();
            } catch (Exception e) {
                AlertHelper.mostrarError("Error", e.getMessage());
            }
        }
    }

    // ========== MÉTRICAS ==========

    @FXML
    private void handleActualizarMetricas(ActionEvent event) {
        actualizarMetricas();
        AlertHelper.mostrarExito("Actualizado", "Métricas actualizadas correctamente");
    }

    private void actualizarMetricas() {
        try {
            // Obtener estadísticas generales
            Map<String, Object> estadisticas = adminService.obtenerEstadisticas();

            // Actualizar estadísticas generales
            lblTotalUsuarios.setText(estadisticas.get("totalUsuarios").toString());
            lblTotalRepartidores.setText(estadisticas.get("totalRepartidores").toString());
            lblTotalEnvios.setText(estadisticas.get("totalEnvios").toString());

            double ingresos = (double) estadisticas.get("ingresosGenerados");
            lblIngresosTotales.setText(String.format("$%,.2f", ingresos));

            // Actualizar envíos por estado
            lblEnviosSolicitados.setText(String.valueOf(
                    contarEnviosPorEstado(EstadoEnvio.SOLICITADO)));
            lblEnviosAsignados.setText(String.valueOf(
                    contarEnviosPorEstado(EstadoEnvio.ASIGNADO)));
            lblEnviosEnRuta.setText(String.valueOf(
                    contarEnviosPorEstado(EstadoEnvio.EN_RUTA)));
            lblEnviosEntregados.setText(String.valueOf(
                    contarEnviosPorEstado(EstadoEnvio.ENTREGADO)));
            lblEnviosIncidencias.setText(String.valueOf(
                    contarEnviosPorEstado(EstadoEnvio.INCIDENCIA)));

            // Actualizar top repartidores
            actualizarTopRepartidores();

            // Actualizar top usuarios
            actualizarTopUsuarios();

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudieron actualizar las métricas: " + e.getMessage());
        }
    }

    private long contarEnviosPorEstado(EstadoEnvio estado) {
        try {
            return envioService.filtrarPorEstado(estado).size();
        } catch (Exception e) {
            return 0;
        }
    }

    private void actualizarTopRepartidores() {
        try {
            List<Repartidor> topRepartidores = adminService.obtenerTopRepartidores(5);

            List<String> items = new ArrayList<>();
            int posicion = 1;
            for (Repartidor rep : topRepartidores) {
                long entregas = rep.getEnviosAsignados().stream()
                        .filter(e -> e.getEstado() == EstadoEnvio.ENTREGADO)
                        .count();

                String item = String.format("%d. %s - %d entregas",
                        posicion++, rep.getNombre(), entregas);
                items.add(item);
            }

            if (items.isEmpty()) {
                items.add("No hay repartidores registrados");
            }

            listTopRepartidores.setItems(FXCollections.observableArrayList(items));

        } catch (Exception e) {
            listTopRepartidores.setItems(FXCollections.observableArrayList(
                    "Error al cargar datos"
            ));
        }
    }

    private void actualizarTopUsuarios() {
        try {
            List<Usuario> topUsuarios = adminService.obtenerUsuariosMasActivos(5);

            List<String> items = new ArrayList<>();
            int posicion = 1;
            for (Usuario usuario : topUsuarios) {
                String item = String.format("%d. %s - %d envíos",
                        posicion++, usuario.getNombre(), usuario.getEnvios().size());
                items.add(item);
            }

            if (items.isEmpty()) {
                items.add("No hay usuarios registrados");
            }

            listTopUsuarios.setItems(FXCollections.observableArrayList(items));

        } catch (Exception e) {
            listTopUsuarios.setItems(FXCollections.observableArrayList(
                    "Error al cargar datos"
            ));
        }
    }

    // ========== CERRAR SESIÓN ==========

    @FXML
    private void handleCerrarSesion(ActionEvent event) {
        boolean confirmar = AlertHelper.mostrarConfirmacion(
                "Cerrar Sesión",
                "¿Estás seguro de que deseas cerrar sesión?"
        );

        if (confirmar) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/co/edu/uniquindio/fx10/proyectofinals2/Login.fxml")
                );
                Parent root = loader.load();

                Stage stage = (Stage) lblBienvenida.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("RapponCho - Login");
                stage.centerOnScreen();

                System.out.println("🚪 Administrador cerró sesión: " + adminActual.getNombre());

            } catch (Exception e) {
                AlertHelper.mostrarError("Error", "No se pudo cerrar sesión: " + e.getMessage());
            }
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Formatea un número para mostrar en la UI
     */
    private String formatearNumero(int numero) {
        return String.format("%,d", numero);
    }

    /**
     * Formatea una cantidad de dinero
     */
    private String formatearDinero(double cantidad) {
        return String.format("$%,.2f", cantidad);
    }

    /**
     * Obtiene el color de estado según el estado del envío
     */
    private String obtenerColorEstado(EstadoEnvio estado) {
        return switch (estado) {
            case SOLICITADO -> "#94a3b8";
            case ASIGNADO -> "#3b82f6";
            case EN_RUTA -> "#f59e0b";
            case ENTREGADO -> "#10b981";
            case INCIDENCIA -> "#ef4444";
        };
    }

    /**
     * Genera un resumen de estadísticas para exportar
     */
    public String generarResumenEstadisticas() {
        try {
            Map<String, Object> stats = adminService.obtenerEstadisticas();

            StringBuilder resumen = new StringBuilder();
            resumen.append("═══════════════════════════════════════\n");
            resumen.append("     RESUMEN DE ESTADÍSTICAS\n");
            resumen.append("═══════════════════════════════════════\n\n");

            resumen.append("GENERAL:\n");
            resumen.append("  • Total de Usuarios: ").append(stats.get("totalUsuarios")).append("\n");
            resumen.append("  • Total de Repartidores: ").append(stats.get("totalRepartidores")).append("\n");
            resumen.append("  • Total de Envíos: ").append(stats.get("totalEnvios")).append("\n");
            resumen.append("  • Ingresos Generados: $").append(
                    String.format("%,.2f", (double) stats.get("ingresosGenerados"))).append("\n\n");

            resumen.append("ENVÍOS POR ESTADO:\n");
            resumen.append("  • Solicitados: ").append(contarEnviosPorEstado(EstadoEnvio.SOLICITADO)).append("\n");
            resumen.append("  • Asignados: ").append(contarEnviosPorEstado(EstadoEnvio.ASIGNADO)).append("\n");
            resumen.append("  • En Ruta: ").append(contarEnviosPorEstado(EstadoEnvio.EN_RUTA)).append("\n");
            resumen.append("  • Entregados: ").append(contarEnviosPorEstado(EstadoEnvio.ENTREGADO)).append("\n");
            resumen.append("  • Con Incidencia: ").append(contarEnviosPorEstado(EstadoEnvio.INCIDENCIA)).append("\n\n");

            resumen.append("TOP 5 REPARTIDORES:\n");
            List<Repartidor> topReps = adminService.obtenerTopRepartidores(5);
            int pos = 1;
            for (Repartidor rep : topReps) {
                long entregas = rep.getEnviosAsignados().stream()
                        .filter(e -> e.getEstado() == EstadoEnvio.ENTREGADO)
                        .count();
                resumen.append(String.format("  %d. %s - %d entregas\n", pos++, rep.getNombre(), entregas));
            }

            resumen.append("\nTOP 5 USUARIOS ACTIVOS:\n");
            List<Usuario> topUsers = adminService.obtenerUsuariosMasActivos(5);
            pos = 1;
            for (Usuario user : topUsers) {
                resumen.append(String.format("  %d. %s - %d envíos\n",
                        pos++, user.getNombre(), user.getEnvios().size()));
            }

            resumen.append("\n═══════════════════════════════════════\n");
            resumen.append("Generado: ").append(java.time.LocalDateTime.now()).append("\n");
            resumen.append("═══════════════════════════════════════\n");

            return resumen.toString();

        } catch (Exception e) {
            return "Error al generar resumen: " + e.getMessage();
        }
    }

    /**
     * Imprime estadísticas en consola (útil para debugging)
     */
    public void imprimirEstadisticasEnConsola() {
        System.out.println(generarResumenEstadisticas());
    }

    /**
     * Valida que el administrador tenga permisos para realizar una acción
     * (En una implementación real, aquí se verificarían roles y permisos)
     */
    private boolean validarPermisos(String accion) {
        if (adminActual == null) {
            AlertHelper.mostrarNoAutorizado();
            return false;
        }
        return true;
    }

    /**
     * Registra una acción del administrador en el log
     */
    private void registrarAccion(String accion, String detalles) {
        System.out.println(String.format(
                "[ADMIN] %s - %s: %s",
                java.time.LocalDateTime.now(),
                adminActual.getNombre(),
                accion
        ));
        if (detalles != null && !detalles.isEmpty()) {
            System.out.println("  Detalles: " + detalles);
        }
    }

    /**
     * Calcula el porcentaje de envíos entregados exitosamente
     */
    private double calcularTasaExito() {
        try {
            long totalEnvios = adminService.listarEnvios().size();
            if (totalEnvios == 0) return 0.0;

            long entregados = contarEnviosPorEstado(EstadoEnvio.ENTREGADO);
            return (entregados * 100.0) / totalEnvios;

        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Obtiene el tiempo promedio de entrega (simulado)
     */
    private double calcularTiempoPromedioEntrega() {
        // En una implementación real, esto calcularía el tiempo real
        // entre fechaCreacion y fechaEntrega de los envíos completados
        return 4.5; // Horas simuladas
    }

    /**
     * Genera alertas automáticas para el administrador
     */
    private List<String> generarAlertasAutomaticas() {
        List<String> alertas = new ArrayList<>();

        try {
            // Alerta de repartidores insuficientes
            List<Repartidor> repartidoresActivos = repartidorService.listarDisponibles();
            if (repartidoresActivos.size() < 3) {
                alertas.add("⚠ Pocos repartidores disponibles (" + repartidoresActivos.size() + ")");
            }

            // Alerta de envíos con incidencias
            long incidencias = contarEnviosPorEstado(EstadoEnvio.INCIDENCIA);
            if (incidencias > 0) {
                alertas.add("⚠ Hay " + incidencias + " envío(s) con incidencias");
            }

            // Alerta de envíos pendientes de asignar
            long sinAsignar = contarEnviosPorEstado(EstadoEnvio.SOLICITADO);
            if (sinAsignar > 5) {
                alertas.add("⚠ Hay " + sinAsignar + " envíos sin asignar");
            }

        } catch (Exception e) {
            alertas.add("Error al generar alertas");
        }

        return alertas;
    }

    /**
     * Muestra notificaciones importantes al administrador
     */
    private void mostrarNotificacionesImportantes() {
        List<String> alertas = generarAlertasAutomaticas();

        if (!alertas.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Notificaciones del sistema:\n\n");
            for (String alerta : alertas) {
                mensaje.append("• ").append(alerta).append("\n");
            }

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Notificaciones");
            alert.setHeaderText("Atención requerida");
            alert.setContentText(mensaje.toString());
            alert.show(); // No bloqueante
        }
    }

    /**
     * Exporta datos a consola para debugging
     */
    public void exportarDatosDebug() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEBUG: Estado actual del sistema");
        System.out.println("=".repeat(60));

        try {
            System.out.println("\n📊 ESTADÍSTICAS:");
            Map<String, Object> stats = adminService.obtenerEstadisticas();
            stats.forEach((key, value) ->
                    System.out.println("  " + key + ": " + value));

            System.out.println("\n👥 USUARIOS:");
            adminService.listarUsuarios().forEach(u ->
                    System.out.println("  - " + u.getNombre() + " (" + u.getId() + ")"));

            System.out.println("\n🚚 REPARTIDORES:");
            adminService.listarRepartidores().forEach(r ->
                    System.out.println("  - " + r.getNombre() + " [" + r.getEstadoDisponibilidad() + "]"));

            System.out.println("\n📦 ENVÍOS RECIENTES:");
            adminService.listarEnvios().stream()
                    .limit(5)
                    .forEach(e -> System.out.println(
                            "  - " + e.getIdEnvio() + " [" + e.getEstado() + "]"));

        } catch (Exception e) {
            System.err.println("Error en exportación debug: " + e.getMessage());
        }

        System.out.println("\n" + "=".repeat(60) + "\n");
    }

    }