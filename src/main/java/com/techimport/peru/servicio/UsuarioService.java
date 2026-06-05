package com.techimport.peru.servicio;

import com.google.common.base.Preconditions;
import com.techimport.peru.dao.UsuarioDAO;
import com.techimport.peru.modelo.Usuario;
import com.techimport.peru.util.ExcelExportUtil;
import com.techimport.peru.util.SeguridadUtil;
import com.techimport.peru.util.ValidacionUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Capa de servicio (lógica de negocio) para la gestión de usuarios.
 *
 * <p>Esta clase actúa como intermediaria entre la capa de presentación (controladores/servlets)
 * y la capa de acceso a datos ({@link UsuarioDAO}), encapsulando toda la lógica de negocio
 * relacionada con los usuarios del sistema.</p>
 *
 * <h3>Responsabilidades principales:</h3>
 * <ul>
 *   <li><strong>Registro:</strong> Validación de datos, verificación de unicidad de email,
 *       generación de salt, hash de contraseña y persistencia del usuario</li>
 *   <li><strong>Autenticación:</strong> Verificación de credenciales mediante hash seguro</li>
 *   <li><strong>Consultas:</strong> Delegación de búsquedas al DAO con validación previa</li>
 *   <li><strong>Exportación:</strong> Generación de reportes Excel de usuarios</li>
 * </ul>
 *
 * <h3>Principios de diseño aplicados:</h3>
 * <ul>
 *   <li><strong>Inversión de Dependencias (DIP):</strong> El DAO se inyecta vía constructor</li>
 *   <li><strong>Separación de Responsabilidades:</strong> La validación se delega a {@link ValidacionUtil}</li>
 *   <li><strong>Seguridad por diseño:</strong> Passwords nunca se almacenan en texto plano</li>
 * </ul>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 *   UsuarioDAO dao = new UsuarioDAOImpl();
 *   UsuarioService servicio = new UsuarioService(dao);
 *
 *   // Registrar un nuevo usuario
 *   Usuario nuevo = servicio.registrar("Juan", "Pérez", "juan@email.com", "MiPass123");
 *
 *   // Autenticar
 *   Optional<Usuario> autenticado = servicio.autenticar("juan@email.com", "MiPass123");
 * }</pre>
 *
 * @author TechImport Perú
 * @version 1.0
 * @since 2026-06-07
 * @see UsuarioDAO
 * @see SeguridadUtil
 * @see ValidacionUtil
 */
public class UsuarioService {

    /** Logger para registrar operaciones del servicio de usuarios. */
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    /** Rol asignado por defecto a los nuevos usuarios registrados. */
    private static final String ROL_POR_DEFECTO = "CLIENTE";

    /** DAO de usuarios inyectado para acceso a datos. Aplica el Principio de Inversión de Dependencias. */
    private final UsuarioDAO usuarioDAO;

    /**
     * Constructor con inyección de dependencias.
     *
     * <p>Recibe una implementación de {@link UsuarioDAO} que será utilizada
     * para todas las operaciones de acceso a datos. Utiliza {@link Preconditions#checkNotNull}
     * de Google Guava para garantizar que la dependencia no sea nula.</p>
     *
     * @param usuarioDAO la implementación del DAO de usuarios; no debe ser {@code null}
     * @throws NullPointerException si {@code usuarioDAO} es {@code null}
     */
    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = Preconditions.checkNotNull(usuarioDAO,
                "El DAO de usuarios no puede ser nulo.");
        logger.debug("UsuarioService inicializado con DAO: {}", usuarioDAO.getClass().getSimpleName());
    }

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * <p>El proceso de registro incluye las siguientes validaciones y operaciones:</p>
     * <ol>
     *   <li>Validación del formato del nombre y apellidos</li>
     *   <li>Validación del formato del correo electrónico</li>
     *   <li>Verificación de la fortaleza de la contraseña (mínimo 8 caracteres,
     *       mayúscula, minúscula y dígito)</li>
     *   <li>Verificación de que el email no esté ya registrado</li>
     *   <li>Generación de salt criptográfico</li>
     *   <li>Hash de la contraseña con SHA-256 + salt</li>
     *   <li>Creación del objeto {@link Usuario} con rol "{@value #ROL_POR_DEFECTO}"</li>
     *   <li>Persistencia mediante el DAO</li>
     * </ol>
     *
     * @param nombres    los nombres del usuario; debe ser un nombre válido según {@link ValidacionUtil#esNombreValido}
     * @param apellidos  los apellidos del usuario; debe ser un nombre válido
     * @param email      la dirección de correo electrónico; debe ser válida y no estar registrada previamente
     * @param contrasena la contraseña en texto plano; debe cumplir los requisitos de seguridad
     * @return el objeto {@link Usuario} creado (sin la contraseña en texto plano),
     *         o {@code null} si el registro falló
     * @throws IllegalArgumentException si alguno de los parámetros no cumple las validaciones
     * @throws IllegalStateException    si el email ya está registrado en el sistema
     */
    public Usuario registrar(String nombres, String apellidos, String email, String contrasena) {
        logger.info("Iniciando proceso de registro para email: {}", email);

        // Validación de nombres
        Preconditions.checkArgument(ValidacionUtil.esNombreValido(nombres),
                "El nombre proporcionado no es válido: '%s'", nombres);

        // Validación de apellidos
        Preconditions.checkArgument(ValidacionUtil.esNombreValido(apellidos),
                "Los apellidos proporcionados no son válidos: '%s'", apellidos);

        // Validación de email
        Preconditions.checkArgument(ValidacionUtil.esEmailValido(email),
                "El correo electrónico proporcionado no es válido: '%s'", email);

        // Validación de contraseña
        Preconditions.checkArgument(ValidacionUtil.esContrasenaSegura(contrasena),
                "La contraseña no cumple los requisitos mínimos de seguridad "
                        + "(mínimo 8 caracteres, mayúscula, minúscula y dígito).");

        // Verificar unicidad del email
        if (usuarioDAO.existeEmail(email)) {
            logger.warn("Intento de registro con email ya existente: {}", email);
            throw new IllegalStateException("El correo electrónico '" + email + "' ya está registrado.");
        }

        // Generar salt y hash de la contraseña
        String salt = SeguridadUtil.generarSalt();
        String hashContrasena = SeguridadUtil.hashContrasena(contrasena, salt);

        // Crear el objeto Usuario
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombres(ValidacionUtil.sanitizarEntrada(nombres));
        nuevoUsuario.setApellidos(ValidacionUtil.sanitizarEntrada(apellidos));
        nuevoUsuario.setEmail(email.trim().toLowerCase());
        nuevoUsuario.setContrasena(hashContrasena);
        nuevoUsuario.setSalt(salt);
        nuevoUsuario.setRol(ROL_POR_DEFECTO);
        nuevoUsuario.setActivo(true);

        // Persistir mediante el DAO
        boolean creado = usuarioDAO.crear(nuevoUsuario);

        if (creado) {
            logger.info("Nuevo usuario registrado: {}", email);
            return nuevoUsuario;
        } else {
            logger.error("Error al registrar usuario en la base de datos: {}", email);
            return null;
        }
    }

    /**
     * Autentica un usuario mediante su correo electrónico y contraseña.
     *
     * <p>El proceso de autenticación consiste en:</p>
     * <ol>
     *   <li>Buscar el usuario por email en la base de datos</li>
     *   <li>Si no se encuentra, registrar advertencia y retornar vacío</li>
     *   <li>Si se encuentra, verificar la contraseña usando el salt almacenado</li>
     *   <li>Registrar el resultado del intento de autenticación (éxito o fallo)</li>
     * </ol>
     *
     * @param email      la dirección de correo electrónico del usuario
     * @param contrasena la contraseña en texto plano proporcionada por el usuario
     * @return un {@link Optional} con el {@link Usuario} si la autenticación fue exitosa,
     *         o {@link Optional#empty()} si las credenciales son incorrectas o el usuario no existe
     */
    public Optional<Usuario> autenticar(String email, String contrasena) {
        logger.info("Intento de autenticación para: {}", email);

        // Buscar usuario por email
        Optional<Usuario> usuarioOpt = usuarioDAO.buscarPorEmail(email);

        if (usuarioOpt.isEmpty()) {
            logger.warn("Intento de login fallido — usuario no encontrado: {}", email);
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();

        // Verificar contraseña
        boolean contrasenaValida = SeguridadUtil.verificarContrasena(
                contrasena, usuario.getSalt(), usuario.getContrasena());

        if (!contrasenaValida) {
            logger.warn("Intento de login fallido para: {}", email);
            return Optional.empty();
        }

        logger.info("Login exitoso para: {}", email);
        return Optional.of(usuario);
    }

    /**
     * Busca un usuario por su identificador único.
     *
     * <p>Delega directamente al DAO sin lógica de negocio adicional.</p>
     *
     * @param id el identificador único del usuario
     * @return un {@link Optional} con el usuario encontrado, o vacío si no existe
     */
    public Optional<Usuario> buscarPorId(int id) {
        logger.debug("Buscando usuario por ID: {}", id);
        return usuarioDAO.buscarPorId(id);
    }

    /**
     * Lista todos los usuarios activos del sistema.
     *
     * <p>Delega directamente al DAO, que retorna solo usuarios con {@code activo = true}.</p>
     *
     * @return lista de usuarios activos; nunca {@code null}
     */
    public List<Usuario> listarTodos() {
        logger.debug("Listando todos los usuarios activos.");
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        logger.debug("Total de usuarios activos encontrados: {}", usuarios.size());
        return usuarios;
    }

    /**
     * Exporta todos los usuarios activos a un libro de trabajo Excel (.xlsx).
     *
     * <p>Obtiene la lista completa de usuarios activos del DAO y delega la
     * generación del archivo Excel a {@link ExcelExportUtil#exportarUsuarios(List)}.</p>
     *
     * <p>El libro de trabajo incluye:</p>
     * <ul>
     *   <li>Encabezado estilizado con fondo azul y texto blanco</li>
     *   <li>Columnas: ID, Nombres, Apellidos, Email, Rol, Fecha Creación, Estado</li>
     *   <li>Colores alternados en las filas para facilitar la lectura</li>
     *   <li>Columnas auto-dimensionadas</li>
     * </ul>
     *
     * @return un {@link Workbook} con los datos de todos los usuarios activos
     * @see ExcelExportUtil#exportarUsuarios(List)
     */
    public Workbook exportarUsuariosExcel() {
        logger.info("Iniciando exportación de usuarios a Excel.");
        List<Usuario> usuarios = listarTodos();
        Workbook workbook = ExcelExportUtil.exportarUsuarios(usuarios);
        logger.info("Exportación a Excel completada. Total usuarios exportados: {}", usuarios.size());
        return workbook;
    }
}
