package co.edu.uniquindio.rapponcho.controllers;

import co.edu.uniquindio.rapponcho.dataTransferObjects.*;
import co.edu.uniquindio.rapponcho.model.*;
import co.edu.uniquindio.rapponcho.model.AdapterDTO.DTOAdapter;
import co.edu.uniquindio.rapponcho.reposytorie.Repositorio;
import co.edu.uniquindio.rapponcho.services.*;
import co.edu.uniquindio.rapponcho.utils.AlertHelper;
import co.edu.uniquindio.rapponcho.utils.GeneradorID;
import co.edu.uniquindio.rapponcho.utils.Validador;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador principal para el panel del Repartidor
 * Gestiona envíos, zonas de cobertura, incidencias y perfil
 */
public class RepartidorMainController {

    @FXML private Label lblBienvenida;
    @FXML private Label lblEstadoActual;
    @FXML private Button btnCambiarEstado;
    @FXML private Label lblEnviosActuales;
    @FXML private Label lblCapacidadMaxima;
    @FXML private ProgressBar progressCapacidad;

    @FXML private ComboBox<EstadoEnvio> cmbFiltroEstado;
    @FXML private TableView<EnvioDetalleDTO> tableEnvios;
    @FXML private TableColumn<EnvioDetalleDTO, String> colIdEnvio;
    @FXML private TableColumn<EnvioDetalleDTO, String> colUsuario;
    @FXML private TableColumn<EnvioDetalleDTO, String> colOrigen;
    @FXML private TableColumn<EnvioDetalleDTO, String> colDestino;
    @FXML private TableColumn<EnvioDetalleDTO, String> colEstado;
    @FXML private TableColumn<EnvioDetalleDTO, String> colFecha;
    @FXML private TableColumn<EnvioDetalleDTO, Void> colAcciones;
    @FXML private Label lblPendientes;
    @FXML private Label lblEnRuta;
    @FXML private Label lblEntregadosHoy;
    @FXML private Label lblTotalEntregados;

    @FXML private VBox vboxZonas;
    @FXML private GridPane gridZonasDisponibles;

    @FXML private ComboBox<String> cmbEnvioIncidencia;
    @FXML private ComboBox<String> cmbTipoIncidencia;
    @FXML private TextArea txtDescripcionIncidencia;
    @FXML private ListView<String> listIncidencias;

    @FXML private Label lblPerfilId;
    @FXML private TextField txtPerfilNombre;
    @FXML private Label lblPerfilDocumento;
    @FXML private TextField txtPerfilTelefono;
    @FXML private TextField txtPerfilCorreo;
    @FXML private Label lblEstadTotalEntregas;
    @FXML private Label lblEstadTasaExito;
    @FXML private Label lblEstadIncidencias;
    @FXML private Label lblEstadActivos;
    @FXML private Slider sliderCapacidad;
    @FXML private Label lblCapacidadSeleccionada;
    @FXML private CheckBox chkAsignacionAutomatica;

    private final RepartidorService repartidorService;
    private final EnvioService envioService;
    private final Repositorio repositorio;

    private Repartidor repartidorActual;
    private int capacidadMaxima = 10;
    private boolean asignacionAutomatica = true;
    private Timeline autoRefreshTimeline;

    public RepartidorMainController() {
        this.repartidorService = new RepartidorService();
        this.envioService = new EnvioService();
        this.repositorio = Repositorio.getInstancia();
    }

    @FXML
    public void initialize() {
        configurarTablasEnvios();
        configurarFiltros();
        configurarSliders();
        configurarComboIncidencias();
    }

    public void setRepartidor(Repartidor repartidor) {
        this.repartidorActual = repartidor;
        lblBienvenida.setText("Bienvenido, " + repartidor.getNombre());
        cargarDatosIniciales();
        iniciarActualizacionAutomatica();
    }


    private void configurarTablasEnvios() {
        colIdEnvio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdEnvio()));
        colUsuario.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getUsuario().getNombre()));
        colOrigen.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getOrigen().getDireccionCompleta()));
        colDestino.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDestino().getDireccionCompleta()));
        colEstado.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getEstado()));
        colFecha.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFechaCreacion()));

        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnIniciar = new Button("🚀 Iniciar");
            private final Button btnEntregar = new Button("✅ Entregar");
            private final Button btnIncidencia = new Button("⚠");

            {
                btnIniciar.setStyle("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");
                btnEntregar.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");
                btnIncidencia.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");

                btnIniciar.setOnAction(e -> {
                    EnvioDetalleDTO envio = getTableView().getItems().get(getIndex());
                    iniciarEntrega(envio);
                });

                btnEntregar.setOnAction(e -> {
                    EnvioDetalleDTO envio = getTableView().getItems().get(getIndex());
                    entregarPaquete(envio);
                });

                btnIncidencia.setOnAction(e -> {
                    EnvioDetalleDTO envio = getTableView().getItems().get(getIndex());
                    reportarIncidenciaRapida(envio);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    EnvioDetalleDTO envio = getTableView().getItems().get(getIndex());
                    HBox box = new HBox(5);

                    // Mostrar botones según el estado
                    if ("ASIGNADO".equals(envio.getEstado())) {
                        box.getChildren().addAll(btnIniciar, btnIncidencia);
                    } else if ("EN_RUTA".equals(envio.getEstado())) {
                        box.getChildren().addAll(btnEntregar, btnIncidencia);
                    } else {
                        box.getChildren().add(new Label("✓"));
                    }

                    setGraphic(box);
                }
            }
        });
    }

    private void configurarFiltros() {
        List<EstadoEnvio> estados = Arrays.asList(
                EstadoEnvio.ASIGNADO,
                EstadoEnvio.EN_RUTA,
                EstadoEnvio.ENTREGADO,
                EstadoEnvio.INCIDENCIA
        );
        cmbFiltroEstado.setItems(FXCollections.observableArrayList(estados));
    }

    private void configurarSliders() {
        sliderCapacidad.valueProperty().addListener((obs, oldVal, newVal) -> {
            int capacidad = newVal.intValue();
            lblCapacidadSeleccionada.setText(capacidad + " envíos");
        });
    }

    private void configurarComboIncidencias() {
        List<String> tiposIncidencia = Arrays.asList(
                "Dirección incorrecta",
                "Cliente no disponible",
                "Paquete dañado",
                "Acceso restringido",
                "Condiciones climáticas",
                "Problema vehicular",
                "Otro"
        );
        cmbTipoIncidencia.setItems(FXCollections.observableArrayList(tiposIncidencia));
    }

    // ========== CARGA DE DATOS ==========

    private void cargarDatosIniciales() {
        actualizarEstadoHeader();
        actualizarTablaEnvios();
        cargarZonasCobertura();
        cargarIncidencias();
        cargarPerfil();
        actualizarEstadisticas();
    }

    private void actualizarEstadoHeader() {
        lblEstadoActual.setText(repartidorActual.getEstadoDisponibilidad().name());

        // Cambiar color según estado
        String color = switch (repartidorActual.getEstadoDisponibilidad()) {
            case ACTIVO -> "#10b981";
            case EN_RUTA -> "#f59e0b";
            case INACTIVO -> "#ef4444";
        };
        lblEstadoActual.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");

        // Actualizar capacidad
        int enviosActivos = (int) repartidorActual.getEnviosAsignados().stream()
                .filter(e -> e.getEstado() != EstadoEnvio.ENTREGADO)
                .count();

        lblEnviosActuales.setText(String.valueOf(enviosActivos));
        lblCapacidadMaxima.setText(String.valueOf(capacidadMaxima));
        progressCapacidad.setProgress((double) enviosActivos / capacidadMaxima);

        // Cambiar color de la barra según capacidad
        if (enviosActivos >= capacidadMaxima) {
            progressCapacidad.setStyle("-fx-accent: #ef4444;");
        } else if (enviosActivos >= capacidadMaxima * 0.7) {
            progressCapacidad.setStyle("-fx-accent: #f59e0b;");
        } else {
            progressCapacidad.setStyle("-fx-accent: #10b981;");
        }
    }

    @FXML
    private void handleActualizarEnvios(ActionEvent event) {
        actualizarTablaEnvios();
        AlertHelper.mostrarInfo("Actualizado", "Lista de envíos actualizada");
    }

    private void actualizarTablaEnvios() {
        try {
            List<Envio> envios = repartidorService.consultarEnviosAsignados(repartidorActual.getId());

            List<EnvioDetalleDTO> enviosDTO = envios.stream()
                    .map(DTOAdapter::toDTO)
                    .collect(Collectors.toList());

            tableEnvios.setItems(FXCollections.observableArrayList(enviosDTO));

            // Actualizar contadores
            actualizarContadores(envios);
            actualizarEstadoHeader();

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudieron cargar los envíos: " + e.getMessage());
        }
    }

    private void actualizarContadores(List<Envio> envios) {
        long pendientes = envios.stream()
                .filter(e -> e.getEstado() == EstadoEnvio.ASIGNADO)
                .count();

        long enRuta = envios.stream()
                .filter(e -> e.getEstado() == EstadoEnvio.EN_RUTA)
                .count();

        long entregadosHoy = envios.stream()
                .filter(e -> e.getEstado() == EstadoEnvio.ENTREGADO)
                .filter(e -> e.getFechaCreacion().toLocalDate().equals(LocalDateTime.now().toLocalDate()))
                .count();

        long totalEntregados = envios.stream()
                .filter(e -> e.getEstado() == EstadoEnvio.ENTREGADO)
                .count();

        lblPendientes.setText(String.valueOf(pendientes));
        lblEnRuta.setText(String.valueOf(enRuta));
        lblEntregadosHoy.setText(String.valueOf(entregadosHoy));
        lblTotalEntregados.setText(String.valueOf(totalEntregados));
    }

    @FXML
    private void handleFiltrar(ActionEvent event) {
        EstadoEnvio estadoSeleccionado = cmbFiltroEstado.getValue();

        try {
            List<Envio> envios = repartidorService.consultarEnviosAsignados(repartidorActual.getId());

            if (estadoSeleccionado != null) {
                envios = envios.stream()
                        .filter(e -> e.getEstado() == estadoSeleccionado)
                        .collect(Collectors.toList());
            }

            List<EnvioDetalleDTO> enviosDTO = envios.stream()
                    .map(DTOAdapter::toDTO)
                    .collect(Collectors.toList());

            tableEnvios.setItems(FXCollections.observableArrayList(enviosDTO));

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudieron filtrar los envíos");
        }
    }

    // ========== GESTIÓN DE ENVÍOS ==========

    @FXML
    private void handleBuscarNuevosEnvios(ActionEvent event) {
        // Verificar capacidad
        int enviosActivos = (int) repartidorActual.getEnviosAsignados().stream()
                .filter(e -> e.getEstado() != EstadoEnvio.ENTREGADO)
                .count();

        if (enviosActivos >= capacidadMaxima) {
            AlertHelper.mostrarAdvertencia("Capacidad Completa",
                    "Has alcanzado tu capacidad máxima de " + capacidadMaxima + " envíos.\n" +
                            "Completa algunos envíos antes de aceptar nuevos.");
            return;
        }

        // Verificar estado
        if (repartidorActual.getEstadoDisponibilidad() == EstadoDisponibilidad.INACTIVO) {
            AlertHelper.mostrarAdvertencia("Estado Inactivo",
                    "Debes cambiar tu estado a ACTIVO para buscar nuevos envíos.");
            return;
        }

        buscarYAsignarEnviosAutomaticamente();
    }

    private void buscarYAsignarEnviosAutomaticamente() {
        try {
            // Obtener envíos disponibles (SOLICITADO) en las zonas del repartidor
            List<Envio> enviosDisponibles = repositorio.getEnvios().values().stream()
                    .filter(e -> e.getEstado() == EstadoEnvio.SOLICITADO)
                    .filter(e -> e.getRepartidor() == null)
                    .filter(e -> perteneceAMisZonas(e))
                    .limit(capacidadMaxima - contarEnviosActivos())
                    .collect(Collectors.toList());

            if (enviosDisponibles.isEmpty()) {
                AlertHelper.mostrarInfo("Sin Envíos Disponibles",
                        "No hay envíos disponibles en tus zonas de cobertura en este momento.");
                return;
            }

            // Mostrar diálogo de selección
            Dialog<List<Envio>> dialog = new Dialog<>();
            dialog.setTitle("Nuevos Envíos Disponibles");
            dialog.setHeaderText("Se encontraron " + enviosDisponibles.size() + " envíos en tus zonas");

            VBox content = new VBox(10);
            content.setPadding(new Insets(20));

            List<CheckBox> checkboxes = new ArrayList<>();
            for (Envio envio : enviosDisponibles) {
                CheckBox cb = new CheckBox(String.format("%s - De %s a %s",
                        envio.getIdEnvio(),
                        envio.getOrigen().getMunicipio().name(),
                        envio.getDestino().getMunicipio().name()));
                cb.setUserData(envio);
                checkboxes.add(cb);
                content.getChildren().add(cb);
            }

            dialog.getDialogPane().setContent(new ScrollPane(content));
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(button -> {
                if (button == ButtonType.OK) {
                    return checkboxes.stream()
                            .filter(CheckBox::isSelected)
                            .map(cb -> (Envio) cb.getUserData())
                            .collect(Collectors.toList());
                }
                return null;
            });

            Optional<List<Envio>> result = dialog.showAndWait();
            result.ifPresent(enviosSeleccionados -> {
                if (!enviosSeleccionados.isEmpty()) {
                    asignarEnvios(enviosSeleccionados);
                }
            });

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudieron buscar envíos: " + e.getMessage());
        }
    }

    private boolean perteneceAMisZonas(Envio envio) {
        ZonaDireccion zonaDestino = envio.getDestino().getMunicipio();
        return repartidorActual.getZonaCobertura().stream()
                .anyMatch(z -> z.name().equals(zonaDestino.name()));
    }

    private int contarEnviosActivos() {
        return (int) repartidorActual.getEnviosAsignados().stream()
                .filter(e -> e.getEstado() != EstadoEnvio.ENTREGADO)
                .count();
    }

    private void asignarEnvios(List<Envio> envios) {
        try {
            int asignados = 0;
            for (Envio envio : envios) {
                repartidorService.asignarEnvio(repartidorActual.getId(), envio.getIdEnvio());
                asignados++;
            }

            AlertHelper.mostrarExito("Envíos Asignados",
                    "Se te asignaron " + asignados + " nuevos envíos exitosamente.");
            actualizarTablaEnvios();

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudieron asignar los envíos: " + e.getMessage());
        }
    }

    private void iniciarEntrega(EnvioDetalleDTO envioDTO) {
        boolean confirmar = AlertHelper.mostrarConfirmacion(
                "Iniciar Entrega",
                "¿Confirmas que vas a iniciar la entrega del envío " + envioDTO.getIdEnvio() + "?"
        );

        if (confirmar) {
            try {
                envioService.actualizarEstado(envioDTO.getIdEnvio(), EstadoEnvio.EN_RUTA);

                // Cambiar estado del repartidor si es necesario
                if (repartidorActual.getEstadoDisponibilidad() == EstadoDisponibilidad.ACTIVO) {
                    repartidorActual.setEstadoDisponibilidad(EstadoDisponibilidad.EN_RUTA);
                }

                AlertHelper.mostrarExito("En Ruta", "Envío marcado como EN RUTA");
                actualizarTablaEnvios();

            } catch (Exception e) {
                AlertHelper.mostrarError("Error", e.getMessage());
            }
        }
    }

    private void entregarPaquete(EnvioDetalleDTO envioDTO) {
        // Diálogo de confirmación con código de verificación simulado
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Confirmar Entrega");
        dialog.setHeaderText("Entrega del envío: " + envioDTO.getIdEnvio());
        dialog.setContentText("Código de verificación (simular):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(codigo -> {
            if (codigo.length() >= 4) {
                try {
                    envioService.actualizarEstado(envioDTO.getIdEnvio(), EstadoEnvio.ENTREGADO);
                    AlertHelper.mostrarExito("¡Entregado!",
                            "Envío marcado como ENTREGADO exitosamente.\n\n" +
                                    "Código de verificación: " + codigo);
                    actualizarTablaEnvios();
                    actualizarEstadisticas();

                } catch (Exception e) {
                    AlertHelper.mostrarError("Error", e.getMessage());
                }
            } else {
                AlertHelper.mostrarError("Código Inválido",
                        "El código debe tener al menos 4 caracteres");
            }
        });
    }

    // ========== GESTIÓN DE ESTADO ==========

    @FXML
    private void handleCambiarEstado(ActionEvent event) {
        ChoiceDialog<EstadoDisponibilidad> dialog = new ChoiceDialog<>(
                repartidorActual.getEstadoDisponibilidad(),
                EstadoDisponibilidad.values()
        );
        dialog.setTitle("Cambiar Estado");
        dialog.setHeaderText("Selecciona tu nuevo estado de disponibilidad");
        dialog.setContentText("Estado:");

        Optional<EstadoDisponibilidad> result = dialog.showAndWait();
        result.ifPresent(nuevoEstado -> {
            try {
                // Validar cambio de estado
                if (nuevoEstado == EstadoDisponibilidad.INACTIVO && contarEnviosActivos() > 0) {
                    AlertHelper.mostrarAdvertencia("Cambio no Permitido",
                            "No puedes cambiar a INACTIVO mientras tengas envíos activos.\n" +
                                    "Completa tus entregas primero.");
                    return;
                }

                repartidorService.cambiarDisponibilidad(repartidorActual.getId(), nuevoEstado);
                AlertHelper.mostrarExito("Estado Actualizado",
                        "Tu estado cambió a: " + nuevoEstado);
                actualizarEstadoHeader();

            } catch (Exception e) {
                AlertHelper.mostrarError("Error", e.getMessage());
            }
        });
    }

    // ========== GESTIÓN DE ZONAS ==========

    private void cargarZonasCobertura() {
        vboxZonas.getChildren().clear();

        for (ZonaCobertura zona : repartidorActual.getZonaCobertura()) {
            VBox zonaCard = crearTarjetaZona(zona);
            vboxZonas.getChildren().add(zonaCard);
        }

        if (repartidorActual.getZonaCobertura().isEmpty()) {
            Label lblSinZonas = new Label("No tienes zonas asignadas. Agrega al menos una zona para recibir envíos.");
            lblSinZonas.setStyle("-fx-text-fill: #64748b; -fx-font-style: italic;");
            vboxZonas.getChildren().add(lblSinZonas);
        }

        // Cargar grid de zonas disponibles
        cargarGridZonasDisponibles();
    }

    private VBox crearTarjetaZona(ZonaCobertura zona) {
        VBox card = new VBox(10);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-background-radius: 8;");

        HBox header = new HBox(10);
        header.setStyle("-fx-alignment: center-left;");

        Label lblNombre = new Label("📍 " + zona.name());
        lblNombre.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnEliminar = new Button("🗑");
        btnEliminar.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");
        btnEliminar.setOnAction(e -> eliminarZona(zona));

        header.getChildren().addAll(lblNombre, spacer, btnEliminar);

        // Estadísticas de la zona
        long enviosEnZona = repartidorActual.getEnviosAsignados().stream()
                .filter(env -> env.getDestino().getMunicipio().name().equals(zona.name()))
                .count();

        Label lblStats = new Label("Envíos entregados en esta zona: " + enviosEnZona);
        lblStats.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");

        card.getChildren().addAll(header, lblStats);
        return card;
    }

    private void cargarGridZonasDisponibles() {
        gridZonasDisponibles.getChildren().clear();

        ZonaDireccion[] todasLasZonas = ZonaDireccion.values();
        int col = 0;
        int row = 0;

        for (ZonaDireccion zona : todasLasZonas) {
            boolean yaAsignada = repartidorActual.getZonaCobertura().stream()
                    .anyMatch(z -> z.name().equals(zona.name()));

            Button btnZona = new Button(zona.name());
            btnZona.setPrefWidth(150);
            btnZona.setStyle(yaAsignada ?
                    "-fx-background-color: #10b981; -fx-text-fill: white;" :
                    "-fx-background-color: #e2e8f0; -fx-text-fill: #64748b;");

            if (!yaAsignada) {
                btnZona.setCursor(javafx.scene.Cursor.HAND);
                btnZona.setOnAction(e -> agregarZonaRapida(zona));
            }

            gridZonasDisponibles.add(btnZona, col, row);

            col++;
            if (col > 3) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void handleAgregarZona(ActionEvent event) {
        ChoiceDialog<ZonaDireccion> dialog = new ChoiceDialog<>(
                ZonaDireccion.ARMENIA,
                ZonaDireccion.values()
        );
        dialog.setTitle("Agregar Zona");
        dialog.setHeaderText("Selecciona una nueva zona de cobertura");
        dialog.setContentText("Zona:");

        Optional<ZonaDireccion> result = dialog.showAndWait();
        result.ifPresent(this::agregarZonaRapida);
    }

    private void agregarZonaRapida(ZonaDireccion zona) {
        try {
            // Convertir ZonaDireccion a ZonaCobertura
            ZonaCobertura zonaCobertura = ZonaCobertura.valueOf(zona.name());

            if (repartidorActual.getZonaCobertura().contains(zonaCobertura)) {
                AlertHelper.mostrarInfo("Ya Asignada", "Ya tienes esta zona en tu cobertura");
                return;
            }

            repartidorActual.getZonaCobertura().add(zonaCobertura);
            AlertHelper.mostrarExito("Zona Agregada", "Zona " + zona.name() + " agregada a tu cobertura");
            cargarZonasCobertura();

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudo agregar la zona: " + e.getMessage());
        }
    }

    private void eliminarZona(ZonaCobertura zona) {
        boolean confirmar = AlertHelper.mostrarConfirmacion(
                "Eliminar Zona",
                "¿Estás seguro de eliminar " + zona.name() + " de tu cobertura?"
        );

        if (confirmar) {
            repartidorActual.getZonaCobertura().remove(zona);
            AlertHelper.mostrarExito("Zona Eliminada", "Zona eliminada de tu cobertura");
            cargarZonasCobertura();
        }
    }

    // ========== GESTIÓN DE INCIDENCIAS ==========

    @FXML
    private void handleReportarIncidencia(ActionEvent event) {
        reportarIncidenciaCompleta();
    }

    @FXML
    private void handleRegistrarIncidencia(ActionEvent event) {
        String envioId = cmbEnvioIncidencia.getValue();
        String tipo = cmbTipoIncidencia.getValue();
        String descripcion = txtDescripcionIncidencia.getText().trim();

        if (envioId == null || tipo == null || descripcion.isEmpty()) {
            AlertHelper.mostrarError("Datos Incompletos",
                    "Por favor completa todos los campos");
            return;
        }

        try {
            Envio envio = repositorio.getEnvios().get(envioId);
            if (envio == null) {
                AlertHelper.mostrarError("Error", "Envío no encontrado");
                return;
            }

            String idIncidencia = GeneradorID.generarIDIncidencia();
            String descripcionCompleta = String.format("[%s] %s", tipo, descripcion);

            Incidencia incidencia = new Incidencia(
                    idIncidencia,
                    descripcionCompleta,
                    null,
                    envio
            );

            envio.agregarIncidencia(incidencia);
            envio.setEstado(EstadoEnvio.INCIDENCIA);

            AlertHelper.mostrarExito("Incidencia Registrada",
                    "La incidencia fue registrada con ID: " + idIncidencia);

            // Limpiar formulario
            cmbEnvioIncidencia.setValue(null);
            cmbTipoIncidencia.setValue(null);
            txtDescripcionIncidencia.clear();

            actualizarTablaEnvios();
            cargarIncidencias();

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudo registrar la incidencia: " + e.getMessage());
        }
    }

    private void reportarIncidenciaRapida(EnvioDetalleDTO envioDTO) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Reportar Incidencia");
        dialog.setHeaderText("Reportar incidencia para: " + envioDTO.getIdEnvio());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        ComboBox<String> cmbTipo = new ComboBox<>();
        cmbTipo.setItems(FXCollections.observableArrayList(
                "Dirección incorrecta",
                "Cliente no disponible",
                "Paquete dañado",
                "Acceso restringido",
                "Condiciones climáticas",
                "Problema vehicular",
                "Otro"
        ));

        TextArea txtDesc = new TextArea();
        txtDesc.setPromptText("Describe el problema...");
        txtDesc.setPrefRowCount(4);

        grid.add(new Label("Tipo:"), 0, 0);
        grid.add(cmbTipo, 1, 0);
        grid.add(new Label("Descripción:"), 0, 1);
        grid.add(txtDesc, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String tipo = cmbTipo.getValue();
            String desc = txtDesc.getText().trim();

            if (tipo != null && !desc.isEmpty()) {
                try {
                    Envio envio = repositorio.getEnvios().get(envioDTO.getIdEnvio());
                    String idIncidencia = GeneradorID.generarIDIncidencia();
                    String descripcionCompleta = String.format("[%s] %s", tipo, desc);

                    Incidencia incidencia = new Incidencia(
                            idIncidencia,
                            descripcionCompleta,
                            null,
                            envio
                    );

                    envio.agregarIncidencia(incidencia);
                    envio.setEstado(EstadoEnvio.INCIDENCIA);

                    AlertHelper.mostrarExito("Incidencia Registrada",
                            "La incidencia fue registrada exitosamente");

                    actualizarTablaEnvios();
                    cargarIncidencias();

                } catch (Exception e) {
                    AlertHelper.mostrarError("Error", e.getMessage());
                }
            } else {
                AlertHelper.mostrarError("Datos Incompletos", "Completa todos los campos");
            }
        }
    }

    private void reportarIncidenciaCompleta() {
        // Similar a reportarIncidenciaRapida pero con más opciones
        reportarIncidenciaRapida(null);
    }

    private void cargarIncidencias() {
        List<String> incidencias = new ArrayList<>();

        for (Envio envio : repartidorActual.getEnviosAsignados()) {
            for (Incidencia inc : envio.getIncidencias()) {
                IncidenciaDTO incDTO = DTOAdapter.toDTO(inc);
                String item = String.format("[%s] %s - %s (%s)",
                        incDTO.getIdIncidencia(),
                        envio.getIdEnvio(),
                        incDTO.getDescripcion(),
                        incDTO.getEstado()
                );
                incidencias.add(item);
            }
        }

        if (incidencias.isEmpty()) {
            incidencias.add("No hay incidencias registradas");
        }

        listIncidencias.setItems(FXCollections.observableArrayList(incidencias));

        // Actualizar combo de envíos para incidencias
        List<String> enviosParaIncidencia = repartidorActual.getEnviosAsignados().stream()
                .filter(e -> e.getEstado() != EstadoEnvio.ENTREGADO)
                .map(Envio::getIdEnvio)
                .collect(Collectors.toList());

        cmbEnvioIncidencia.setItems(FXCollections.observableArrayList(enviosParaIncidencia));
    }

    // ========== PERFIL ==========

    private void cargarPerfil() {
        lblPerfilId.setText(repartidorActual.getId());
        txtPerfilNombre.setText(repartidorActual.getNombre());
        lblPerfilDocumento.setText(repartidorActual.getDocumento());
        txtPerfilTelefono.setText(repartidorActual.getTelefono());
        txtPerfilCorreo.setText(repartidorActual.getCorreo());

        sliderCapacidad.setValue(capacidadMaxima);
        chkAsignacionAutomatica.setSelected(asignacionAutomatica);
    }

    @FXML
    private void handleActualizarPerfil(ActionEvent event) {
        try {
            String nombre = txtPerfilNombre.getText().trim();
            String telefono = txtPerfilTelefono.getText().trim();
            String correo = txtPerfilCorreo.getText().trim();

            if (!Validador.validarTextoNoVacio(nombre)) {
                AlertHelper.mostrarError("Error", "El nombre no puede estar vacío");
                return;
            }

            if (!Validador.validarTelefono(telefono)) {
                AlertHelper.mostrarError("Error", "El teléfono debe tener 10 dígitos");
                return;
            }

            if (!Validador.validarEmail(correo)) {
                AlertHelper.mostrarError("Error", "El correo no es válido");
                return;
            }

            repartidorActual.setNombre(nombre);
            repartidorActual.setTelefono(telefono);
            repartidorActual.setCorreo(correo);

            AlertHelper.mostrarExito("Perfil Actualizado",
                    "Tu información se actualizó correctamente");
            lblBienvenida.setText("Bienvenido, " + nombre);

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    private void handleGuardarConfiguracion(ActionEvent event) {
        capacidadMaxima = (int) sliderCapacidad.getValue();
        asignacionAutomatica = chkAsignacionAutomatica.isSelected();

        lblCapacidadMaxima.setText(String.valueOf(capacidadMaxima));

        AlertHelper.mostrarExito("Configuración Guardada",
                String.format("Capacidad: %d envíos\nAsignación automática: %s",
                        capacidadMaxima,
                        asignacionAutomatica ? "Activada" : "Desactivada"));

        actualizarEstadoHeader();

        if (asignacionAutomatica) {
            iniciarBusquedaAutomatica();
        }
    }

    private void actualizarEstadisticas() {
        try {
            long totalEntregas = repartidorActual.getEnviosAsignados().stream()
                    .filter(e -> e.getEstado() == EstadoEnvio.ENTREGADO)
                    .count();

            long totalEnvios = repartidorActual.getEnviosAsignados().size();
            double tasaExito = totalEnvios > 0 ? (totalEntregas * 100.0 / totalEnvios) : 0;

            long totalIncidencias = repartidorActual.getEnviosAsignados().stream()
                    .mapToLong(e -> e.getIncidencias().size())
                    .sum();

            long enviosActivos = repartidorActual.getEnviosAsignados().stream()
                    .filter(e -> e.getEstado() != EstadoEnvio.ENTREGADO)
                    .count();

            lblEstadTotalEntregas.setText(String.valueOf(totalEntregas));
            lblEstadTasaExito.setText(String.format("%.1f%%", tasaExito));
            lblEstadIncidencias.setText(String.valueOf(totalIncidencias));
            lblEstadActivos.setText(String.valueOf(enviosActivos));

        } catch (Exception e) {
            System.err.println("Error al actualizar estadísticas: " + e.getMessage());
        }
    }

    // ========== ASIGNACIÓN AUTOMÁTICA ==========

    private void iniciarBusquedaAutomatica() {
        if (!asignacionAutomatica) return;

        AlertHelper.mostrarInfo("Búsqueda Automática",
                "Se activó la búsqueda automática de envíos.\n" +
                        "El sistema buscará envíos disponibles cada 5 minutos.");
    }

    private void iniciarActualizacionAutomatica() {
        autoRefreshTimeline = new Timeline(new KeyFrame(Duration.minutes(1), e -> {
            actualizarTablaEnvios();

            // Si está en modo automático, buscar nuevos envíos
            if (asignacionAutomatica &&
                    repartidorActual.getEstadoDisponibilidad() == EstadoDisponibilidad.ACTIVO &&
                    contarEnviosActivos() < capacidadMaxima) {

                buscarYAsignarAutomaticamente();
            }
        }));
        autoRefreshTimeline.setCycleCount(Timeline.INDEFINITE);
        autoRefreshTimeline.play();
    }

    private void buscarYAsignarAutomaticamente() {
        try {
            List<Envio> enviosDisponibles = repositorio.getEnvios().values().stream()
                    .filter(e -> e.getEstado() == EstadoEnvio.SOLICITADO)
                    .filter(e -> e.getRepartidor() == null)
                    .filter(e -> perteneceAMisZonas(e))
                    .limit(capacidadMaxima - contarEnviosActivos())
                    .collect(Collectors.toList());

            if (!enviosDisponibles.isEmpty()) {
                for (Envio envio : enviosDisponibles) {
                    repartidorService.asignarEnvio(repartidorActual.getId(), envio.getIdEnvio());
                }

                System.out.println(String.format(
                        "[AUTO-ASIGNACIÓN] Se asignaron %d envíos automáticamente a %s",
                        enviosDisponibles.size(),
                        repartidorActual.getNombre()
                ));

                actualizarTablaEnvios();
            }

        } catch (Exception e) {
            System.err.println("Error en asignación automática: " + e.getMessage());
        }
    }

    // ========== CERRAR SESIÓN ==========

    @FXML
    private void handleCerrarSesion(ActionEvent event) {
        // Verificar si tiene envíos en ruta
        long enRuta = repartidorActual.getEnviosAsignados().stream()
                .filter(e -> e.getEstado() == EstadoEnvio.EN_RUTA)
                .count();

        if (enRuta > 0) {
            boolean confirmar = AlertHelper.mostrarConfirmacion(
                    "Envíos en Ruta",
                    "Tienes " + enRuta + " envío(s) EN RUTA.\n\n" +
                            "¿Estás seguro de que deseas cerrar sesión?"
            );

            if (!confirmar) return;
        }

        boolean confirmar = AlertHelper.mostrarConfirmacion(
                "Cerrar Sesión",
                "¿Estás seguro de que deseas cerrar sesión?"
        );

        if (confirmar) {
            try {
                // Detener actualización automática
                if (autoRefreshTimeline != null) {
                    autoRefreshTimeline.stop();
                }

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/co/edu/uniquindio/rapponcho/Login.fxml")
                );
                Parent root = loader.load();

                Stage stage = (Stage) lblBienvenida.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("RapponCho - Login");

                System.out.println("🚪 Repartidor cerró sesión: " + repartidorActual.getNombre());

            } catch (Exception e) {
                AlertHelper.mostrarError("Error", "No se pudo cerrar sesión: " + e.getMessage());
            }
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * Calcula la distancia entre dos zonas (simulado)
     */
    private double calcularDistancia(ZonaDireccion origen, ZonaDireccion destino) {
        double dx = destino.getCoordenadaX() - origen.getCoordenadaX();
        double dy = destino.getCoordenadaY() - origen.getCoordenadaY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Estima el tiempo de entrega en minutos (simulado)
     */
    private int estimarTiempoEntrega(Envio envio) {
        double distancia = calcularDistancia(
                envio.getOrigen().getMunicipio(),
                envio.getDestino().getMunicipio()
        );
        // 5 minutos por km + 10 minutos base
        return (int) (distancia * 5 + 10);
    }

    /**
     * Genera reporte de entregas del día
     */
    public String generarReporteDelDia() {
        LocalDateTime hoy = LocalDateTime.now();
        List<Envio> entregasHoy = repartidorActual.getEnviosAsignados().stream()
                .filter(e -> e.getEstado() == EstadoEnvio.ENTREGADO)
                .filter(e -> e.getFechaCreacion().toLocalDate().equals(hoy.toLocalDate()))
                .collect(Collectors.toList());

        StringBuilder reporte = new StringBuilder();
        reporte.append("═══════════════════════════════════════\n");
        reporte.append("     REPORTE DE ENTREGAS DEL DÍA\n");
        reporte.append("═══════════════════════════════════════\n\n");

        reporte.append("Repartidor: ").append(repartidorActual.getNombre()).append("\n");
        reporte.append("ID: ").append(repartidorActual.getId()).append("\n");
        reporte.append("Fecha: ").append(hoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).append("\n\n");

        reporte.append("ENTREGAS COMPLETADAS: ").append(entregasHoy.size()).append("\n\n");

        if (!entregasHoy.isEmpty()) {
            for (Envio envio : entregasHoy) {
                reporte.append("• ").append(envio.getIdEnvio()).append("\n");
                reporte.append("  De: ").append(envio.getOrigen().getMunicipio().name()).append("\n");
                reporte.append("  A: ").append(envio.getDestino().getMunicipio().name()).append("\n");
                reporte.append("  Cliente: ").append(envio.getUsuario().getNombre()).append("\n\n");
            }
        }

        reporte.append("═══════════════════════════════════════\n");

        return reporte.toString();
    }

    /**
     * Imprime reporte en consola
     */
    public void imprimirReporteEnConsola() {
        System.out.println(generarReporteDelDia());
    }

    /**
     * Obtiene el siguiente envío recomendado basado en ubicación actual
     */
    private Envio obtenerSiguienteEnvioRecomendado() {
        return repartidorActual.getEnviosAsignados().stream()
                .filter(e -> e.getEstado() == EstadoEnvio.ASIGNADO)
                .min(Comparator.comparingInt(this::estimarTiempoEntrega))
                .orElse(null);
    }

    /**
     * Valida que el repartidor pueda realizar una acción
     */
    private boolean validarAccion(String accion) {
        if (repartidorActual.getEstadoDisponibilidad() == EstadoDisponibilidad.INACTIVO) {
            AlertHelper.mostrarAdvertencia("Acción no Permitida",
                    "Debes estar ACTIVO o EN_RUTA para " + accion);
            return false;
        }
        return true;
    }

    /**
     * Registra una acción en el log del sistema
     */
    private void registrarAccion(String accion, String detalles) {
        System.out.println(String.format(
                "[REPARTIDOR] %s - %s (%s): %s - %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                repartidorActual.getNombre(),
                repartidorActual.getId(),
                accion,
                detalles
        ));
    }

    /**
     * Muestra notificaciones de envíos próximos
     */
    private void mostrarNotificacionesEnvios() {
        long pendientes = repartidorActual.getEnviosAsignados().stream()
                .filter(e -> e.getEstado() == EstadoEnvio.ASIGNADO)
                .count();

        if (pendientes > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Envíos Pendientes");
            alert.setHeaderText("Tienes envíos por entregar");
            alert.setContentText("Tienes " + pendientes + " envío(s) pendiente(s) de iniciar.\n" +
                    "Revisa tu lista de envíos.");
            alert.show(); // No bloqueante
        }
    }

    /**
     * Calcula eficiencia del repartidor
     */
    private double calcularEficiencia() {
        long total = repartidorActual.getEnviosAsignados().size();
        if (total == 0) return 100.0;

        long entregados = repartidorActual.getEnviosAsignados().stream()
                .filter(e -> e.getEstado() == EstadoEnvio.ENTREGADO)
                .count();

        long conIncidencia = repartidorActual.getEnviosAsignados().stream()
                .filter(e -> !e.getIncidencias().isEmpty())
                .count();

        // Fórmula: (entregados / total) * (1 - incidencias/total) * 100
        return (entregados / (double) total) * (1 - (conIncidencia / (double) total)) * 100;
    }

    /**
     * Exporta datos de depuración
     */
    public void exportarDatosDebug() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("DEBUG: Estado del Repartidor");
        System.out.println("=".repeat(60));

        System.out.println("\n👤 INFORMACIÓN:");
        System.out.println("  Nombre: " + repartidorActual.getNombre());
        System.out.println("  ID: " + repartidorActual.getId());
        System.out.println("  Estado: " + repartidorActual.getEstadoDisponibilidad());
        System.out.println("  Capacidad: " + contarEnviosActivos() + "/" + capacidadMaxima);

        System.out.println("\n📦 ENVÍOS:");
        System.out.println("  Total: " + repartidorActual.getEnviosAsignados().size());
        System.out.println("  Asignados: " + contarEnviosPorEstado(EstadoEnvio.ASIGNADO));
        System.out.println("  En Ruta: " + contarEnviosPorEstado(EstadoEnvio.EN_RUTA));
        System.out.println("  Entregados: " + contarEnviosPorEstado(EstadoEnvio.ENTREGADO));
        System.out.println("  Con Incidencia: " + contarEnviosPorEstado(EstadoEnvio.INCIDENCIA));

        System.out.println("\n📍 ZONAS:");
        repartidorActual.getZonaCobertura().forEach(z ->
                System.out.println("  - " + z.name()));

        System.out.println("\n📊 MÉTRICAS:");
        System.out.println("  Eficiencia: " + String.format("%.1f%%", calcularEficiencia()));
        System.out.println("  Tasa de éxito: " + lblEstadTasaExito.getText());

        System.out.println("\n" + "=".repeat(60) + "\n");
    }

    private long contarEnviosPorEstado(EstadoEnvio estado) {
        return repartidorActual.getEnviosAsignados().stream()
                .filter(e -> e.getEstado() == estado)
                .count();
    }

    /**
     * Cleanup al destruir el controlador
     */
    public void cleanup() {
        if (autoRefreshTimeline != null) {
            autoRefreshTimeline.stop();
        }
    }
}