package co.edu.uniquindio.fx10.proyectofinals2.services;


import co.edu.uniquindio.fx10.proyectofinals2.model.*;
import co.edu.uniquindio.fx10.proyectofinals2.reposytorie.Repositorio;
import co.edu.uniquindio.fx10.proyectofinals2.utils.GeneradorID;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

    /**
     * Servicio para gestionar operaciones relacionadas con Envío
     * RF-004, RF-006, RF-007, RF-008, RF-022, RF-023, RF-024, RF-025, RF-026
     */
    public class EnvioService {
        private final Repositorio repositorio;

        public EnvioService() {
            this.repositorio = Repositorio.getInstancia();
        }

        /**
         * Crear Envio
         * @param idUsuario
         * @param idOrigen
         * @param idDestino
         * @param servicios
         * @return
         * @throws Exception
         */
        public Envio crearEnvio(String idUsuario, String idOrigen, String idDestino,
                                List<Paquete> paquetes, // ← Recibe los paquetes
                                List<ServicioAdicional> servicios) throws Exception {

            Usuario usuario = repositorio.getUsuarios().get(idUsuario);
            Direccion origen = repositorio.getDirecciones().get(idOrigen);
            Direccion destino = repositorio.getDirecciones().get(idDestino);

            if (usuario == null || origen == null || destino == null) {
                throw new Exception("Usuario, origen o destino no encontrado");
            }

            // Validar que haya al menos un paquete
            if (paquetes == null || paquetes.isEmpty()) {
                throw new Exception("Debe haber al menos un paquete en el envío");
            }

            String idEnvio = GeneradorID.generarIDEnvio();

            // Crear tarifa base
            String idTarifa = GeneradorID.generarIDTarifa();
            ITarifa tarifa = new Tarifa.Builder()
                    .idTarifa(idTarifa)
                    .costoPorKm(2000)
                    .costoPorKg(1500)
                    .costoPorM3(5000)
                    .build();

            // Aplicar servicios adicionales
            for (ServicioAdicional servicio : servicios) {
                tarifa = aplicarServicio(tarifa, servicio);
            }

            // Crear el envío
            Envio nuevoEnvio = new Envio.Builder()
                    .IdEnvio(idEnvio)
                    .Usuario(usuario)
                    .Origen(origen)
                    .Destino(destino)
                    .Tarifa((Tarifa) tarifa)
                    .EstadoEnvio(EstadoEnvio.SOLICITADO)
                    .FechaCreacion(LocalDateTime.now())
                    .FechaEstimadaEntrega(LocalDateTime.now().plusHours(6))
                    .build();

            // Agregar paquetes DESPUÉS de crear el envío
            for (Paquete paquete : paquetes) {
                nuevoEnvio.agregarPaquete(paquete);
                repositorio.getPaquetes().put(paquete.getIdpaquete(), paquete);
            }

            usuario.getEnvios().add(nuevoEnvio);
            repositorio.getEnvios().put(idEnvio, nuevoEnvio);

            return nuevoEnvio;
        }

        private ITarifa aplicarServicio(ITarifa tarifa, ServicioAdicional servicio) {
            return switch (servicio) {
                case SEGURO -> new TarifaSeguro(tarifa);
                case PRIORIDAD -> new TarifaPrioridad(tarifa);
                case FRAGIL -> new TarifaFragil(tarifa);
                case FIRMA_REQUERIDA -> new TarifaFirmaRequerida(tarifa);
            };
        }

        /**
         * Modificar Envio
         * @param idEnvio
         * @param idOrigen
         * @param idDestino
         * @throws Exception
         */
        public void modificarEnvio(String idEnvio, String idOrigen, String idDestino) throws Exception {
            Envio envio = repositorio.getEnvios().get(idEnvio);
            if (envio == null) {
                throw new Exception("Envío no encontrado");
            }

            if (envio.getEstado() != EstadoEnvio.SOLICITADO) {
                throw new Exception("Solo se pueden modificar envíos en estado SOLICITADO");
            }

            // Modificación (necesitarías agregar setters en Envio)
            // Direccion origen = repositorio.getDirecciones().get(idOrigen);
            // Direccion destino = repositorio.getDirecciones().get(idDestino);
            // envio.setOrigen(origen);
            // envio.setDestino(destino);
        }

        /**
         * Cancelar Envio
         * @param idEnvio
         * @throws Exception
         */
        public void cancelarEnvio(String idEnvio) throws Exception {
            Envio envio = repositorio.getEnvios().get(idEnvio);
            if (envio == null) {
                throw new Exception("Envío no encontrado");
            }

            if (envio.getEstado() != EstadoEnvio.SOLICITADO) {
                throw new Exception("Solo se pueden cancelar envíos en estado SOLICITADO");
            }

            // Eliminar el envío
            Usuario usuario = envio.getUsuario();
            usuario.getEnvios().remove(envio);
            repositorio.getEnvios().remove(idEnvio);
        }

        /**
         * Actualizar Estado
         * @param idEnvio
         * @param nuevoEstado
         * @throws Exception
         */
        public void actualizarEstado(String idEnvio, EstadoEnvio nuevoEstado) throws Exception {
            Envio envio = repositorio.getEnvios().get(idEnvio);
            if (envio == null) {
                throw new Exception("Envío no encontrado");
            }

            envio.setEstado(nuevoEstado);
        }

        /**
         * Agregar servicio adicional
         * @param idEnvio
         * @param servicio
         * @throws Exception
         */
        public void agregarServicioAdicional(String idEnvio, ServicioAdicional servicio) throws Exception {
            Envio envio = repositorio.getEnvios().get(idEnvio);
            if (envio == null) {
                throw new Exception("Envío no encontrado");
            }

            if (envio.getEstado() != EstadoEnvio.SOLICITADO) {
                throw new Exception("Solo se pueden agregar servicios a envíos en estado SOLICITADO");
            }

            envio.agregarServicioAdicional(servicio);
        }

        /**
         * Filtrar por estado
         * @param estado
         * @return
         */
        public List<Envio> filtrarPorEstado(EstadoEnvio estado) {
            return repositorio.getEnvios().values().stream()
                    .filter(e -> e.getEstado() == estado)
                    .collect(Collectors.toList());
        }

        /**
         * Filtrar por zona
         * @param zona
         * @return
         */
        public List<Envio> filtrarPorZona(ZonaDireccion zona) {
            return repositorio.getEnvios().values().stream()
                    .filter(e -> e.getDestino().getMunicipio() == zona)
                    .collect(Collectors.toList());
        }

        /**
         * Filtrar por Fecha
         * @param fechaInicio
         * @param fechaFin
         * @return
         */
        public List<Envio> filtrarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
            return repositorio.getEnvios().values().stream()
                    .filter(e -> e.getFechaCreacion().isAfter(fechaInicio) &&
                            e.getFechaCreacion().isBefore(fechaFin))
                    .collect(Collectors.toList());
        }

        /**
         * Consultar Envio
         * @param idEnvio
         * @return
         * @throws Exception
         */
        public Envio consultarDetalle(String idEnvio) throws Exception {
            Envio envio = repositorio.getEnvios().get(idEnvio);
            if (envio == null) {
                throw new Exception("Envío no encontrado");
            }
            return envio;
        }

        /**
         * Agregar Paquete
         * @param idEnvio
         * @param ancho
         * @param alto
         * @param largo
         * @param peso
         * @throws Exception
         */
        public void agregarPaquete(String idEnvio, double ancho, double alto,
                                   double largo, double peso) throws Exception {
            Envio envio = repositorio.getEnvios().get(idEnvio);
            if (envio == null) {
                throw new Exception("Envío no encontrado");
            }

            String idPaquete = GeneradorID.generarIDPaquete();
            Paquete paquete = new Paquete(idPaquete, ancho, alto, largo, peso);

            envio.agregarPaquete(paquete);
            repositorio.getPaquetes().put(idPaquete, paquete);
        }
    }

