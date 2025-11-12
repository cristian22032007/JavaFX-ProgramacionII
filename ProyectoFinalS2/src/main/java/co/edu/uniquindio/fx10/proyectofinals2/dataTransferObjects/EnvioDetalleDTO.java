package co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects;

    import java.util.ArrayList;
import java.util.List;

/**
 * DTO completo para detalles de envío
 * Usado tanto por Admin como por Usuario
 * La interacción (clickeable o no) se maneja en la vista
 */
public class EnvioDetalleDTO {
    private String idEnvio;
    private UsuarioSimpleDTO usuario;
    private RepartidorSimpleDTO repartidor;
    private DireccionSimpleDTO origen;
    private DireccionSimpleDTO destino;
    private TarifaDTO tarifa;
    private String estado;
    private String fechaCreacion;
    private String fechaEstimadaEntrega;
    private PagoSimpleDTO pago;
    private List<PaqueteSimpleDTO> paquetes;
    private List<IncidenciaDTO> incidencias;

    public EnvioDetalleDTO(String idEnvio, UsuarioSimpleDTO usuario,
                           RepartidorSimpleDTO repartidor, DireccionSimpleDTO origen,
                           DireccionSimpleDTO destino, TarifaDTO tarifa, String estado,
                           String fechaCreacion, String fechaEstimadaEntrega,
                           PagoSimpleDTO pago, List<PaqueteSimpleDTO> paquetes,
                           List<IncidenciaDTO> incidencias) {
        this.idEnvio = idEnvio;
        this.usuario = usuario;
        this.repartidor = repartidor;
        this.origen = origen;
        this.destino = destino;
        this.tarifa = tarifa;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaEstimadaEntrega = fechaEstimadaEntrega;
        this.pago = pago;
        this.paquetes = paquetes != null ? paquetes : new ArrayList<>();
        this.incidencias = incidencias != null ? incidencias : new ArrayList<>();
    }

    // Getters
    public String getIdEnvio() { return idEnvio; }
    public UsuarioSimpleDTO getUsuario() { return usuario; }
    public RepartidorSimpleDTO getRepartidor() { return repartidor; }
    public DireccionSimpleDTO getOrigen() { return origen; }
    public DireccionSimpleDTO getDestino() { return destino; }
    public TarifaDTO getTarifa() { return tarifa; }
    public String getEstado() { return estado; }
    public String getFechaCreacion() { return fechaCreacion; }
    public String getFechaEstimadaEntrega() { return fechaEstimadaEntrega; }
    public PagoSimpleDTO getPago() { return pago; }
    public List<PaqueteSimpleDTO> getPaquetes() { return paquetes; }
    public List<IncidenciaDTO> getIncidencias() { return incidencias; }

    // Métodos de utilidad
    public String getServiciosAplicados() {
        // Los servicios vienen en la descripción de la tarifa
        return tarifa.getDescripcion();
    }

    public boolean tieneIncidencias() {
        return !incidencias.isEmpty();
    }

    public int getCantidadPaquetes() {
        return paquetes.size();
    }

    public String getEstadoPago() {
        return pago != null ? pago.getResultado() : "PENDIENTE";
    }

    public String getRepartidorNombre() {
        return repartidor != null ? repartidor.getNombre() : "Sin asignar";
    }
}
