package co.edu.uniquindio.fx10.proyectofinals2.model.AdapterDTO;

import co.edu.uniquindio.fx10.proyectofinals2.model.*;
import co.edu.uniquindio.fx10.proyectofinals2.dataTransferObjects.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DTOAdapter {

    /**
     * PATRÓN ADAPTER
     *
     * Problema: El modelo de dominio (Usuario, Envio, etc.) tiene objetos complejos
     * con relaciones bidireccionales, lógica de negocio, y referencias circulares.
     * La capa de presentación (JavaFX) necesita objetos simples y planos.
     *
     * Propósito: Adaptar objetos del modelo a DTOs para la vista, y viceversa.
     *
     * Solución: DTOMapper actúa como adaptador entre las dos capas.
     */

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

        // ============ ADAPTAR: Envío → DTO ============
        public static EnvioSimpleDTO toDTO(Envio envio) {
            return new EnvioSimpleDTO(
                    envio.getIdEnvio(),
                    envio.getUsuario().getNombre(),
                    envio.getRepartidor() != null ? envio.getRepartidor().getNombre() : "Sin asignar",
                    envio.getOrigen().getMunicipio().name(),
                    envio.getDestino().getMunicipio().name(),
                    0.0, // Calcular después con tarifa
                    envio.getEstado().name(),
                    envio.getFechaCreacion(),
                    envio.getFechaEstimadaEntrega(),
                    envio.getPago() != null ? envio.getPago().getResultado().name() : "PENDIENTE"
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
            return new PaqueteSimpleDTO(paquete.getIdpaquete(), paquete.getAncho(), paquete.getAlto(), paquete.getLargo(), paquete.getPesokg(), paquete.getVolumenM3(), paquete.getDimensionesFormateadas());
        }

        // ============ ADAPTAR: Incidencia → DTO ============
        public static IncidenciaDTO toDTO(Incidencia incidencia) {
            return new IncidenciaDTO(
                    incidencia.getIdIncidencia(),
                    incidencia.getDescripcion(),
                    incidencia.getFecha(),
                    incidencia.getEstado().name(),
                    incidencia.getEnvio().getIdEnvio()
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
            double costoBase = tarifa.CalcularCosto(distancia, peso, volumen);

            return new TarifaDTO(
                    tarifa.getIdTarifa(),
                    tarifa.getTarifaBase(),
                    tarifa.getCostoPorKm(),
                    tarifa.getCostoPorKg(),
                    tarifa.getCostoPorM3(),
                    costoBase,
                    tarifa.getDescripcion(),
                    new ArrayList<>(),
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
