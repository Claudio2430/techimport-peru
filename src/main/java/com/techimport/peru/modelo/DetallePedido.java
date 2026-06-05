package com.techimport.peru.modelo;

import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 * Entidad que representa una línea de detalle dentro de un pedido
 * en el sistema TechImport Perú.
 *
 * <p>Cada detalle de pedido vincula un producto específico con el pedido,
 * registrando la cantidad solicitada, el precio unitario al momento de la
 * compra y el subtotal correspondiente. Las validaciones se realizan con
 * Google Guava {@link Preconditions}.</p>
 *
 * @author TechImport Perú - Proyecto Universitario UTP (9.° ciclo)
 * @version 1.0
 * @see Pedido
 * @see Producto
 */
public class DetallePedido {

    // ──────────────────────────────────────────────
    //  Atributos privados (encapsulamiento completo)
    // ──────────────────────────────────────────────

    /** Identificador único del detalle de pedido. */
    private int id;

    /** Identificador del pedido al que pertenece este detalle. */
    private int pedidoId;

    /** Identificador del producto incluido en este detalle. */
    private int productoId;

    /** Cantidad de unidades del producto solicitadas. Debe ser mayor a 0. */
    private int cantidad;

    /** Precio unitario del producto al momento de la compra. Debe ser mayor a 0. */
    private double precioUnitario;

    /** Subtotal de esta línea (cantidad × precioUnitario). No puede ser negativo. */
    private double subtotal;

    // ──────────────────────────────────────────────
    //  Constructores
    // ──────────────────────────────────────────────

    /**
     * Constructor sin argumentos.
     * Requerido para frameworks de serialización y persistencia.
     */
    public DetallePedido() {
        // Constructor vacío por defecto
    }

    /**
     * Constructor con todos los argumentos para inicializar un detalle de pedido completo.
     *
     * @param id              identificador único del detalle
     * @param pedidoId        identificador del pedido padre
     * @param productoId      identificador del producto
     * @param cantidad        cantidad de unidades (debe ser mayor a 0)
     * @param precioUnitario  precio unitario al momento de la compra (debe ser mayor a 0)
     * @param subtotal        subtotal de la línea (no puede ser negativo)
     */
    public DetallePedido(int id, int pedidoId, int productoId,
                         int cantidad, double precioUnitario, double subtotal) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        setCantidad(cantidad);
        setPrecioUnitario(precioUnitario);
        setSubtotal(subtotal);
    }

    // ──────────────────────────────────────────────
    //  Getters y Setters con validaciones
    // ──────────────────────────────────────────────

    /**
     * Obtiene el identificador único del detalle de pedido.
     *
     * @return el id del detalle
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del detalle de pedido.
     *
     * @param id el nuevo id del detalle
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el identificador del pedido al que pertenece este detalle.
     *
     * @return el id del pedido padre
     */
    public int getPedidoId() {
        return pedidoId;
    }

    /**
     * Establece el identificador del pedido al que pertenece este detalle.
     *
     * @param pedidoId el id del pedido padre
     */
    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    /**
     * Obtiene el identificador del producto incluido en este detalle.
     *
     * @return el id del producto
     */
    public int getProductoId() {
        return productoId;
    }

    /**
     * Establece el identificador del producto incluido en este detalle.
     *
     * @param productoId el id del producto
     */
    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    /**
     * Obtiene la cantidad de unidades del producto solicitadas.
     *
     * @return la cantidad de unidades
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de unidades del producto solicitadas.
     * Valida que sea estrictamente mayor a cero.
     *
     * @param cantidad la cantidad de unidades
     * @throws IllegalArgumentException si la cantidad no es mayor a 0
     */
    public void setCantidad(int cantidad) {
        // Validación: la cantidad debe ser positiva
        Preconditions.checkArgument(cantidad > 0,
                "La cantidad debe ser mayor a 0");
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio unitario del producto al momento de la compra.
     *
     * @return el precio unitario
     */
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Establece el precio unitario del producto al momento de la compra.
     * Valida que sea estrictamente mayor a cero.
     *
     * @param precioUnitario el precio unitario
     * @throws IllegalArgumentException si el precio unitario no es mayor a 0
     */
    public void setPrecioUnitario(double precioUnitario) {
        // Validación: el precio unitario debe ser positivo
        Preconditions.checkArgument(precioUnitario > 0,
                "El precio unitario debe ser mayor a 0");
        this.precioUnitario = precioUnitario;
    }

    /**
     * Obtiene el subtotal de esta línea de detalle.
     *
     * @return el subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Establece el subtotal de esta línea de detalle.
     * Valida que no sea un valor negativo.
     *
     * @param subtotal el subtotal de la línea
     * @throws IllegalArgumentException si el subtotal es negativo
     */
    public void setSubtotal(double subtotal) {
        // Validación: el subtotal no puede ser negativo
        Preconditions.checkArgument(subtotal >= 0,
                "El subtotal no puede ser negativo");
        this.subtotal = subtotal;
    }

    // ──────────────────────────────────────────────
    //  Métodos de negocio
    // ──────────────────────────────────────────────

    /**
     * Calcula el subtotal de esta línea de detalle multiplicando
     * la cantidad por el precio unitario.
     *
     * <p>Este método es útil para recalcular el subtotal antes de
     * persistir el detalle o para verificaciones de integridad.</p>
     *
     * @return el subtotal calculado ({@code cantidad * precioUnitario})
     */
    public double calcularSubtotal() {
        return this.cantidad * this.precioUnitario;
    }

    // ──────────────────────────────────────────────
    //  Métodos estándar: toString, equals, hashCode
    // ──────────────────────────────────────────────

    /**
     * Devuelve una representación en cadena de texto del detalle de pedido.
     *
     * @return representación legible del detalle
     */
    @Override
    public String toString() {
        return "DetallePedido{" +
                "id=" + id +
                ", pedidoId=" + pedidoId +
                ", productoId=" + productoId +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + subtotal +
                '}';
    }

    /**
     * Compara este detalle de pedido con otro objeto para determinar igualdad.
     * Dos detalles se consideran iguales si tienen el mismo {@code id}.
     *
     * @param o el objeto a comparar
     * @return {@code true} si los objetos son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetallePedido that = (DetallePedido) o;
        return id == that.id;
    }

    /**
     * Calcula el código hash del detalle de pedido basado en {@code id}.
     *
     * @return el código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
