package com.techimport.peru.dao.impl;

import com.techimport.peru.dao.UsuarioDAO;
import com.techimport.peru.modelo.Usuario;
import com.techimport.peru.util.ConexionDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Implementación concreta del DAO de usuarios utilizando JDBC.
 *
 * <p>Esta clase implementa la interfaz {@link UsuarioDAO} proporcionando acceso
 * a la base de datos relacional para todas las operaciones CRUD y consultas
 * especializadas sobre la tabla {@code usuario}.</p>
 *
 * <h3>Características principales:</h3>
 * <ul>
 *   <li>Utiliza {@link PreparedStatement} para todas las consultas, previniendo inyección SQL</li>
 *   <li>Implementa soft delete (eliminación lógica) mediante el campo {@code activo}</li>
 *   <li>Logging extensivo con SLF4J a nivel DEBUG, INFO, WARN y ERROR</li>
 *   <li>Gestión segura de recursos JDBC en bloques {@code finally}</li>
 *   <li>Aplica el Principio de Inversión de Dependencias (DIP) mediante un constructor
 *       alternativo que acepta un {@link Supplier}{@code <Connection>} para testing</li>
 * </ul>
 *
 * <p>Ejemplo de uso en producción:</p>
 * <pre>{@code
 *   UsuarioDAO dao = new UsuarioDAOImpl();
 *   Optional<Usuario> usuario = dao.buscarPorEmail("juan@email.com");
 * }</pre>
 *
 * <p>Ejemplo de uso en tests:</p>
 * <pre>{@code
 *   Supplier<Connection> mockSupplier = () -> mockConnection;
 *   UsuarioDAO dao = new UsuarioDAOImpl(mockSupplier);
 * }</pre>
 *
 * @author TechImport Perú
 * @version 1.0
 * @since 2026-06-07
 * @see UsuarioDAO
 * @see ConexionDB
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    /** Logger para registrar todas las operaciones de acceso a datos de usuario. */
    private static final Logger logger = LoggerFactory.getLogger(UsuarioDAOImpl.class);

    // ──────────────────────────────────────────────────────────────────────────
    // Consultas SQL
    // ──────────────────────────────────────────────────────────────────────────

    /** SQL para insertar un nuevo usuario. */
    private static final String SQL_CREAR =
            "INSERT INTO usuario (nombres, apellidos, email, contrasena, salt, rol, activo) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    /** SQL para buscar un usuario por su ID. */
    private static final String SQL_BUSCAR_POR_ID =
            "SELECT * FROM usuario WHERE id = ?";

    /** SQL para buscar un usuario por su email. */
    private static final String SQL_BUSCAR_POR_EMAIL =
            "SELECT * FROM usuario WHERE email = ?";

    /** SQL para listar todos los usuarios activos. */
    private static final String SQL_LISTAR_TODOS =
            "SELECT * FROM usuario WHERE activo = true";

    /** SQL para actualizar los datos de un usuario. */
    private static final String SQL_ACTUALIZAR =
            "UPDATE usuario SET nombres=?, apellidos=?, email=?, rol=?, activo=? WHERE id=?";

    /** SQL para eliminación lógica (soft delete) de un usuario. */
    private static final String SQL_ELIMINAR =
            "UPDATE usuario SET activo = false WHERE id = ?";

    /** SQL para verificar si un email ya existe en la base de datos. */
    private static final String SQL_EXISTE_EMAIL =
            "SELECT COUNT(*) FROM usuario WHERE email = ?";

    /** SQL para buscar usuarios activos por rol. */
    private static final String SQL_BUSCAR_POR_ROL =
            "SELECT * FROM usuario WHERE rol = ? AND activo = true";

    // ──────────────────────────────────────────────────────────────────────────
    // Campos de instancia
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Proveedor de conexiones a la base de datos.
     *
     * <p>Permite inyectar un origen de conexiones alternativo para pruebas unitarias,
     * aplicando el Principio de Inversión de Dependencias (DIP).</p>
     */
    private final Supplier<Connection> connectionSupplier;

    // ──────────────────────────────────────────────────────────────────────────
    // Constructores
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Constructor por defecto que utiliza {@link ConexionDB} para obtener conexiones.
     *
     * <p>Este es el constructor que se utiliza en producción. Las conexiones se obtienen
     * a través de {@link ConexionDB#obtenerConexion()}.</p>
     */
    public UsuarioDAOImpl() {
        this.connectionSupplier = () -> {
            try {
                return ConexionDB.obtenerConexion();
            } catch (SQLException e) {
                logger.error("Error al obtener conexión de ConexionDB: {}", e.getMessage(), e);
                throw new RuntimeException("No se pudo obtener la conexión a la base de datos.", e);
            }
        };
        logger.debug("UsuarioDAOImpl inicializado con ConexionDB por defecto.");
    }

    /**
     * Constructor para inyección de dependencias que acepta un proveedor de conexiones.
     *
     * <p>Aplica el Principio de Inversión de Dependencias (DIP) del patrón SOLID,
     * permitiendo inyectar conexiones mock durante las pruebas unitarias.</p>
     *
     * @param connectionSupplier el proveedor de conexiones JDBC; no debe ser {@code null}
     * @throws NullPointerException si {@code connectionSupplier} es {@code null}
     */
    public UsuarioDAOImpl(Supplier<Connection> connectionSupplier) {
        if (connectionSupplier == null) {
            throw new NullPointerException("El proveedor de conexiones no puede ser nulo.");
        }
        this.connectionSupplier = connectionSupplier;
        logger.debug("UsuarioDAOImpl inicializado con Supplier<Connection> personalizado.");
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Implementación de GenericDAO<Usuario, Integer>
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Busca un usuario por su identificador único en la tabla {@code usuario}.</p>
     *
     * @param id el ID del usuario a buscar
     * @return un {@link Optional} con el usuario encontrado, o vacío si no existe
     */
    @Override
    public Optional<Usuario> buscarPorId(Integer id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionSupplier.get();
            ps = conn.prepareStatement(SQL_BUSCAR_POR_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Usuario usuario = mapearResultSet(rs);
                logger.debug("Usuario encontrado con ID: {}", id);
                return Optional.of(usuario);
            } else {
                logger.warn("No se encontró usuario con ID: {}", id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error al buscar usuario por ID: {}", id, e);
            return Optional.empty();
        } finally {
            ConexionDB.cerrarRecursos(conn, ps, rs);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Recupera todos los usuarios activos ({@code activo = true}) de la base de datos.</p>
     *
     * @return lista de usuarios activos; nunca {@code null}
     */
    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionSupplier.get();
            ps = conn.prepareStatement(SQL_LISTAR_TODOS);
            rs = ps.executeQuery();

            while (rs.next()) {
                usuarios.add(mapearResultSet(rs));
            }

            logger.debug("Se listaron {} usuarios activos.", usuarios.size());
        } catch (SQLException e) {
            logger.error("Error al listar todos los usuarios.", e);
        } finally {
            ConexionDB.cerrarRecursos(conn, ps, rs);
        }

        return usuarios;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Inserta un nuevo usuario en la tabla {@code usuario} con los datos proporcionados.
     * Los campos requeridos son: nombres, apellidos, email, contraseña, salt, rol y estado activo.</p>
     *
     * @param entidad el usuario a crear
     * @return {@code true} si la inserción fue exitosa, {@code false} en caso contrario
     */
    @Override
    public boolean crear(Usuario entidad) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = connectionSupplier.get();
            ps = conn.prepareStatement(SQL_CREAR);

            ps.setString(1, entidad.getNombres());
            ps.setString(2, entidad.getApellidos());
            ps.setString(3, entidad.getEmail());
            ps.setString(4, entidad.getContrasena());
            ps.setString(5, entidad.getSalt());
            ps.setString(6, entidad.getRol());
            ps.setBoolean(7, entidad.isActivo());

            int filasAfectadas = ps.executeUpdate();
            boolean exito = filasAfectadas > 0;

            if (exito) {
                logger.debug("Usuario creado exitosamente: {}", entidad.getEmail());
            } else {
                logger.warn("No se pudo crear el usuario: {}", entidad.getEmail());
            }

            return exito;
        } catch (SQLException e) {
            logger.error("Error al crear usuario: {}", entidad.getEmail(), e);
            return false;
        } finally {
            ConexionDB.cerrarRecursos(conn, ps, null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Actualiza los datos de un usuario existente: nombres, apellidos, email, rol y estado.
     * La contraseña y el salt <strong>no</strong> se actualizan mediante este método.</p>
     *
     * @param entidad el usuario con los datos actualizados
     * @return {@code true} si la actualización fue exitosa, {@code false} en caso contrario
     */
    @Override
    public boolean actualizar(Usuario entidad) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = connectionSupplier.get();
            ps = conn.prepareStatement(SQL_ACTUALIZAR);

            ps.setString(1, entidad.getNombres());
            ps.setString(2, entidad.getApellidos());
            ps.setString(3, entidad.getEmail());
            ps.setString(4, entidad.getRol());
            ps.setBoolean(5, entidad.isActivo());
            ps.setInt(6, entidad.getId());

            int filasAfectadas = ps.executeUpdate();
            boolean exito = filasAfectadas > 0;

            if (exito) {
                logger.debug("Usuario actualizado exitosamente. ID: {}", entidad.getId());
            } else {
                logger.warn("No se pudo actualizar el usuario. ID: {} (posiblemente no existe)", entidad.getId());
            }

            return exito;
        } catch (SQLException e) {
            logger.error("Error al actualizar usuario. ID: {}", entidad.getId(), e);
            return false;
        } finally {
            ConexionDB.cerrarRecursos(conn, ps, null);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Realiza una eliminación lógica (soft delete) del usuario, estableciendo
     * el campo {@code activo} a {@code false} en lugar de eliminar el registro físicamente.</p>
     *
     * @param id el ID del usuario a eliminar
     * @return {@code true} si la eliminación lógica fue exitosa, {@code false} en caso contrario
     */
    @Override
    public boolean eliminar(Integer id) {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = connectionSupplier.get();
            ps = conn.prepareStatement(SQL_ELIMINAR);
            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();
            boolean exito = filasAfectadas > 0;

            if (exito) {
                logger.debug("Usuario eliminado (soft delete) exitosamente. ID: {}", id);
            } else {
                logger.warn("No se pudo eliminar el usuario. ID: {} (posiblemente no existe)", id);
            }

            return exito;
        } catch (SQLException e) {
            logger.error("Error al eliminar usuario. ID: {}", id, e);
            return false;
        } finally {
            ConexionDB.cerrarRecursos(conn, ps, null);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Implementación de UsuarioDAO (métodos específicos)
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * {@inheritDoc}
     *
     * <p>Busca un usuario por su dirección de correo electrónico. Este método se utiliza
     * principalmente durante el proceso de autenticación (login).</p>
     *
     * @param email el correo electrónico del usuario a buscar
     * @return un {@link Optional} con el usuario encontrado, o vacío si no existe
     */
    @Override
    public Optional<Usuario> buscarPorEmail(String email) {
        logger.info("Intento de búsqueda de usuario para login: {}", email);

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionSupplier.get();
            ps = conn.prepareStatement(SQL_BUSCAR_POR_EMAIL);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                Usuario usuario = mapearResultSet(rs);
                logger.debug("Usuario encontrado con email: {}", email);
                return Optional.of(usuario);
            } else {
                logger.warn("No se encontró usuario con email: {}", email);
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error al buscar usuario por email: {}", email, e);
            return Optional.empty();
        } finally {
            ConexionDB.cerrarRecursos(conn, ps, rs);
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Recupera todos los usuarios activos que tienen el rol especificado.</p>
     *
     * @param rol el rol por el cual filtrar (e.g., "ADMIN", "CLIENTE")
     * @return lista de usuarios con el rol indicado; nunca {@code null}
     */
    @Override
    public List<Usuario> buscarPorRol(String rol) {
        List<Usuario> usuarios = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionSupplier.get();
            ps = conn.prepareStatement(SQL_BUSCAR_POR_ROL);
            ps.setString(1, rol);
            rs = ps.executeQuery();

            while (rs.next()) {
                usuarios.add(mapearResultSet(rs));
            }

            logger.debug("Se encontraron {} usuarios con rol: {}", usuarios.size(), rol);
        } catch (SQLException e) {
            logger.error("Error al buscar usuarios por rol: {}", rol, e);
        } finally {
            ConexionDB.cerrarRecursos(conn, ps, rs);
        }

        return usuarios;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Verifica la existencia de un usuario con el email dado contando registros
     * en la tabla {@code usuario}.</p>
     *
     * @param email el correo electrónico a verificar
     * @return {@code true} si ya existe al menos un usuario con ese email
     */
    @Override
    public boolean existeEmail(String email) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectionSupplier.get();
            ps = conn.prepareStatement(SQL_EXISTE_EMAIL);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                boolean existe = count > 0;
                logger.debug("Verificación de existencia de email '{}': {}", email, existe);
                return existe;
            }

            return false;
        } catch (SQLException e) {
            logger.error("Error al verificar existencia de email: {}", email, e);
            return false;
        } finally {
            ConexionDB.cerrarRecursos(conn, ps, rs);
        }
    }

    // ──────────────────────────────────────────────────────────────────────────
    // Métodos privados auxiliares
    // ──────────────────────────────────────────────────────────────────────────

    /**
     * Mapea una fila del {@link ResultSet} a un objeto {@link Usuario}.
     *
     * <p>Extrae todos los campos de la fila actual del resultado y los asigna
     * a las propiedades correspondientes del objeto {@link Usuario}.</p>
     *
     * <p>Columnas mapeadas:</p>
     * <ul>
     *   <li>{@code id} → {@link Usuario#setId(int)}</li>
     *   <li>{@code nombres} → {@link Usuario#setNombres(String)}</li>
     *   <li>{@code apellidos} → {@link Usuario#setApellidos(String)}</li>
     *   <li>{@code email} → {@link Usuario#setEmail(String)}</li>
     *   <li>{@code contrasena} → {@link Usuario#setContrasena(String)}</li>
     *   <li>{@code salt} → {@link Usuario#setSalt(String)}</li>
     *   <li>{@code rol} → {@link Usuario#setRol(String)}</li>
     *   <li>{@code activo} → {@link Usuario#setActivo(boolean)}</li>
     *   <li>{@code fecha_creacion} → {@link Usuario#setFechaCreacion(LocalDateTime)}</li>
     * </ul>
     *
     * @param rs el {@link ResultSet} posicionado en la fila a mapear
     * @return un objeto {@link Usuario} con los datos de la fila
     * @throws SQLException si ocurre un error al leer los datos del ResultSet
     */
    private Usuario mapearResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNombres(rs.getString("nombres"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setEmail(rs.getString("email"));
        usuario.setContrasena(rs.getString("contrasena"));
        usuario.setSalt(rs.getString("salt"));
        usuario.setRol(rs.getString("rol"));
        usuario.setActivo(rs.getBoolean("activo"));

        Timestamp timestamp = rs.getTimestamp("fecha_creacion");
        if (timestamp != null) {
            usuario.setFechaCreacion(timestamp.toLocalDateTime());
        }

        return usuario;
    }
}
