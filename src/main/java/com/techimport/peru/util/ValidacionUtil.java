package com.techimport.peru.util;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Clase utilitaria para la validación y sanitización de entradas de usuario.
 *
 * <p>Proporciona métodos estáticos para validar correos electrónicos, verificar
 * la fortaleza de contraseñas, validar nombres y sanitizar entradas para prevenir
 * ataques de Cross-Site Scripting (XSS).</p>
 *
 * <p>Utiliza {@link StringUtils} de Apache Commons Lang para validaciones de cadenas
 * y {@link Preconditions} de Google Guava para comprobaciones de precondiciones.</p>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 *   boolean emailOk = ValidacionUtil.esEmailValido("usuario@email.com");
 *   boolean passOk = ValidacionUtil.esContrasenaSegura("MiPass123");
 *   String limpio = ValidacionUtil.sanitizarEntrada("<script>alert('xss')</script>");
 * }</pre>
 *
 * @author TechImport Perú
 * @version 1.0
 * @since 2026-06-07
 */
public final class ValidacionUtil {

    /**
     * Patrón de expresión regular para validar direcciones de correo electrónico.
     *
     * <p>Acepta formatos estándar como {@code usuario@dominio.com}.</p>
     */
    private static final Pattern PATRON_EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /**
     * Longitud mínima requerida para una contraseña segura.
     */
    private static final int LONGITUD_MINIMA_CONTRASENA = 8;

    /**
     * Patrón para detectar al menos una letra mayúscula.
     */
    private static final Pattern PATRON_MAYUSCULA = Pattern.compile("[A-Z]");

    /**
     * Patrón para detectar al menos una letra minúscula.
     */
    private static final Pattern PATRON_MINUSCULA = Pattern.compile("[a-z]");

    /**
     * Patrón para detectar al menos un dígito numérico.
     */
    private static final Pattern PATRON_DIGITO = Pattern.compile("[0-9]");

    /**
     * Patrón para validar nombres (solo letras, espacios y caracteres acentuados).
     */
    private static final Pattern PATRON_NOMBRE =
            Pattern.compile("^[A-Za-záéíóúÁÉÍÓÚñÑüÜ\\s]{2,100}$");

    /**
     * Constructor privado para evitar la instanciación de esta clase utilitaria.
     */
    private ValidacionUtil() {
        // Clase utilitaria — no instanciable
    }

    /**
     * Valida si una cadena tiene el formato de un correo electrónico válido.
     *
     * <p>Verifica que la cadena no sea nula ni vacía y que coincida con el patrón
     * de email estándar (e.g., {@code usuario@dominio.com}).</p>
     *
     * @param email la dirección de correo electrónico a validar
     * @return {@code true} si el email tiene un formato válido,
     *         {@code false} si es {@code null}, está vacío o tiene formato inválido
     */
    public static boolean esEmailValido(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return PATRON_EMAIL.matcher(email.trim()).matches();
    }

    /**
     * Verifica si una contraseña cumple con los requisitos mínimos de seguridad.
     *
     * <p>Una contraseña se considera segura si cumple <strong>todos</strong> los
     * siguientes criterios:</p>
     * <ul>
     *   <li>Tiene al menos {@value #LONGITUD_MINIMA_CONTRASENA} caracteres</li>
     *   <li>Contiene al menos una letra mayúscula</li>
     *   <li>Contiene al menos una letra minúscula</li>
     *   <li>Contiene al menos un dígito numérico</li>
     * </ul>
     *
     * @param contrasena la contraseña a evaluar
     * @return {@code true} si la contraseña cumple todos los requisitos de seguridad,
     *         {@code false} en caso contrario o si es {@code null}/vacía
     */
    public static boolean esContrasenaSegura(String contrasena) {
        if (StringUtils.isBlank(contrasena)) {
            return false;
        }

        if (contrasena.length() < LONGITUD_MINIMA_CONTRASENA) {
            return false;
        }

        boolean tieneMayuscula = PATRON_MAYUSCULA.matcher(contrasena).find();
        boolean tieneMinuscula = PATRON_MINUSCULA.matcher(contrasena).find();
        boolean tieneDigito = PATRON_DIGITO.matcher(contrasena).find();

        return tieneMayuscula && tieneMinuscula && tieneDigito;
    }

    /**
     * Sanitiza una cadena de entrada para prevenir ataques de Cross-Site Scripting (XSS).
     *
     * <p>Realiza las siguientes operaciones:</p>
     * <ol>
     *   <li>Recorta espacios en blanco al inicio y final (trim)</li>
     *   <li>Reemplaza los caracteres especiales de HTML por sus entidades correspondientes:
     *       {@code &}, {@code <}, {@code >}, {@code "}, {@code '}</li>
     * </ol>
     *
     * @param entrada la cadena de texto a sanitizar
     * @return la cadena sanitizada; {@code null} si la entrada es {@code null},
     *         cadena vacía si la entrada está en blanco
     * @throws NullPointerException si se requiere una entrada no nula (según el contexto de uso)
     */
    public static String sanitizarEntrada(String entrada) {
        if (entrada == null) {
            return null;
        }

        String sanitizada = StringUtils.trimToEmpty(entrada);

        if (sanitizada.isEmpty()) {
            return sanitizada;
        }

        // Reemplazar caracteres peligrosos para prevenir XSS
        sanitizada = sanitizada.replace("&", "&amp;");
        sanitizada = sanitizada.replace("<", "&lt;");
        sanitizada = sanitizada.replace(">", "&gt;");
        sanitizada = sanitizada.replace("\"", "&quot;");
        sanitizada = sanitizada.replace("'", "&#x27;");

        return sanitizada;
    }

    /**
     * Valida si una cadena es un nombre válido de persona.
     *
     * <p>Un nombre válido debe cumplir los siguientes criterios:</p>
     * <ul>
     *   <li>No ser {@code null} ni estar vacío</li>
     *   <li>Tener entre 2 y 100 caracteres</li>
     *   <li>Contener únicamente letras (incluyendo caracteres acentuados del español),
     *       la letra ñ/Ñ y espacios</li>
     * </ul>
     *
     * @param nombre el nombre a validar
     * @return {@code true} si el nombre cumple con los criterios de validación,
     *         {@code false} en caso contrario
     */
    public static boolean esNombreValido(String nombre) {
        if (StringUtils.isBlank(nombre)) {
            return false;
        }
        return PATRON_NOMBRE.matcher(nombre.trim()).matches();
    }
}
