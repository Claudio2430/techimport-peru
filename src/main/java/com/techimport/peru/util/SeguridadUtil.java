package com.techimport.peru.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Clase utilitaria para la seguridad de contraseñas.
 *
 * <p>Proporciona métodos para la generación de salt criptográfico, el hash
 * seguro de contraseñas utilizando SHA-256 y la verificación de contraseñas
 * almacenadas. Utiliza {@link DigestUtils} de Apache Commons Codec para las
 * operaciones de hashing.</p>
 *
 * <h3>Flujo de registro de usuario:</h3>
 * <ol>
 *   <li>Generar un salt aleatorio con {@link #generarSalt()}</li>
 *   <li>Hashear la contraseña con {@link #hashContrasena(String, String)}</li>
 *   <li>Almacenar el salt y el hash en la base de datos</li>
 * </ol>
 *
 * <h3>Flujo de autenticación:</h3>
 * <ol>
 *   <li>Recuperar el salt y hash almacenados del usuario</li>
 *   <li>Verificar con {@link #verificarContrasena(String, String, String)}</li>
 * </ol>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 *   String salt = SeguridadUtil.generarSalt();
 *   String hash = SeguridadUtil.hashContrasena("miContraseña123", salt);
 *   boolean valida = SeguridadUtil.verificarContrasena("miContraseña123", salt, hash);
 * }</pre>
 *
 * @author TechImport Perú
 * @version 1.0
 * @since 2026-06-07
 */
public final class SeguridadUtil {

    /** Logger para registrar eventos de seguridad y autenticación. */
    private static final Logger logger = LoggerFactory.getLogger(SeguridadUtil.class);

    /** Tamaño del salt en bytes. */
    private static final int SALT_TAMANO_BYTES = 16;

    /** Generador de números aleatorios criptográficamente seguros. */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Constructor privado para evitar la instanciación de esta clase utilitaria.
     */
    private SeguridadUtil() {
        // Clase utilitaria — no instanciable
    }

    /**
     * Genera un salt criptográficamente seguro codificado en Base64.
     *
     * <p>Utiliza {@link SecureRandom} para generar 16 bytes aleatorios,
     * que luego son codificados en Base64 para su almacenamiento como texto.</p>
     *
     * @return una cadena Base64 que representa el salt generado; nunca {@code null}
     */
    public static String generarSalt() {
        byte[] saltBytes = new byte[SALT_TAMANO_BYTES];
        SECURE_RANDOM.nextBytes(saltBytes);

        String salt = Base64.getEncoder().encodeToString(saltBytes);
        logger.debug("Salt criptográfico generado exitosamente.");
        return salt;
    }

    /**
     * Genera el hash SHA-256 de una contraseña combinada con un salt.
     *
     * <p>El hash se calcula concatenando el salt con la contraseña ({@code salt + contraseña})
     * y aplicando el algoritmo SHA-256 mediante {@link DigestUtils#sha256Hex(String)}.</p>
     *
     * @param contrasena la contraseña en texto plano a hashear; no debe ser {@code null} ni estar vacía
     * @param salt       el salt criptográfico a utilizar; no debe ser {@code null} ni estar vacío
     * @return el hash SHA-256 de la combinación salt + contraseña en formato hexadecimal;
     *         {@code null} si alguno de los parámetros es inválido
     */
    public static String hashContrasena(String contrasena, String salt) {
        if (StringUtils.isBlank(contrasena)) {
            logger.error("Intento de hashear una contraseña vacía o nula.");
            return null;
        }

        if (StringUtils.isBlank(salt)) {
            logger.error("Intento de hashear con un salt vacío o nulo.");
            return null;
        }

        String hash = DigestUtils.sha256Hex(salt + contrasena);
        logger.debug("Hash de contraseña generado exitosamente.");
        return hash;
    }

    /**
     * Verifica si una contraseña en texto plano coincide con un hash almacenado.
     *
     * <p>Recalcula el hash de la contraseña proporcionada usando el salt almacenado
     * y lo compara con el hash previamente almacenado en la base de datos.</p>
     *
     * @param contrasena      la contraseña en texto plano a verificar;
     *                        no debe ser {@code null} ni estar vacía
     * @param salt            el salt utilizado originalmente al crear el hash;
     *                        no debe ser {@code null} ni estar vacío
     * @param hashAlmacenado  el hash almacenado en la base de datos contra el cual
     *                        se realiza la comparación; no debe ser {@code null} ni estar vacío
     * @return {@code true} si la contraseña es correcta (los hashes coinciden),
     *         {@code false} en caso contrario o si algún parámetro es inválido
     */
    public static boolean verificarContrasena(String contrasena, String salt, String hashAlmacenado) {
        if (StringUtils.isBlank(contrasena) || StringUtils.isBlank(salt) || StringUtils.isBlank(hashAlmacenado)) {
            logger.warn("Intento de verificación de contraseña con parámetros inválidos (vacíos o nulos).");
            return false;
        }

        String hashCalculado = hashContrasena(contrasena, salt);

        if (hashCalculado == null) {
            logger.error("Error al calcular el hash durante la verificación de contraseña.");
            return false;
        }

        boolean coincide = hashAlmacenado.equals(hashCalculado);

        if (coincide) {
            logger.info("Verificación de contraseña exitosa.");
        } else {
            logger.warn("Verificación de contraseña fallida: los hashes no coinciden.");
        }

        return coincide;
    }
}
