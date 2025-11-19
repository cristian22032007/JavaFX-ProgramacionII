package co.edu.uniquindio.rapponcho.controllers;

import co.edu.uniquindio.rapponcho.dataTransferObjects.*;
import co.edu.uniquindio.rapponcho.model.*;
import co.edu.uniquindio.rapponcho.model.AdapterDTO.DTOAdapter;
import co.edu.uniquindio.rapponcho.services.*;
import co.edu.uniquindio.rapponcho.utils.AlertHelper;
import co.edu.uniquindio.rapponcho.utils.GeneradorID;
import co.edu.uniquindio.rapponcho.utils.Validador;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioMainController {

    // Header
    @FXML
    private Label lblBienvenida;

    // Tab Nuevo Envío
    @FXML
    private ComboBox<String> cmbOrigen;
    @FXML
    private ComboBox<String> cmbDestino;
    @FXML
    private TextField txtAncho;
    @FXML
    private TextField txtAlto;
    @FXML
    private TextField txtLargo;
    @FXML
    private TextField txtPeso;
    @FXML
    private CheckBox chkSeguro;
    @FXML
    private CheckBox chkPrioridad;
    @FXML
    private CheckBox chkFragil;
    @FXML
    private CheckBox chkFirma;
    @FXML
    private Label lblCosto;
    @FXML
    private ComboBox<String> cmbMetodoPago;

    // Tab Mis Envíos
    @FXML
    private TableView<EnvioDetalleDTO> tableEnvios;
    @FXML
    private TableColumn<EnvioDetalleDTO, String> colIdEnvio;
    @FXML
    private TableColumn<EnvioDetalleDTO, String> colOrigen;
    @FXML
    private TableColumn<EnvioDetalleDTO, String> colDestino;
    @FXML
    private TableColumn<EnvioDetalleDTO, String> colEstado;
    @FXML
    private TableColumn<EnvioDetalleDTO, String> colFecha;
    @FXML
    private TableColumn<EnvioDetalleDTO, String> colCosto;

    // Tab Perfil
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private ListView<String> listDirecciones;
    @FXML
    private ListView<String> listMetodosPago;

    private final UsuarioService usuarioService;
    private final EnvioService envioService;
    private final TarifaService tarifaService;
    private final PagoService pagoService;

    private Usuario usuarioActual;
    private ITarifa tarifaCotizada;
    private double costoActual = 0;

    public UsuarioMainController() {
        this.usuarioService = new UsuarioService();
        this.envioService = new EnvioService();
        this.tarifaService = new TarifaService();
        this.pagoService = new PagoService();
    }

    @FXML
    public void initialize() {
        configurarTablas();
    }

    public void setUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        lblBienvenida.setText("Bienvenido, " + usuario.getNombre());
        cargarDatosUsuario();
    }

    private void configurarTablas() {
        colIdEnvio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdEnvio()));
        colOrigen.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getOrigen().getAlias() + " - " + data.getValue().getOrigen().getMunicipio()
        ));
        colDestino.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDestino().getAlias() + " - " + data.getValue().getDestino().getMunicipio()
        ));
        colEstado.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado()));
        colFecha.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFechaCreacion()));
        colCosto.setCellValueFactory(data -> new SimpleStringProperty(
                String.format("$%.2f", data.getValue().getTarifa().getCostoTotal())
        ));
    }

    private void cargarDatosUsuario() {
        // Cargar direcciones en combos
        cmbOrigen.setItems(FXCollections.observableArrayList(
                usuarioActual.getDirecciones().stream()
                        .map(d -> d.getIdDireccion() + " - " + d.getAlias())
                        .toList()
        ));
        cmbDestino.setItems(FXCollections.observableArrayList(
                usuarioActual.getDirecciones().stream()
                        .map(d -> d.getIdDireccion() + " - " + d.getAlias())
                        .toList()
        ));

        // Cargar métodos de pago
        cmbMetodoPago.setItems(FXCollections.observableArrayList(
                usuarioActual.getMetodosPago().stream()
                        .map(m -> m.getAlias() + " - " + m.getTipo().name())
                        .toList()
        ));

        // Cargar perfil
        txtNombre.setText(usuarioActual.getNombre());
        txtTelefono.setText(usuarioActual.getTelefono());
        txtCorreo.setText(usuarioActual.getCorreo());

        actualizarListaDirecciones();
        actualizarListaMetodosPago();
        actualizarTablaEnvios();
    }


    @FXML
    private void handleCotizar(ActionEvent event) {
        try {
            // Validar selección
            if (cmbOrigen.getValue() == null || cmbDestino.getValue() == null) {
                AlertHelper.mostrarError("Error", "Debes seleccionar origen y destino");
                return;
            }

            // Validar dimensiones usando Validador
            if (!validarDimensiones()) return;

            double ancho = Double.parseDouble(txtAncho.getText().trim());
            double alto = Double.parseDouble(txtAlto.getText().trim());
            double largo = Double.parseDouble(txtLargo.getText().trim());
            double peso = Double.parseDouble(txtPeso.getText().trim());

            // Calcular volumen
            double volumen = (ancho * alto * largo) / 1_000_000.0;

            // Obtener direcciones
            String idOrigen = extraerIdDireccion(cmbOrigen.getValue());
            String idDestino = extraerIdDireccion(cmbDestino.getValue());

            Direccion origen = buscarDireccion(idOrigen);
            Direccion destino = buscarDireccion(idDestino);

            if (origen == null || destino == null) {
                AlertHelper.mostrarError("Error", "Direcciones no encontradas");
                return;
            }

            // Calcular distancia usando las coordenadas del enum ZonaDireccion
            double dx = destino.getMunicipio().getCoordenadaX() - origen.getMunicipio().getCoordenadaX();
            double dy = destino.getMunicipio().getCoordenadaY() - origen.getMunicipio().getCoordenadaY();
            double distancia = Math.sqrt(dx * dx + dy * dy);

            // Crear tarifa usando TarifaService
            tarifaCotizada = tarifaService.cotizarTarifa(
                    distancia, peso, volumen,
                    chkSeguro.isSelected(),
                    chkPrioridad.isSelected(),
                    chkFragil.isSelected(),
                    chkFirma.isSelected()
            );

            // Calcular costo
            costoActual = tarifaCotizada.CalcularCosto(distancia, peso, volumen);
            lblCosto.setText(String.format("Costo: $%.2f", costoActual));

            // Mostrar desglose
            String desglose = tarifaService.desglosarTarifa(tarifaCotizada, distancia, peso, volumen);
            AlertHelper.mostrarInfo("Cotización", desglose);

        } catch (NumberFormatException e) {
            AlertHelper.mostrarError("Error", "Ingresa valores numéricos válidos");
        } catch (Exception e) {
            AlertHelper.mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    private void handleCrearEnvio() {
        try {
            // Validar cotización
            if (costoActual <= 0 || tarifaCotizada == null) {
                AlertHelper.mostrarError("Error", "Primero debes cotizar el envío");
                return;
            }

            // Validar método de pago
            if (cmbMetodoPago.getValue() == null) {
                AlertHelper.mostrarError("Error", "Selecciona un método de pago");
                return;
            }

            // Obtener IDs
            String idOrigen = extraerIdDireccion(cmbOrigen.getValue());
            String idDestino = extraerIdDireccion(cmbDestino.getValue());

            // Crear paquete
            double ancho = Double.parseDouble(txtAncho.getText().trim());
            double alto = Double.parseDouble(txtAlto.getText().trim());
            double largo = Double.parseDouble(txtLargo.getText().trim());
            double peso = Double.parseDouble(txtPeso.getText().trim());

            String idPaquete = GeneradorID.generarIDPaquete();
            Paquete paquete = new Paquete(idPaquete, ancho, alto, largo, peso);
            List<Paquete> paquetes = List.of(paquete);

            // Crear lista de servicios
            List<ServicioAdicional> servicios = new ArrayList<>();
            if (chkSeguro.isSelected()) servicios.add(ServicioAdicional.SEGURO);
            if (chkPrioridad.isSelected()) servicios.add(ServicioAdicional.PRIORIDAD);
            if (chkFragil.isSelected()) servicios.add(ServicioAdicional.FRAGIL);
            if (chkFirma.isSelected()) servicios.add(ServicioAdicional.FIRMA_REQUERIDA);

            // Crear envío usando EnvioService
            Envio nuevoEnvio = envioService.crearEnvio(
                    usuarioActual.getId(),
                    idOrigen,
                    idDestino,
                    paquetes,
                    servicios
            );

            // Obtener método de pago
            String aliasMetodo = cmbMetodoPago.getValue().split(" - ")[0];
            MetodoPago metodoPago = usuarioActual.getMetodosPago().stream()
                    .filter(m -> m.getAlias().equals(aliasMetodo))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Método de pago no encontrado"));

            // Procesar pago usando PagoService
            Pago pago = pagoService.procesarPago(nuevoEnvio.getIdEnvio(), metodoPago);

            if (pago.getResultado() == ResultadoPago.APROBADO) {
                AlertHelper.mostrarExito("¡Envío Creado!",
                        String.format("ID: %s\nCosto: $%.2f\nEstado: %s\n\nPago aprobado exitosamente",
                                nuevoEnvio.getIdEnvio(), costoActual, nuevoEnvio.getEstado().name()));

                limpiarFormulario();
                actualizarTablaEnvios();
            } else if (pago.getResultado() == ResultadoPago.RECHAZADO) {
                AlertHelper.mostrarError("Pago Rechazado",
                        "El pago fue rechazado. Intenta con otro método.");
            } else {
                AlertHelper.mostrarInfo("Pago Pendiente",
                        "El pago está pendiente (contraentrega). El envío fue creado.");
                limpiarFormulario();
                actualizarTablaEnvios();
            }

        } catch (Exception e) {
            AlertHelper.mostrarError("Error al Crear Envío", e.getMessage());
        }
    }

    @FXML
    private void handleLimpiar() {
        limpiarFormulario();
    }

    // ========== TAB: MIS ENVÍOS ==========

    @FXML
    private void handleActualizarEnvios() {
        actualizarTablaEnvios();
        AlertHelper.mostrarInfo("Actualizado", "Lista de envíos actualizada");
    }

    private void actualizarTablaEnvios() {
        try {
            // Obtener envíos del usuario usando UsuarioService
            List<Envio> envios = usuarioService.consultarEnvios(usuarioActual.getId());

            // Convertir a DTOs usando DTOAdapter
            List<EnvioDetalleDTO> enviosDTO = envios.stream()
                    .map(DTOAdapter::toDTO)
                    .toList();

            tableEnvios.setItems(FXCollections.observableArrayList(enviosDTO));

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudieron cargar los envíos: " + e.getMessage());
        }
    }

    @FXML
    private void handleActualizarPerfil(ActionEvent event) {
        try {
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String correo = txtCorreo.getText().trim();

            // Validar usando Validador
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

            // Actualizar usando UsuarioService
            usuarioService.actualizarPerfil(usuarioActual.getId(), nombre, telefono, correo);

            AlertHelper.mostrarExito("Perfil Actualizado", "Tu información ha sido actualizada correctamente");

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", e.getMessage());
        }
    }

    @FXML
    private void handleAgregarDireccion(ActionEvent event) {
        mostrarDialogoDireccion();
    }

    @FXML
    private void handleAgregarMetodoPago(ActionEvent event) {
        mostrarDialogoMetodoPago();
    }

    private void mostrarDialogoDireccion() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nueva Dirección");
        dialog.setHeaderText("Agrega una nueva dirección");

        // Crear formulario
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));

        TextField txtAlias = new TextField();
        txtAlias.setPromptText("Ej: Casa, Oficina");

        TextField txtCalle = new TextField();
        txtCalle.setPromptText("Ej: Calle 15 #20-30");

        ComboBox<ZonaDireccion> cmbMunicipio = new ComboBox<>();
        cmbMunicipio.setItems(FXCollections.observableArrayList(ZonaDireccion.values()));
        cmbMunicipio.setPromptText("Selecciona municipio");

        grid.add(new Label("Alias:"), 0, 0);
        grid.add(txtAlias, 1, 0);
        grid.add(new Label("Calle:"), 0, 1);
        grid.add(txtCalle, 1, 1);
        grid.add(new Label("Municipio:"), 0, 2);
        grid.add(cmbMunicipio, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String alias = txtAlias.getText().trim();
                String calle = txtCalle.getText().trim();
                ZonaDireccion municipio = cmbMunicipio.getValue();

                if (alias.isEmpty() || calle.isEmpty() || municipio == null) {
                    AlertHelper.mostrarError("Error", "Completa todos los campos");
                    return;
                }

                // Agregar usando UsuarioService
                usuarioService.agregarDireccion(usuarioActual.getId(), alias, calle, municipio);

                AlertHelper.mostrarExito("Dirección Agregada", "La dirección se agregó correctamente");
                actualizarListaDirecciones();
                cargarDatosUsuario(); // Recargar combos

            } catch (Exception e) {
                AlertHelper.mostrarError("Error", e.getMessage());
            }
        }
    }

    private void mostrarDialogoMetodoPago() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Método de Pago");
        dialog.setHeaderText("Agrega un nuevo método de pago");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));

        TextField txtAlias = new TextField();
        txtAlias.setPromptText("Ej: Visa Principal");

        ComboBox<TipoMetodo> cmbTipo = new ComboBox<>();
        cmbTipo.setItems(FXCollections.observableArrayList(TipoMetodo.values()));
        cmbTipo.setPromptText("Selecciona tipo");

        TextField txtNumero = new TextField();
        txtNumero.setPromptText("Últimos 4 dígitos (simulado)");

        grid.add(new Label("Alias:"), 0, 0);
        grid.add(txtAlias, 1, 0);
        grid.add(new Label("Tipo:"), 0, 1);
        grid.add(cmbTipo, 1, 1);
        grid.add(new Label("Número:"), 0, 2);
        grid.add(txtNumero, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String alias = txtAlias.getText().trim();
                TipoMetodo tipo = cmbTipo.getValue();
                String numero = txtNumero.getText().trim();

                if (alias.isEmpty() || tipo == null || numero.isEmpty()) {
                    AlertHelper.mostrarError("Error", "Completa todos los campos");
                    return;
                }

                // Agregar usando UsuarioService
                usuarioService.agregarMetodoPago(usuarioActual.getId(), alias, tipo, numero);

                AlertHelper.mostrarExito("Método Agregado", "El método de pago se agregó correctamente");
                actualizarListaMetodosPago();
                cargarDatosUsuario(); // Recargar combo

            } catch (Exception e) {
                AlertHelper.mostrarError("Error", e.getMessage());
            }
        }
    }

    private void actualizarListaDirecciones() {
        List<String> direcciones = usuarioActual.getDirecciones().stream()
                .map(d -> String.format("%s - %s (%s)", d.getAlias(), d.getCalle(), d.getMunicipio().name()))
                .toList();
        listDirecciones.setItems(FXCollections.observableArrayList(direcciones));
    }

    private void actualizarListaMetodosPago() {
        List<String> metodos = usuarioActual.getMetodosPago().stream()
                .map(m -> String.format("%s - %s", m.getAlias(), m.getTipo().name()))
                .toList();
        listMetodosPago.setItems(FXCollections.observableArrayList(metodos));
    }

    private void limpiarFormulario() {
        cmbOrigen.setValue(null);
        cmbDestino.setValue(null);
        txtAncho.clear();
        txtAlto.clear();
        txtLargo.clear();
        txtPeso.clear();
        chkSeguro.setSelected(false);
        chkPrioridad.setSelected(false);
        chkFragil.setSelected(false);
        chkFirma.setSelected(false);
        lblCosto.setText("Costo: $0.00");
        cmbMetodoPago.setValue(null);
        costoActual = 0;
        tarifaCotizada = null;
    }

    private boolean validarDimensiones() {
        if (!Validador.validarTextoNoVacio(txtAncho.getText()) ||
                !Validador.validarTextoNoVacio(txtAlto.getText()) ||
                !Validador.validarTextoNoVacio(txtLargo.getText()) ||
                !Validador.validarTextoNoVacio(txtPeso.getText())) {

            AlertHelper.mostrarError("Error", "Completa todas las dimensiones del paquete");
            return false;
        }

        try {
            double ancho = Double.parseDouble(txtAncho.getText().trim());
            double alto = Double.parseDouble(txtAlto.getText().trim());
            double largo = Double.parseDouble(txtLargo.getText().trim());
            double peso = Double.parseDouble(txtPeso.getText().trim());

            if (!Validador.validarDimensiones(ancho) ||
                    !Validador.validarDimensiones(alto) ||
                    !Validador.validarDimensiones(largo)) {
                AlertHelper.mostrarError("Error", "Las dimensiones deben estar entre 0 y 200 cm");
                return false;
            }

            if (!Validador.validarPeso(peso)) {
                AlertHelper.mostrarError("Error", "El peso debe estar entre 0 y 100 kg");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            AlertHelper.mostrarError("Error", "Ingresa valores numéricos válidos");
            return false;
        }
    }

    private String extraerIdDireccion(String texto) {
        return texto.split(" - ")[0];
    }

    private Direccion buscarDireccion(String id) {
        return usuarioActual.getDirecciones().stream()
                .filter(d -> d.getIdDireccion().equals(id))
                .findFirst()
                .orElse(null);
    }

    @FXML
    private void handleCerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/co/edu/uniquindio/rapponcho/Login.fxml")
            );
            Parent root = loader.load();

            Stage stage = (Stage) lblBienvenida.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("RapponCho - Login");

        } catch (Exception e) {
            AlertHelper.mostrarError("Error", "No se pudo cerrar sesión: " + e.getMessage());
        }
    }
}