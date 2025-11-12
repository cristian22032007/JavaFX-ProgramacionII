package co.edu.uniquindio.fx10.proyectofinals2.services;

import co.edu.uniquindio.fx10.proyectofinals2.model.*;
import co.edu.uniquindio.fx10.proyectofinals2.reposytorie.Repositorio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdministradorService {
    private final Repositorio repositorio;

    public AdministradorService() {
        this.repositorio = Repositorio.getInstancia();
    }

    // ========== AUTENTICACIÓN ==========

    /**
     * Login de administrador
     * @param usuario Nombre de usuario
     * @param contrasena Contraseña
     * @return Administrador autenticado
     * @throws Exception si las credenciales son incorrectas
     */
    public Administrador iniciarSesion(String usuario, String contrasena) throws Exception {
        Administrador admin = repositorio.getAdministradores().values().stream()
                .filter(a -> a.getUsuario().equals(usuario))
                .findFirst()
                .orElse(null);

        if (admin == null) {
            throw new Exception("Usuario administrador no encontrado");
        }

        if (!admin.getContrasena().equals(contrasena)) {
            throw new Exception("Contraseña incorrecta");
        }

        return admin;
    }

    // ========== GESTIÓN DE USUARIOS ==========

    /**
     * Listar todos los usuarios del sistema
     * @return Lista de usuarios
     */
    public List<Usuario> listarUsuarios() {
        return repositorio.getUsuarios().values().stream()
                .collect(Collectors.toList());
    }

    /**
     * Buscar usuario por ID
     * @param idUsuario ID del usuario
     * @return Usuario encontrado
     * @throws Exception si no existe
     */
    public Usuario consultarUsuario(String idUsuario) throws Exception {
        Usuario usuario = repositorio.getUsuarios().get(idUsuario);
        if (usuario == null) {
            throw new Exception("Usuario no encontrado");
        }
        return usuario;
    }

    /**
     * Eliminar usuario del sistema
     * @param idUsuario ID del usuario
     * @throws Exception si hay error
     */
    public void eliminarUsuario(String idUsuario) throws Exception {
        Usuario usuario = repositorio.getUsuarios().get(idUsuario);

        if (usuario == null) {
            throw new Exception("Usuario no encontrado");
        }

        // Verificar que no tenga envíos activos
        boolean tieneEnviosActivos = usuario.getEnvios().stream()
                .anyMatch(e -> e.getEstado() != EstadoEnvio.ENTREGADO);

        if (tieneEnviosActivos) {
            throw new Exception("No se puede eliminar usuario con envíos activos");
        }

        repositorio.getUsuarios().remove(idUsuario);
    }

    // ========== GESTIÓN DE REPARTIDORES ==========

    /**
     * Listar todos los repartidores
     * @return Lista de repartidores
     */
    public List<Repartidor> listarRepartidores() {
        return repositorio.getRepartidores().values().stream()
                .collect(Collectors.toList());
    }

    /**
     * Buscar repartidor por ID
     * @param idRepartidor ID del repartidor
     * @return Repartidor encontrado
     * @throws Exception si no existe
     */
    public Repartidor consultarRepartidor(String idRepartidor) throws Exception {
        Repartidor repartidor = repositorio.getRepartidores().get(idRepartidor);
        if (repartidor == null) {
            throw new Exception("Repartidor no encontrado");
        }
        return repartidor;
    }

    /**
     * Eliminar repartidor del sistema
     * @param idRepartidor ID del repartidor
     * @throws Exception si hay error
     */
    public void eliminarRepartidor(String idRepartidor) throws Exception {
        Repartidor repartidor = repositorio.getRepartidores().get(idRepartidor);

        if (repartidor == null) {
            throw new Exception("Repartidor no encontrado");
        }

        // Verificar que no tenga envíos asignados activos
        boolean tieneEnviosActivos = repartidor.getEnviosAsignados().stream()
                .anyMatch(e -> e.getEstado() != EstadoEnvio.ENTREGADO);

        if (tieneEnviosActivos) {
            throw new Exception("No se puede eliminar repartidor con envíos activos");
        }

        repositorio.getRepartidores().remove(idRepartidor);
    }

    // ========== GESTIÓN DE ENVÍOS ==========

    /**
     * Listar todos los envíos del sistema
     * @return Lista de envíos
     */
    public List<Envio> listarEnvios() {
        return repositorio.getEnvios().values().stream()
                .collect(Collectors.toList());
    }

    /**
     * Consultar detalle de envío
     * @param idEnvio ID del envío
     * @return Envío encontrado
     * @throws Exception si no existe
     */
    public Envio consultarEnvio(String idEnvio) throws Exception {
        Envio envio = repositorio.getEnvios().get(idEnvio);
        if (envio == null) {
            throw new Exception("Envío no encontrado");
        }
        return envio;
    }

    /**
     * Cancelar cualquier envío (privilegio de admin)
     * @param idEnvio ID del envío
     * @throws Exception si hay error
     */
    public void cancelarEnvio(String idEnvio) throws Exception {
        Envio envio = repositorio.getEnvios().get(idEnvio);

        if (envio == null) {
            throw new Exception("Envío no encontrado");
        }

        if (envio.getEstado() == EstadoEnvio.ENTREGADO) {
            throw new Exception("No se puede cancelar un envío ya entregado");
        }

        // Remover de usuario
        Usuario usuario = envio.getUsuario();
        usuario.getEnvios().remove(envio);

        // Remover de repartidor si tiene
        if (envio.getRepartidor() != null) {
            envio.getRepartidor().getEnviosAsignados().remove(envio);
        }

        repositorio.getEnvios().remove(idEnvio);
    }

    // ========== ESTADÍSTICAS Y REPORTES ==========

    /**
     * Obtener estadísticas generales del sistema
     * @return Mapa con estadísticas
     */
    public Map<String, Object> obtenerEstadisticas() {
        return Map.of(
                "totalUsuarios", repositorio.getUsuarios().size(),
                "totalRepartidores", repositorio.getRepartidores().size(),
                "totalEnvios", repositorio.getEnvios().size(),
                "enviosEntregados", contarEnviosPorEstado(EstadoEnvio.ENTREGADO),
                "enviosEnRuta", contarEnviosPorEstado(EstadoEnvio.EN_RUTA),
                "enviosConIncidencia", contarEnviosPorEstado(EstadoEnvio.INCIDENCIA),
                "totalPagos", repositorio.getPagos().size(),
                "ingresosGenerados", calcularIngresosTotales()
        );
    }

    /**
     * Contar envíos por estado
     * @param estado Estado a contar
     * @return Cantidad de envíos
     */
    private long contarEnviosPorEstado(EstadoEnvio estado) {
        return repositorio.getEnvios().values().stream()
                .filter(e -> e.getEstado() == estado)
                .count();
    }

    /**
     * Calcular ingresos totales
     * @return Total de ingresos
     */
    private double calcularIngresosTotales() {
        return repositorio.getPagos().values().stream()
                .filter(p -> p.getResultado() == ResultadoPago.APROBADO)
                .mapToDouble(Pago::getMonto)
                .sum();
    }

    /**
     * Obtener envíos por rango de fechas
     * @param inicio Fecha inicio
     * @param fin Fecha fin
     * @return Lista de envíos filtrados
     */
    public List<Envio> obtenerEnviosPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return repositorio.getEnvios().values().stream()
                .filter(e -> e.getFechaCreacion().isAfter(inicio) &&
                        e.getFechaCreacion().isBefore(fin))
                .collect(Collectors.toList());
    }

    /**
     * Obtener top repartidores por cantidad de entregas
     * @param limite Cantidad de repartidores a retornar
     * @return Lista de repartidores ordenados
     */
    public List<Repartidor> obtenerTopRepartidores(int limite) {
        return repositorio.getRepartidores().values().stream()
                .sorted((r1, r2) -> {
                    long entregas1 = r1.getEnviosAsignados().stream()
                            .filter(e -> e.getEstado() == EstadoEnvio.ENTREGADO)
                            .count();
                    long entregas2 = r2.getEnviosAsignados().stream()
                            .filter(e -> e.getEstado() == EstadoEnvio.ENTREGADO)
                            .count();
                    return Long.compare(entregas2, entregas1);
                })
                .limit(limite)
                .collect(Collectors.toList());
    }

    /**
     * Obtener usuarios más activos
     * @param limite Cantidad de usuarios a retornar
     * @return Lista de usuarios ordenados
     */
    public List<Usuario> obtenerUsuariosMasActivos(int limite) {
        return repositorio.getUsuarios().values().stream()
                .sorted((u1, u2) -> Integer.compare(u2.getEnvios().size(), u1.getEnvios().size()))
                .limit(limite)
                .collect(Collectors.toList());
    }
}

