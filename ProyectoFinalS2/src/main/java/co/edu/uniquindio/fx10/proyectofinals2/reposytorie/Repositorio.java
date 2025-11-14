package co.edu.uniquindio.fx10.proyectofinals2.reposytorie;

import co.edu.uniquindio.fx10.proyectofinals2.model.*;
import co.edu.uniquindio.fx10.proyectofinals2.utils.*;

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

    public void inicializarSistemaPorDefecto() {
        if (!administradores.isEmpty()) {
            return; // Ya hay datos inicializados
        }

        System.out.println("🔧 Inicializando sistema con datos por defecto...");

        // ========== ADMINISTRADOR ==========
        Administrador adminDefault = new Administrador(
                "ADM001",
                "Carlos Administrador",
                "3001234567",
                "admin@rapponcho.com",
                "admin",
                "admin123"
        );
        administradores.put(adminDefault.getId(), adminDefault);
        System.out.println("✅ Administrador creado - Usuario: admin | Contraseña: admin123");

        // ========== REPARTIDOR ==========
        Repartidor repartidorDefault = new Repartidor(
                "REP100",
                "Juan Repartidor Pérez",
                "3102345678",
                "juan.repartidor@rapponcho.com",
                "repartidor",
                "repartidor123",
                "1234567890"
        );

        // Agregar zonas de cobertura al repartidor
        repartidorDefault.getZonaCobertura().add(ZonaCobertura.ARMENIA);
        repartidorDefault.getZonaCobertura().add(ZonaCobertura.CALARCA);
        repartidorDefault.getZonaCobertura().add(ZonaCobertura.CIRCASIA);
        repartidorDefault.setEstadoDisponibilidad(EstadoDisponibilidad.ACTIVO);

        repartidores.put(repartidorDefault.getId(), repartidorDefault);
        System.out.println("✅ Repartidor creado - Usuario: repartidor | Contraseña: repartidor123");

        // ========== USUARIO ==========
        Usuario usuarioDefault = new Usuario(
                "USR1001",
                "María Usuario López",
                "3203456789",
                "maria.usuario@gmail.com",
                "usuario",
                "usuario123"
        );

        // Crear direcciones para el usuario
        Direccion dir1 = new Direccion(
                "DIR00001",
                "Casa",
                "Carrera 14 #10-25",
                ZonaDireccion.ARMENIA
        );

        Direccion dir2 = new Direccion(
                "DIR00002",
                "Oficina",
                "Avenida Bolívar #15-30",
                ZonaDireccion.CALARCA
        );

        Direccion dir3 = new Direccion(
                "DIR00003",
                "Casa de Padres",
                "Calle 20 #8-15",
                ZonaDireccion.CIRCASIA
        );

        usuarioDefault.getDirecciones().add(dir1);
        usuarioDefault.getDirecciones().add(dir2);
        usuarioDefault.getDirecciones().add(dir3);

        direcciones.put(dir1.getIdDireccion(), dir1);
        direcciones.put(dir2.getIdDireccion(), dir2);
        direcciones.put(dir3.getIdDireccion(), dir3);

        // Crear métodos de pago para el usuario
        MetodoPago metodoPago1 = new MetodoPago(
                "Visa Principal",
                TipoMetodo.TARJETA_SIMULADA,
                "4532123456781234"
        );

        MetodoPago metodoPago2 = new MetodoPago(
                "Transferencia Bancolombia",
                TipoMetodo.TRANSFERENCIA_SIMULADA,
                "0012345678"
        );

        MetodoPago metodoPago3 = new MetodoPago(
                "Pago en Efectivo",
                TipoMetodo.CONTRAENTREGA,
                "N/A"
        );

        usuarioDefault.getMetodosPago().add(metodoPago1);
        usuarioDefault.getMetodosPago().add(metodoPago2);
        usuarioDefault.getMetodosPago().add(metodoPago3);

        usuarios.put(usuarioDefault.getId(), usuarioDefault);
        System.out.println("✅ Usuario creado - Usuario: usuario | Contraseña: usuario123");
        System.out.println("   📍 3 direcciones agregadas");
        System.out.println("   💳 3 métodos de pago agregados");

        // ========== CREAR ENVÍO DE EJEMPLO ==========
        try {
            // Crear tarifa base
            Tarifa tarifa = new Tarifa.Builder()
                    .idTarifa("TAR" + System.currentTimeMillis())
                    .costoPorKm(2000)
                    .costoPorKg(1500)
                    .costoPorM3(5000)
                    .build();

            // Crear envío de ejemplo
            Envio envioEjemplo = new Envio.Builder()
                    .Usuario(usuarioDefault)
                    .Repartidor(repartidorDefault)
                    .Origen(dir1)
                    .Destino(dir2)
                    .Tarifa(tarifa)
                    .EstadoEnvio(EstadoEnvio.EN_RUTA)
                    .FechaCreacion(java.time.LocalDateTime.now().minusHours(2))
                    .FechaEstimadaEntrega(java.time.LocalDateTime.now().plusHours(4))
                    .build();
                    GeneradorID.generarIDEnvio();


            // Crear paquete
            Paquete paquete = new Paquete(
                    "PKG000001",
                    30.0,  // ancho
                    20.0,  // alto
                    15.0,  // largo
                    2.5    // peso
            );

            envioEjemplo.agregarPaquete(paquete);
            paquetes.put(paquete.getIdpaquete(), paquete);

            // Crear pago aprobado
            Pago pago = new Pago(
                    "PAG20250113120000001",
                    45000.0,
                    java.time.LocalDateTime.now().minusHours(2),
                    metodoPago1,
                    ResultadoPago.APROBADO
            );

            envioEjemplo.setPago(pago);
            pagos.put(pago.getIdPago(), pago);

            // Asociar envío
            usuarioDefault.getEnvios().add(envioEjemplo);
            repartidorDefault.getEnviosAsignados().add(envioEjemplo);
            envios.put(envioEjemplo.getIdEnvio(), envioEjemplo);

            System.out.println("✅ Envío de ejemplo creado");
            System.out.println("   📦 ID: " + envioEjemplo.getIdEnvio());
            System.out.println("   📍 De: " + dir1.getAlias() + " → " + dir2.getAlias());
            System.out.println("   🚚 Repartidor: " + repartidorDefault.getNombre());
            System.out.println("   💰 Costo: $45,000");
            System.out.println("   ✅ Estado: EN_RUTA");

        } catch (Exception e) {
            System.err.println("⚠ Error al crear envío de ejemplo: " + e.getMessage());
        }

        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("🎉 SISTEMA INICIALIZADO CORRECTAMENTE");
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("📋 CREDENCIALES DE ACCESO:");
        System.out.println("");
        System.out.println("👤 USUARIO:");
        System.out.println("   Usuario: usuario");
        System.out.println("   Contraseña: usuario123");
        System.out.println("");
        System.out.println("🚚 REPARTIDOR:");
        System.out.println("   Usuario: repartidor");
        System.out.println("   Contraseña: repartidor123");
        System.out.println("");
        System.out.println("🔧 ADMINISTRADOR:");
        System.out.println("   Usuario: admin");
        System.out.println("   Contraseña: admin123");
        System.out.println("═══════════════════════════════════════════════════════\n");
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
