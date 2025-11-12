package co.edu.uniquindio.fx10.proyectofinals2.model.AdapterDTO;

import co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects.*;
import co.edu.uniquindio.fx10.proyectofinals2.model.*;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class DTOAdapter {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");



    // ============ ADAPTAR: Dirección → DTO ============
    public static DireccionSimpleDTO toSimpleDTO(Direccion direccion) {
        return new DireccionSimpleDTO(
                direccion.getIdDireccion(),
                direccion.getAlias(),
                direccion.getCalle(),
                direccion.getMunicipio().name(),
                direccion.getMunicipio().getCodigoPostal()
        );
    }

    // ============ ADAPTAR: Usuario → DTO ============
    public static UsuarioSimpleDTO toDTO(Usuario usuario) {
        return new UsuarioSimpleDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getTelefono(),
                usuario.getCorreo(),
                usuario.getUsuario(),
                usuario.getDirecciones().stream()
                        .map(DTOAdapter::toSimpleDTO)
                        .collect(Collectors.toList()),
                usuario.getEnvios().size()
        );
    }

    // ============ ADAPTAR: Repartidor → DTO ============
    public static RepartidorSimpleDTO toDTO(Repartidor repartidor) {
        return new RepartidorSimpleDTO(
                repartidor.getId(),
                repartidor.getNombre(),
                repartidor.getDocumento(),
                repartidor.getTelefono(),
                repartidor.getEstadoDisponibilidad().name(),
                repartidor.getZonaCobertura().stream()
                        .map(Enum::name)
                        .collect(Collectors.toList()),
                repartidor.getEnviosAsignados().size()
        );
    }

    // ============ ADAPTAR: Envío → DTO COMPLETO ============
    public static EnvioDetalleDTO toDTO(Envio envio) {
        // Calcular distancia, peso y volumen para la tarifa
        double distancia = envio.calcularDistancia();
        double peso = envio.getPaquetes().stream()
                .mapToDouble(Paquete::getPesokg)
                .sum();
        double volumen = envio.getPaquetes().stream()
                .mapToDouble(Paquete::getVolumenM3)
                .sum();

        return new EnvioDetalleDTO(
                envio.getIdEnvio(),
                toDTO(envio.getUsuario()),
                envio.getRepartidor() != null ? toDTO(envio.getRepartidor()) : null,
                toSimpleDTO(envio.getOrigen()),
                toSimpleDTO(envio.getDestino()),
                toDTO(envio.getTarifa(), distancia, peso,  volumen),
                envio.getEstado().name(),
                envio.getFechaCreacion().format(FORMATO_FECHA),
                envio.getFechaEstimadaEntrega().format(FORMATO_FECHA),
                envio.getPago() != null ? toDTO(envio.getPago()) : null,
                envio.getPaquetes().stream()
                        .map(DTOAdapter::toDTO)
                        .collect(Collectors.toList()),
                envio.getIncidencias().stream()
                        .map(DTOAdapter::toDTO)
                        .collect(Collectors.toList())
        );
    }

    // ============ ADAPTAR: Pago → DTO ============
    public static PagoSimpleDTO toDTO(Pago pago) {
        return new PagoSimpleDTO(
                pago.getIdPago(),
                pago.getMonto(),
                pago.getFecha(),
                pago.getMetodoPago().getTipo().name(),
                pago.getResultado().name()
        );
    }

    // ============ ADAPTAR: Paquete → DTO ============
    public static PaqueteSimpleDTO toDTO(Paquete paquete) {
        return new PaqueteSimpleDTO(
                paquete.getIdpaquete(),
                paquete.getAncho(),
                paquete.getAlto(),
                paquete.getLargo(),
                paquete.getPesokg(),
                paquete.getVolumenM3(),
                paquete.getDimensionesFormateadas()
        );
    }

    // ============ ADAPTAR: Incidencia → DTO ============
    public static IncidenciaDTO toDTO(Incidencia incidencia) {
        return new IncidenciaDTO(
                incidencia.getIdIncidencia(),
                incidencia.getDescripcion(),
                incidencia.getFecha(),
                incidencia.getEstado().name(),
                incidencia.getEnvio().getIdEnvio(),
                incidencia.getFechaCierre()
        );
    }

    // ============ ADAPTAR: MetodoPago → DTO ============
    public static MetodoPagoDTO toDTO(MetodoPago metodo) {
        String numeroEnmascarado = enmascararNumero(metodo.getNumeroSimulado());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return new MetodoPagoDTO(
                metodo.getAlias(),
                metodo.getTipo().name(),
                numeroEnmascarado,
                metodo.getFechaRegistro().format(formatter)
        );
    }

    // ============ ADAPTAR: Tarifa → DTO ============
    public static TarifaDTO toDTO(Tarifa tarifa, double distancia, double peso, double volumen) {
        double costoTotal = tarifa.CalcularCosto(distancia, peso, volumen);

        return new TarifaDTO(
                tarifa.getIdTarifa(),
                tarifa.getTarifaBase(),
                tarifa.getCostoPorKm(),
                tarifa.getCostoPorKg(),
                tarifa.getCostoPorM3(),
                costoTotal,
                tarifa.getDescripcion(),
                distancia,
                peso,
                volumen
        );
    }

    // ============ MÉTODOS AUXILIARES ============
    private static String enmascararNumero(String numero) {
        if (numero == null || numero.length() < 4) {
            return "****";
        }
        int longitudVisible = 4;
        String digitosVisibles = numero.substring(numero.length() - longitudVisible);
        return "**** **** **** " + digitosVisibles;
    }
}