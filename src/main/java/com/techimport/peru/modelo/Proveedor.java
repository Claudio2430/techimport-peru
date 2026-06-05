package com.techimport.peru.modelo;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * Entidad que representa a un proveedor internacional de productos
 * tecnológicos para TechImport Perú.
 *
 * <p>Los proveedores son las fuentes de los productos importados. Cada proveedor
 * tiene información de contacto y país de origen. Las validaciones se realizan
 * con Google Guava {@link Preconditions} y Apache Commons {@link StringUtils}.</p>
 *
 * @author TechImport Perú - Proyecto Universitario UTP (9.° ciclo)
 * @version 1.0
 */
public class Proveedor {

    // ──────────────────────────────────────────────
    //  Atributos privados (encapsulamiento completo)
    // ──────────────────────────────────────────────

    /** Identificador único del proveedor. */
    private int id;

    /** Nombre o razón social del proveedor. */
    private String nombre;

    /** País de origen del proveedor. */
    private String pais;

    /** Correo electrónico de contacto del proveedor. */
    private String contactoEmail;

    /** Número de teléfono de contacto del proveedor. */
    private String telefono;

    /** Indica si el proveedor está activo en el sistema. */
    private boolean activo;

    // ──────────────────────────────────────────────
    //  Constructores
    // ──────────────────────────────────────────────

    /**
     * Constructor sin argumentos.
     * Requerido para frameworks de serialización y persistencia.
     */
    public Proveedor() {
        // Constructor vacío por defecto
    }

    /**
     * Constructor con todos los argumentos para inicializar un proveedor completo.
     *
     * @param id            identificador único del proveedor
     * @param nombre        nombre o razón social (no puede estar vacío)
     * @param pais          país de origen (no puede estar vacío)
     * @param contactoEmail correo electrónico de contacto (debe contener '@')
     * @param telefono      número de teléfono (no puede estar vacío)
     * @param activo        estado de actividad del proveedor
     */
    public Proveedor(int id, String nombre, String pais, String contactoEmail,
                     String telefono, boolean activo) {
        this.id = id;
        setNombre(nombre);
        setPais(pais);
        setContactoEmail(contactoEmail);
        setTelefono(telefono);
        this.activo = activo;
    }

    // ──────────────────────────────────────────────
    //  Getters y Setters con validaciones
    // ──────────────────────────────────────────────

    /**
     * Obtiene el identificador único del proveedor.
     *
     * @return el id del proveedor
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del proveedor.
     *
     * @param id el nuevo id del proveedor
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre o razón social del proveedor.
     *
     * @return el nombre del proveedor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre o razón social del proveedor.
     * Valida que no esté vacío ni contenga solo espacios en blanco.
     *
     * @param nombre el nombre del proveedor
     * @throws IllegalArgumentException si el nombre está vacío o en blanco
     */
    public void setNombre(String nombre) {
        // Validación: el nombre del proveedor es obligatorio
        Preconditions.checkArgument(StringUtils.isNotBlank(nombre),
                "El nombre del proveedor no puede estar vacío");
        this.nombre = StringUtils.trim(nombre);
    }

    /**
     * Obtiene el país de origen del proveedor.
     *
     * @return el país del proveedor
     */
    public String getPais() {
        return pais;
    }

    /**
     * Establece el país de origen del proveedor.
     * Valida que no esté vacío ni contenga solo espacios en blanco.
     *
     * @param pais el país de origen
     * @throws IllegalArgumentException si el país está vacío o en blanco
     */
    public void setPais(String pais) {
        // Validación: el país es obligatorio
        Preconditions.checkArgument(StringUtils.isNotBlank(pais),
                "El país no puede estar vacío");
        this.pais = StringUtils.trim(pais);
    }

    /**
     * Obtiene el correo electrónico de contacto del proveedor.
     *
     * @return el email de contacto
     */
    public String getContactoEmail() {
        return contactoEmail;
    }

    /**
     * Establece el correo electrónico de contacto del proveedor.
     * Valida que no esté vacío y que contenga el carácter '@'.
     *
     * @param contactoEmail el correo electrónico de contacto
     * @throws IllegalArgumentException si el email es inválido
     */
    public void setContactoEmail(String contactoEmail) {
        // Validación: formato básico de email de contacto
        Preconditions.checkArgument(
                StringUtils.isNotBlank(contactoEmail) && contactoEmail.contains("@"),
                "Email de contacto inválido");
        this.contactoEmail = StringUtils.trim(contactoEmail);
    }

    /**
     * Obtiene el número de teléfono de contacto del proveedor.
     *
     * @return el teléfono del proveedor
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono de contacto del proveedor.
     * Valida que no esté vacío ni contenga solo espacios en blanco.
     *
     * @param telefono el número de teléfono
     * @throws IllegalArgumentException si el teléfono está vacío o en blanco
     */
    public void setTelefono(String telefono) {
        // Validación: el teléfono es obligatorio
        Preconditions.checkArgument(StringUtils.isNotBlank(telefono),
                "El teléfono no puede estar vacío");
        this.telefono = StringUtils.trim(telefono);
    }

    /**
     * Indica si el proveedor está activo en el sistema.
     *
     * @return {@code true} si está activo, {@code false} en caso contrario
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece el estado de actividad del proveedor.
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
     * Devuelve una representación en cadena de texto del proveedor.
     *
     * @return representación legible del proveedor
     */
    @Override
    public String toString() {
        return "Proveedor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", pais='" + pais + '\'' +
                ", contactoEmail='" + contactoEmail + '\'' +
                ", telefono='" + telefono + '\'' +
                ", activo=" + activo +
                '}';
    }

    /**
     * Compara este proveedor con otro objeto para determinar igualdad.
     * Dos proveedores se consideran iguales si tienen el mismo {@code id}.
     *
     * @param o el objeto a comparar
     * @return {@code true} si los objetos son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proveedor proveedor = (Proveedor) o;
        return id == proveedor.id;
    }

    /**
     * Calcula el código hash del proveedor basado en {@code id}.
     *
     * @return el código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
