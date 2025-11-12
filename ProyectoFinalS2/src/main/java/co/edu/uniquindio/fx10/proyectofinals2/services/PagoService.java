package co.edu.uniquindio.fx10.proyectofinals2.services;


import co.edu.uniquindio.fx10.proyectofinals2.model.*;
import co.edu.uniquindio.fx10.proyectofinals2.reposytorie.Repositorio;
import co.edu.uniquindio.fx10.proyectofinals2.utils.GeneradorID;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar pagos
 * RF-005, RF-009, RF-027
 */
public class PagoService {
    private final Repositorio repositorio;

    public PagoService() {
        this.repositorio = Repositorio.getInstancia();
    }

    /**
     * Procesar pago de un envío
     * @param idEnvio ID del envío a pagar
     * @param metodoPago Método de pago seleccionado
     * @return Pago procesado
     * @throws Exception si hay error en el proceso
     */
    public Pago procesarPago(String idEnvio, MetodoPago metodoPago) throws Exception {
        Envio envio = repositorio.getEnvios().get(idEnvio);

        if (envio == null) {
            throw new Exception("Envío no encontrado");
        }

        if (envio.getPago() != null && envio.getPago().getResultado() == ResultadoPago.APROBADO) {
            throw new Exception("Este envío ya ha sido pagado");
        }

        // Calcular monto total
        double distancia = envio.calcularDistancia();
        double peso = envio.getPaquetes().stream()
                .mapToDouble(Paquete::getPesokg)
                .sum();
        double volumen = envio.getPaquetes().stream()
                .mapToDouble(Paquete::getVolumenM3)
                .sum();

        double monto = envio.getTarifa().CalcularCosto(distancia, peso, volumen);

        // Simular procesamiento de pago
        String idPago = GeneradorID.generarIDPago();
        ResultadoPago resultado = simularProcesamiento(metodoPago);

        Pago nuevoPago = new Pago(
                idPago,
                monto,
                LocalDateTime.now(),
                metodoPago,
                resultado
        );

        // Asociar pago al envío
        envio.setPago(nuevoPago);
        repositorio.getPagos().put(idPago, nuevoPago);

        // Si el pago fue aprobado, cambiar estado del envío
        if (resultado == ResultadoPago.APROBADO) {
            envio.setEstado(EstadoEnvio.SOLICITADO);
        }

        return nuevoPago;
    }

    /**
     * Consultar estado de un pago
     * @param idPago ID del pago
     * @return Pago encontrado
     * @throws Exception si no existe
     */
    public Pago consultarPago(String idPago) throws Exception {
        Pago pago = repositorio.getPagos().get(idPago);
        if (pago == null) {
            throw new Exception("Pago no encontrado");
        }
        return pago;
    }

    /**
     * Listar pagos por resultado
     * @param resultado Estado del pago
     * @return Lista de pagos filtrados
     */
    public List<Pago> listarPorResultado(ResultadoPago resultado) {
        return repositorio.getPagos().values().stream()
                .filter(p -> p.getResultado() == resultado)
                .collect(Collectors.toList());
    }

    /**
     * Listar pagos por método
     * @param tipoMetodo Tipo de método de pago
     * @return Lista de pagos filtrados
     */
    public List<Pago> listarPorMetodo(TipoMetodo tipoMetodo) {
        return repositorio.getPagos().values().stream()
                .filter(p -> p.getMetodoPago().getTipo() == tipoMetodo)
                .collect(Collectors.toList());
    }

    /**
     * Reintentar pago fallido
     * @param idEnvio ID del envío
     * @param nuevoMetodo Nuevo método de pago
     * @return Nuevo pago procesado
     * @throws Exception si hay error
     */
    public Pago reintentarPago(String idEnvio, MetodoPago nuevoMetodo) throws Exception {
        Envio envio = repositorio.getEnvios().get(idEnvio);

        if (envio == null) {
            throw new Exception("Envío no encontrado");
        }

        Pago pagoAnterior = envio.getPago();
        if (pagoAnterior == null || pagoAnterior.getResultado() != ResultadoPago.RECHAZADO) {
            throw new Exception("Solo se pueden reintentar pagos rechazados");
        }

        return procesarPago(idEnvio, nuevoMetodo);
    }

    /**
     * Calcular total de ingresos
     * @return Total de pagos aprobados
     */
    public double calcularIngresosTotales() {
        return repositorio.getPagos().values().stream()
                .filter(p -> p.getResultado() == ResultadoPago.APROBADO)
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    /**
     * Simula el procesamiento de un pago (para demo)
     * En producción, esto se conectaría con una pasarela real
     */
    private ResultadoPago simularProcesamiento(MetodoPago metodo) {
        // Simulación simple: 90% aprobados, 10% rechazados
        if (metodo.getTipo() == TipoMetodo.CONTRAENTREGA) {
            return ResultadoPago.PENDIENTE; // Pago al entregar
        }

        // Simular aprobación aleatoria
        return Math.random() < 0.9 ? ResultadoPago.APROBADO : ResultadoPago.RECHAZADO;
    }
}