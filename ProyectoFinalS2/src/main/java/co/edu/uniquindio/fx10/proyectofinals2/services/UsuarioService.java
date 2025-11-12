package co.edu.uniquindio.fx10.proyectofinals2.services;


import co.edu.uniquindio.fx10.proyectofinals2.model.*;
import co.edu.uniquindio.fx10.proyectofinals2.reposytorie.Repositorio;
import co.edu.uniquindio.fx10.proyectofinals2.utils.GeneradorID;
import co.edu.uniquindio.fx10.proyectofinals2.utils.Validador;

import java.util.List;
import java.util.stream.Collectors;

    /**
     * Servicio para gestionar operaciones relacionadas con Usuario
     * RF-001, RF-002, RF-015, RF-016, RF-017
     */
    public class UsuarioService {
        private final Repositorio repositorio;

        public UsuarioService() {
            this.repositorio = Repositorio.getInstancia();
        }

        /**
         * Registrar Usuario
         * @param nombre
         * @param telefono
         * @param correo
         * @param usuario
         * @param contrasena
         * @return
         * @throws Exception
         */
        public Usuario registrarUsuario(String nombre, String telefono, String correo,
                                        String usuario, String contrasena) throws Exception {
            // Validaciones
            if (!Validador.validarTextoNoVacio(nombre)) {
                throw new Exception("El nombre no puede estar vacío");
            }
            if (!Validador.validarTelefono(telefono)) {
                throw new Exception("El teléfono debe tener 10 dígitos");
            }
            if (!Validador.validarEmail(correo)) {
                throw new Exception("El correo electrónico no es válido");
            }
            if (!Validador.validarUsuarioYContrasena(usuario)) {
                throw new Exception("El usuario debe tener al menos 4 caracteres");
            }
            if (!Validador.validarUsuarioYContrasena(contrasena)) {
                throw new Exception("La contraseña debe tener al menos 4 caracteres");
            }

            // Verificar que el usuario no exista
            if (existeUsuario(usuario)) {
                throw new Exception("El nombre de usuario ya existe");
            }

            // Verificar que el correo no exista
            if (existeCorreo(correo)) {
                throw new Exception("El correo electrónico ya está registrado");
            }

            // Crear usuario
            String id = GeneradorID.generarIDUsuario();
            Usuario nuevoUsuario = new Usuario(id, nombre, telefono, correo, usuario, contrasena);

            // Guardar en repositorio
            repositorio.getUsuarios().put(id, nuevoUsuario);

            return nuevoUsuario;
        }

        /**
         * Metodo para iniciar sesion
         * @param usuario
         * @param contrasena
         * @return
         * @throws Exception
         */
        public Usuario iniciarSesion(String usuario, String contrasena) throws Exception {
            Usuario usuarioEncontrado = repositorio.getUsuarios().values().stream()
                    .filter(u -> u.getUsuario().equals(usuario))
                    .findFirst()
                    .orElse(null);

            if (usuarioEncontrado == null) {
                throw new Exception("Usuario no encontrado");
            }

            if (!usuarioEncontrado.getContrasena().equals(contrasena)) {
                throw new Exception("Contraseña incorrecta");
            }

            return usuarioEncontrado;
        }

        /**
         * Actualizar Datos
         * @param idUsuario
         * @param nombre
         * @param telefono
         * @param correo
         * @throws Exception
         */
        public void actualizarPerfil(String idUsuario, String nombre, String telefono,
                                     String correo) throws Exception {
            Usuario usuario = repositorio.getUsuarios().get(idUsuario);
            if (usuario == null) {
                throw new Exception("Usuario no encontrado");
            }

            // Validaciones
            if (!Validador.validarTextoNoVacio(nombre)) {
                throw new Exception("El nombre no puede estar vacío");
            }
            if (!Validador.validarTelefono(telefono)) {
                throw new Exception("El teléfono debe tener 10 dígitos");
            }
            if (!Validador.validarEmail(correo)) {
                throw new Exception("El correo electrónico no es válido");
            }
            usuario.setNombre(nombre);
            usuario.setTelefono(telefono);
            usuario.setCorreo(correo);
        }

        /**
         * Agregar Direccion
         * @param idUsuario
         * @param alias
         * @param calle
         * @param municipio
         * @return
         * @throws Exception
         */
        public Direccion agregarDireccion(String idUsuario, String alias, String calle,
                                          ZonaDireccion municipio) throws Exception {
            Usuario usuario = repositorio.getUsuarios().get(idUsuario);
            if (usuario == null) {
                throw new Exception("Usuario no encontrado");
            }
            boolean aliasExiste = usuario.getDirecciones().stream()
                    .anyMatch(d -> d.getAlias().equalsIgnoreCase(alias));

            if (aliasExiste) {
                throw new Exception("Ya existe una dirección con ese alias");
            }

            if (!Validador.validarTextoNoVacio(alias) || !Validador.validarTextoNoVacio(calle)) {
                throw new Exception("El alias y la calle no pueden estar vacíos");
            }

            String idDireccion = GeneradorID.generarIDDireccion();
            Direccion nuevaDireccion = new Direccion(idDireccion, alias, calle, municipio);

            usuario.getDirecciones().add(nuevaDireccion);
            repositorio.getDirecciones().put(idDireccion, nuevaDireccion);

            return nuevaDireccion;
        }

        /**
         * Eliminar Direccion
         * @param idUsuario
         * @param idDireccion
         * @throws Exception
         */
        public void eliminarDireccion(String idUsuario, String idDireccion) throws Exception {
            Usuario usuario = repositorio.getUsuarios().get(idUsuario);
            if (usuario == null) {
                throw new Exception("Usuario no encontrado");
            }

            Direccion direccion = usuario.getDirecciones().stream()
                    .filter(d -> d.getIdDireccion().equals(idDireccion))
                    .findFirst()
                    .orElse(null);

            if (direccion == null) {
                throw new Exception("Dirección no encontrada");
            }

            usuario.getDirecciones().remove(direccion);
            repositorio.getDirecciones().remove(idDireccion);
        }

        /**
         * Agregar Metodo de pago
         * @param idUsuario
         * @param alias
         * @param tipo
         * @param numeroSimulado
         * @return
         * @throws Exception
         */
        public MetodoPago agregarMetodoPago(String idUsuario, String alias,
                                            TipoMetodo tipo, String numeroSimulado) throws Exception {
            Usuario usuario = repositorio.getUsuarios().get(idUsuario);
            if (usuario == null) {
                throw new Exception("Usuario no encontrado");
            }

            if (!Validador.validarTextoNoVacio(alias)) {
                throw new Exception("El alias no puede estar vacío");
            }

            MetodoPago nuevoMetodo = new MetodoPago(alias, tipo, numeroSimulado);
            usuario.getMetodosPago().add(nuevoMetodo);

            return nuevoMetodo;
        }

        /**
         * Eliminar Metodo pago
         * @param idUsuario
         * @param alias
         * @throws Exception
         */
        public void eliminarMetodoPago(String idUsuario, String alias) throws Exception {
            Usuario usuario = repositorio.getUsuarios().get(idUsuario);
            if (usuario == null) {
                throw new Exception("Usuario no encontrado");
            }

            MetodoPago metodo = usuario.getMetodosPago().stream()
                    .filter(m -> m.getAlias().equals(alias))
                    .findFirst()
                    .orElse(null);

            if (metodo == null) {
                throw new Exception("Método de pago no encontrado");
            }

            usuario.getMetodosPago().remove(metodo);
        }

        /**
         * Consultar Envios
         * @param idUsuario
         * @return
         * @throws Exception
         */
        public List<Envio> consultarEnvios(String idUsuario) throws Exception {
            Usuario usuario = repositorio.getUsuarios().get(idUsuario);
            if (usuario == null) {
                throw new Exception("Usuario no encontrado");
            }
            return usuario.getEnvios();
        }

        // ========== Métodos auxiliares ==========
        private boolean existeUsuario(String usuario) {
            return repositorio.getUsuarios().values().stream()
                    .anyMatch(u -> u.getUsuario().equals(usuario));
        }

        private boolean existeCorreo(String correo) {
            return repositorio.getUsuarios().values().stream()
                    .anyMatch(u -> u.getCorreo().equals(correo));
        }
    }

