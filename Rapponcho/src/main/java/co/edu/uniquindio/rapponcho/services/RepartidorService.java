package co.edu.uniquindio.rapponcho.services;


import co.edu.uniquindio.rapponcho.model.*;
import co.edu.uniquindio.rapponcho.reposytorie.Repositorio;
import co.edu.uniquindio.rapponcho.utils.GeneradorID;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar repartidores (Admin)
 * RF-011, RF-019, RF-020, RF-021
 */
public class RepartidorService {
    private final Repositorio repositorio;

    public RepartidorService() {
        this.repositorio = Repositorio.getInstancia();
    }

    // ========== RF-011 y RF-019: Crear repartidor ==========
    public Repartidor crearRepartidor(String nombre, String documento, String telefono,
                                      String correo, String usuario, String contrasena) throws Exception {
        // Validar que el documento no exista
        if (existeDocumento(documento)) {
            throw new Exception("Ya existe un repartidor con ese documento");
        }

        String id = GeneradorID.generarIDRepartidor();
        Repartidor nuevoRepartidor = new Repartidor(id, nombre, telefono, correo,
                usuario, contrasena, documento);

        repositorio.getRepartidores().put(id, nuevoRepartidor);
        return nuevoRepartidor;
    }

    // ========== RF-020: Cambiar disponibilidad ==========
    public void cambiarDisponibilidad(String idRepartidor, EstadoDisponibilidad nuevoEstado) throws Exception {
        Repartidor repartidor = repositorio.getRepartidores().get(idRepartidor);
        if (repartidor == null) {
            throw new Exception("Repartidor no encontrado");
        }

        // Necesitarías agregar setter en Repartidor
        // repartidor.setEstadoDisponibilidad(nuevoEstado);
    }

    // ========== RF-021: Consultar envíos asignados ==========
    public List<Envio> consultarEnviosAsignados(String idRepartidor) throws Exception {
        Repartidor repartidor = repositorio.getRepartidores().get(idRepartidor);
        if (repartidor == null) {
            throw new Exception("Repartidor no encontrado");
        }
        return repartidor.getEnviosAsignados();
    }

    // ========== RF-012: Asignar envío a repartidor ==========
    public void asignarEnvio(String idRepartidor, String idEnvio) throws Exception {
        Repartidor repartidor = repositorio.getRepartidores().get(idRepartidor);
        Envio envio = repositorio.getEnvios().get(idEnvio);

        if (repartidor == null || envio == null) {
            throw new Exception("Repartidor o envío no encontrado");
        }

        if (repartidor.getEstadoDisponibilidad() == EstadoDisponibilidad.INACTIVO) {
            throw new Exception("El repartidor no está disponible");
        }

        envio.setRepartidor(repartidor);
        envio.setEstado(EstadoEnvio.ASIGNADO);
        repartidor.getEnviosAsignados().add(envio);
    }

    // ========== Listar repartidores disponibles ==========
    public List<Repartidor> listarDisponibles() {
        return repositorio.getRepartidores().values().stream()
                .filter(r -> r.getEstadoDisponibilidad() == EstadoDisponibilidad.ACTIVO)
                .collect(Collectors.toList());
    }

    private boolean existeDocumento(String documento) {
        return repositorio.getRepartidores().values().stream()
                .anyMatch(r -> r.getDocumento().equals(documento));
    }
}