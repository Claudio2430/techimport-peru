package com.techimport.peru.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase utilitaria para la gestión de conexiones a la base de datos.
 *
 * <p>Implementa el patrón <strong>Singleton</strong> para garantizar que la configuración
 * de la base de datos se cargue una única vez desde el archivo {@code db.properties}
 * ubicado en el classpath.</p>
 *
 * <p>El archivo {@code db.properties} debe contener las siguientes claves:</p>
 * <ul>
 *   <li>{@code db.driver} — Clase del driver JDBC (e.g., {@code com.mysql.cj.jdbc.Driver})</li>
 *   <li>{@code db.url} — URL de conexión JDBC</li>
 *   <li>{@code db.user} — Nombre de usuario de la base de datos</li>
 *   <li>{@code db.password} — Contraseña de la base de datos</li>
 * </ul>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 *   Connection conn = null;
 *   PreparedStatement ps = null;
 *   ResultSet rs = null;
 *   try {
 *       conn = ConexionDB.obtenerConexion();
 *       ps = conn.prepareStatement("SELECT * FROM usuario WHERE id = ?");
 *       ps.setInt(1, 1);
 *       rs = ps.executeQuery();
 *       // procesar resultados
 *   } catch (SQLException e) {
 *       // manejar excepción
 *   } finally {
 *       ConexionDB.cerrarRecursos(conn, ps, rs);
 *   }
 * }</pre>
 *
 * @author TechImport Perú
 * @version 1.0
 * @since 2026-06-07
 */
public final class ConexionDB {

    /** Logger para registrar eventos de conexión a la base de datos. */
    private static final Logger logger = LoggerFactory.getLogger(ConexionDB.class);

    /** Nombre del archivo de propiedades de la base de datos. */
    private static final String ARCHIVO_PROPIEDADES = "db.properties";

    /** URL de conexión JDBC cargada desde las propiedades. */
    private static String url;

    /** Nombre de usuario de la base de datos. */
    private static String usuario;

    /** Contraseña de la base de datos. */
    private static String contrasena;

    /** Indicador de si la configuración fue cargada exitosamente. */
    private static boolean configuracionCargada = false;

    // Bloque estático de inicialización: carga las propiedades y el driver JDBC.
    static {
        cargarConfiguracion();
    }

    /**
     * Constructor privado para evitar la instanciación de esta clase utilitaria.
     *
     * <p>Al ser una clase con métodos exclusivamente estáticos que implementa
     * el patrón Singleton a nivel de configuración, no se permite crear instancias.</p>
     */
    private ConexionDB() {
        // Clase utilitaria — no instanciable
    }

    /**
     * Carga la configuración de la base de datos desde el archivo {@code db.properties}.
     *
     * <p>Este método se invoca automáticamente durante la carga de la clase.
     * Registra el driver JDBC especificado en la propiedad {@code db.driver}.</p>
     */
    private static void cargarConfiguracion() {
        Properties propiedades = new Properties();

        try (InputStream inputStream = ConexionDB.class.getClassLoader()
                .getResourceAsStream(ARCHIVO_PROPIEDADES)) {

            if (inputStream == null) {
                logger.error("No se encontró el archivo de configuración: {}", ARCHIVO_PROPIEDADES);
                return;
            }

            propiedades.load(inputStream);

            String driver = propiedades.getProperty("db.driver");
            url = propiedades.getProperty("db.url");
            usuario = propiedades.getProperty("db.usuario");
            contrasena = propiedades.getProperty("db.contrasena");

            // Registrar el driver JDBC
            Class.forName(driver);
            configuracionCargada = true;

            logger.info("Configuración de base de datos cargada exitosamente desde {}", ARCHIVO_PROPIEDADES);
            logger.debug("URL de conexión: {}", url);

        } catch (ClassNotFoundException e) {
            logger.error("No se pudo cargar el driver JDBC. Verifique que la dependencia esté en el classpath.", e);
        } catch (IOException e) {
            logger.error("Error al leer el archivo de configuración: {}", ARCHIVO_PROPIEDADES, e);
        }
    }

    /**
     * Obtiene una nueva conexión a la base de datos.
     *
     * <p>Cada invocación crea una conexión nueva. El llamador es responsable
     * de cerrar la conexión una vez finalizada su operación, preferiblemente
     * usando {@link #cerrarConexion(Connection)} o {@link #cerrarRecursos(Connection, PreparedStatement, ResultSet)}.</p>
     *
     * @return una {@link Connection} activa a la base de datos
     * @throws SQLException si la configuración no fue cargada correctamente
     *                      o si ocurre un error al establecer la conexión
     */
    public static Connection obtenerConexion() throws SQLException {
        if (!configuracionCargada) {
            logger.error("La configuración de la base de datos no fue cargada correctamente.");
            throw new SQLException("La configuración de la base de datos no fue cargada. "
                    + "Verifique el archivo " + ARCHIVO_PROPIEDADES);
        }

        try {
            Connection conexion = DriverManager.getConnection(url, usuario, contrasena);
            logger.debug("Conexión a la base de datos establecida exitosamente.");
            return conexion;
        } catch (SQLException e) {
            logger.error("Error al establecer la conexión a la base de datos: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Cierra una conexión a la base de datos de forma segura.
     *
     * <p>Si la conexión es {@code null} o ya está cerrada, el método no realiza ninguna acción.</p>
     *
     * @param conn la conexión a cerrar; puede ser {@code null}
     */
    public static void cerrarConexion(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                    logger.debug("Conexión a la base de datos cerrada exitosamente.");
                }
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión a la base de datos: {}", e.getMessage(), e);
            }
        }
    }

    /**
     * Cierra de forma segura todos los recursos de base de datos proporcionados.
     *
     * <p>Los recursos se cierran en orden inverso al de su creación:
     * primero el {@link ResultSet}, luego el {@link PreparedStatement},
     * y finalmente la {@link Connection}. Cada recurso se cierra de forma
     * independiente para garantizar que el fallo en uno no impida el cierre de los demás.</p>
     *
     * @param conn la conexión a cerrar; puede ser {@code null}
     * @param ps   el {@link PreparedStatement} a cerrar; puede ser {@code null}
     * @param rs   el {@link ResultSet} a cerrar; puede ser {@code null}
     */
    public static void cerrarRecursos(Connection conn, PreparedStatement ps, ResultSet rs) {
        // Cerrar ResultSet
        if (rs != null) {
            try {
                rs.close();
                logger.debug("ResultSet cerrado exitosamente.");
            } catch (SQLException e) {
                logger.error("Error al cerrar el ResultSet: {}", e.getMessage(), e);
            }
        }

        // Cerrar PreparedStatement
        if (ps != null) {
            try {
                ps.close();
                logger.debug("PreparedStatement cerrado exitosamente.");
            } catch (SQLException e) {
                logger.error("Error al cerrar el PreparedStatement: {}", e.getMessage(), e);
            }
        }

        // Cerrar Connection
        cerrarConexion(conn);
    }
}
