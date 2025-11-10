package co.edu.uniquindio.fx10.proyectofinals2.reposytorie;

import co.edu.uniquindio.fx10.proyectofinals2.model.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Repositorio {
    private static Repositorio instancia;

    // Mapas para almacenar entidades
    private final Map<String, Usuario> usuarios;
    private final Map<String, Repartidor> repartidores;
    private final Map<String, Administrador> administradores;
    private final Map<String, Envio> envios;
    private final Map<String, Direccion> direcciones;
    private final Map<String, Pago> pagos;
    private final Map<String, Paquete> paquetes;

    // Constructor privado (Singleton)
    private Repositorio() {
        this.usuarios = new ConcurrentHashMap<>();
        this.repartidores = new ConcurrentHashMap<>();
        this.administradores = new ConcurrentHashMap<>();
        this.envios = new ConcurrentHashMap<>();
        this.direcciones = new ConcurrentHashMap<>();
        this.pagos = new ConcurrentHashMap<>();
        this.paquetes = new ConcurrentHashMap<>();
    }

    // Método para obtener la instancia única
    public static synchronized Repositorio getInstancia() {
        if (instancia == null) {
            instancia = new Repositorio();
        }
        return instancia;
    }

    // Getters de los mapas
    public Map<String, Usuario> getUsuarios() { return usuarios; }
    public Map<String, Repartidor> getRepartidores() { return repartidores; }
    public Map<String, Administrador> getAdministradores() { return administradores; }
    public Map<String, Envio> getEnvios() { return envios; }
    public Map<String, Direccion> getDirecciones() { return direcciones; }
    public Map<String, Pago> getPagos() { return pagos; }
    public Map<String, Paquete> getPaquetes() { return paquetes; }
}
