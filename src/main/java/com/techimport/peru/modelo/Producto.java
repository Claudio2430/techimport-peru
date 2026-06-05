package com.techimport.peru.modelo;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad que representa un producto importado disponible en el catálogo
 * de TechImport Perú.
 *
 * <p>Cada producto pertenece a una categoría, está vinculado a un proveedor
 * y mantiene un control de inventario (stock). Las validaciones se realizan
 * con Google Guava {@link Preconditions} y Apache Commons {@link StringUtils}.</p>
 *
 * @author TechImport Perú - Proyecto Universitario UTP (9.° ciclo)
 * @version 1.0
 */
public class Producto {

    // ──────────────────────────────────────────────
    //  Atributos privados (encapsulamiento completo)
    // ──────────────────────────────────────────────

    /** Identificador único del producto. */
    private int id;

    /** Nombre comercial del producto. */
    private String nombre;

    /** Descripción detallada del producto. */
    private String descripcion;

    /** Precio unitario del producto en soles (PEN). Debe ser mayor a 0. */
    private double precio;

    /** Cantidad disponible en inventario. No puede ser negativo. */
    private int stock;

    /** Categoría a la que pertenece el producto (ej. Laptops, Celulares). */
    private String categoria;

    /** URL de la imagen principal del producto. */
    private String imagenUrl;

    /** Identificador del proveedor asociado al producto. */
    private int proveedorId;

    /** Fecha y hora en la que se registró el producto en el sistema. */
    private LocalDateTime fechaCreacion;

    /** Indica si el producto está activo y visible en el catálogo. */
    private boolean activo;

    // ──────────────────────────────────────────────
    //  Constructores
    // ──────────────────────────────────────────────

    /**
     * Constructor sin argumentos.
     * Requerido para frameworks de serialización y persistencia.
     */
    public Producto() {
        // Constructor vacío por defecto
    }

    /**
     * Constructor con todos los argumentos para inicializar un producto completo.
     *
     * @param id            identificador único del producto
     * @param nombre        nombre comercial (no puede estar vacío)
     * @param descripcion   descripción detallada del producto
     * @param precio        precio unitario (debe ser mayor a 0)
     * @param stock         cantidad disponible (no puede ser negativo)
     * @param categoria     categoría del producto
     * @param imagenUrl     URL de la imagen principal
     * @param proveedorId   identificador del proveedor
     * @param fechaCreacion fecha y hora de registro
     * @param activo        estado de visibilidad en el catálogo
     */
    public Producto(int id, String nombre, String descripcion, double precio,
                    int stock, String categoria, String imagenUrl,
                    int proveedorId, LocalDateTime fechaCreacion, boolean activo) {
        this.id = id;
        setNombre(nombre);
        this.descripcion = descripcion;
        setPrecio(precio);
        setStock(stock);
        this.categoria = categoria;
        this.imagenUrl = imagenUrl;
        this.proveedorId = proveedorId;
        this.fechaCreacion = fechaCreacion;
        this.activo = activo;
    }

    // ──────────────────────────────────────────────
    //  Getters y Setters con validaciones
    // ──────────────────────────────────────────────

    /**
     * Obtiene el identificador único del producto.
     *
     * @return el id del producto
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del producto.
     *
     * @param id el nuevo id del producto
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre comercial del producto.
     *
     * @return el nombre del producto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre comercial del producto.
     * Valida que no esté vacío ni contenga solo espacios en blanco.
     *
     * @param nombre el nombre del producto
     * @throws IllegalArgumentException si el nombre está vacío o en blanco
     */
    public void setNombre(String nombre) {
        // Validación: el nombre del producto es obligatorio
        Preconditions.checkArgument(StringUtils.isNotBlank(nombre),
                "El nombre del producto no puede estar vacío");
        this.nombre = StringUtils.trim(nombre);
    }

    /**
     * Obtiene la descripción detallada del producto.
     *
     * @return la descripción del producto
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción detallada del producto.
     *
     * @param descripcion la descripción del producto
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el precio unitario del producto.
     *
     * @return el precio en soles (PEN)
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio unitario del producto.
     * Valida que sea estrictamente mayor a cero.
     *
     * @param precio el precio unitario
     * @throws IllegalArgumentException si el precio no es mayor a 0
     */
    public void setPrecio(double precio) {
        // Validación: el precio debe ser positivo
        Preconditions.checkArgument(precio > 0,
                "El precio debe ser mayor a 0");
        this.precio = precio;
    }

    /**
     * Obtiene la cantidad disponible en inventario.
     *
     * @return el stock del producto
     */
    public int getStock() {
        return stock;
    }

    /**
     * Establece la cantidad disponible en inventario.
     * Valida que no sea un valor negativo.
     *
     * @param stock la cantidad en inventario
     * @throws IllegalArgumentException si el stock es negativo
     */
    public void setStock(int stock) {
        // Validación: el stock no puede ser negativo
        Preconditions.checkArgument(stock >= 0,
                "El stock no puede ser negativo");
        this.stock = stock;
    }

    /**
     * Obtiene la categoría del producto.
     *
     * @return la categoría
     */
    public String getCategoria() {
        return categoria;
    }

    /**
     * Establece la categoría del producto.
     *
     * @param categoria la categoría del producto
     */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * Obtiene la URL de la imagen principal del producto.
     *
     * @return la URL de la imagen
     */
    public String getImagenUrl() {
        return imagenUrl;
    }

    /**
     * Establece la URL de la imagen principal del producto.
     *
     * @param imagenUrl la URL de la imagen
     */
    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    /**
     * Obtiene el identificador del proveedor asociado.
     *
     * @return el id del proveedor
     */
    public int getProveedorId() {
        return proveedorId;
    }

    /**
     * Establece el identificador del proveedor asociado.
     *
     * @param proveedorId el id del proveedor
     */
    public void setProveedorId(int proveedorId) {
        this.proveedorId = proveedorId;
    }

    /**
     * Obtiene la fecha y hora de registro del producto.
     *
     * @return la fecha de creación
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha y hora de registro del producto.
     *
     * @param fechaCreacion la fecha de creación
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Indica si el producto está activo y visible en el catálogo.
     *
     * @return {@code true} si está activo, {@code false} en caso contrario
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece el estado de visibilidad del producto en el catálogo.
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
     * Devuelve una representación en cadena de texto del producto.
     *
     * @return representación legible del producto
     */
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", stock=" + stock +
                ", categoria='" + categoria + '\'' +
                ", imagenUrl='" + imagenUrl + '\'' +
                ", proveedorId=" + proveedorId +
                ", fechaCreacion=" + fechaCreacion +
                ", activo=" + activo +
                '}';
    }

    /**
     * Compara este producto con otro objeto para determinar igualdad.
     * Dos productos se consideran iguales si tienen el mismo {@code id}.
     *
     * @param o el objeto a comparar
     * @return {@code true} si los objetos son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return id == producto.id;
    }

    /**
     * Calcula el código hash del producto basado en {@code id}.
     *
     * @return el código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
