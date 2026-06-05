package com.techimport.peru.modelo;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad que representa a un usuario del sistema TechImport Perú.
 *
 * <p>Un usuario puede tener el rol de {@code ADMIN} (administrador del sistema)
 * o {@code CLIENTE} (comprador de productos importados). Esta clase aplica
 * validaciones estrictas mediante Google Guava {@link Preconditions} y
 * Apache Commons {@link StringUtils} para garantizar la integridad de los datos.</p>
 *
 * @author TechImport Perú - Proyecto Universitario UTP (9.° ciclo)
 * @version 1.0
 */
public class Usuario {

    // ──────────────────────────────────────────────
    //  Atributos privados (encapsulamiento completo)
    // ──────────────────────────────────────────────

    /** Identificador único del usuario en la base de datos. */
    private int id;

    /** Nombres del usuario. */
    private String nombres;

    /** Apellidos del usuario. */
    private String apellidos;

    /** Correo electrónico del usuario (debe contener '@'). */
    private String email;

    /** Contraseña cifrada del usuario. */
    private String contrasena;

    /** Sal criptográfica utilizada para el hashing de la contraseña. */
    private String salt;

    /** Rol del usuario dentro del sistema: {@code ADMIN} o {@code CLIENTE}. */
    private String rol;

    /** Fecha y hora de creación de la cuenta. */
    private LocalDateTime fechaCreacion;

    /** Indica si la cuenta del usuario está activa. */
    private boolean activo;

    // ──────────────────────────────────────────────
    //  Constructores
    // ──────────────────────────────────────────────

    /**
     * Constructor sin argumentos.
     * Requerido para frameworks de serialización y persistencia.
     */
    public Usuario() {
        // Constructor vacío por defecto
    }

    /**
     * Constructor con todos los argumentos para inicializar un usuario completo.
     *
     * @param id            identificador único del usuario
     * @param nombres       nombres del usuario (no puede estar vacío)
     * @param apellidos     apellidos del usuario (no puede estar vacío)
     * @param email         correo electrónico válido (debe contener '@')
     * @param contrasena    contraseña cifrada (no puede ser nula)
     * @param salt          sal criptográfica para el hashing
     * @param rol           rol del usuario ({@code ADMIN} o {@code CLIENTE})
     * @param fechaCreacion fecha y hora de creación de la cuenta
     * @param activo        estado de la cuenta (activo/inactivo)
     */
    public Usuario(int id, String nombres, String apellidos, String email,
                   String contrasena, String salt, String rol,
                   LocalDateTime fechaCreacion, boolean activo) {
        this.id = id;
        setNombres(nombres);
        setApellidos(apellidos);
        setEmail(email);
        setContrasena(contrasena);
        this.salt = salt;
        setRol(rol);
        this.fechaCreacion = fechaCreacion;
        this.activo = activo;
    }

    // ──────────────────────────────────────────────
    //  Getters y Setters con validaciones
    // ──────────────────────────────────────────────

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return el id del usuario
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     *
     * @param id el nuevo id del usuario
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene los nombres del usuario.
     *
     * @return los nombres del usuario
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Establece los nombres del usuario.
     * Valida que no estén vacíos ni contengan solo espacios en blanco.
     *
     * @param nombres los nombres del usuario
     * @throws IllegalArgumentException si los nombres están vacíos o en blanco
     */
    public void setNombres(String nombres) {
        // Validación: los nombres son obligatorios
        Preconditions.checkArgument(StringUtils.isNotBlank(nombres),
                "Los nombres no pueden estar vacíos");
        this.nombres = StringUtils.trim(nombres);
    }

    /**
     * Obtiene los apellidos del usuario.
     *
     * @return los apellidos del usuario
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del usuario.
     * Valida que no estén vacíos ni contengan solo espacios en blanco.
     *
     * @param apellidos los apellidos del usuario
     * @throws IllegalArgumentException si los apellidos están vacíos o en blanco
     */
    public void setApellidos(String apellidos) {
        // Validación: los apellidos son obligatorios
        Preconditions.checkArgument(StringUtils.isNotBlank(apellidos),
                "Los apellidos no pueden estar vacíos");
        this.apellidos = StringUtils.trim(apellidos);
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return el email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     * Valida que no esté vacío y que contenga el carácter '@'.
     *
     * @param email el correo electrónico del usuario
     * @throws IllegalArgumentException si el email es inválido
     */
    public void setEmail(String email) {
        // Validación: formato básico de email
        Preconditions.checkArgument(
                StringUtils.isNotBlank(email) && email.contains("@"),
                "Email inválido");
        this.email = StringUtils.trim(email);
    }

    /**
     * Obtiene la contraseña cifrada del usuario.
     *
     * @return la contraseña cifrada
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Establece la contraseña cifrada del usuario.
     * Valida que no sea nula.
     *
     * @param contrasena la contraseña cifrada
     * @throws NullPointerException si la contraseña es nula
     */
    public void setContrasena(String contrasena) {
        // Validación: la contraseña no puede ser nula
        Preconditions.checkNotNull(contrasena, "La contraseña no puede ser nula");
        this.contrasena = contrasena;
    }

    /**
     * Obtiene la sal criptográfica utilizada para el hashing.
     *
     * @return la sal criptográfica
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Establece la sal criptográfica para el hashing de la contraseña.
     *
     * @param salt la sal criptográfica
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * Obtiene el rol del usuario en el sistema.
     *
     * @return el rol del usuario ({@code ADMIN} o {@code CLIENTE})
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario en el sistema.
     * Solo se permiten los valores {@code ADMIN} o {@code CLIENTE}.
     *
     * @param rol el rol del usuario
     * @throws IllegalArgumentException si el rol no es {@code ADMIN} ni {@code CLIENTE}
     */
    public void setRol(String rol) {
        // Validación: solo roles permitidos
        Preconditions.checkArgument(
                "ADMIN".equals(rol) || "CLIENTE".equals(rol),
                "Rol inválido");
        this.rol = rol;
    }

    /**
     * Obtiene la fecha y hora de creación de la cuenta.
     *
     * @return la fecha de creación
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha y hora de creación de la cuenta.
     *
     * @param fechaCreacion la fecha de creación
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Indica si la cuenta del usuario está activa.
     *
     * @return {@code true} si la cuenta está activa, {@code false} en caso contrario
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece el estado de actividad de la cuenta del usuario.
     *
     * @param activo {@code true} para activar, {@code false} para desactivar
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // ──────────────────────────────────────────────
    //  Métodos estándar: toString, equals, hashCode
    // ──────────────────────────────────────────────

    /**
     * Devuelve una representación en cadena de texto del usuario.
     * Excluye la contraseña y la sal por razones de seguridad.
     *
     * @return representación legible del usuario
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", email='" + email + '\'' +
                ", rol='" + rol + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", activo=" + activo +
                '}';
    }

    /**
     * Compara este usuario con otro objeto para determinar igualdad.
     * Dos usuarios se consideran iguales si tienen el mismo {@code id} y {@code email}.
     *
     * @param o el objeto a comparar
     * @return {@code true} si los objetos son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id &&
                Objects.equals(email, usuario.email);
    }

    /**
     * Calcula el código hash del usuario basado en {@code id} y {@code email}.
     *
     * @return el código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
