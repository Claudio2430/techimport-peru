package com.techimport.peru.modelo;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * Entidad que representa un pedido realizado por un cliente en el sistema
 * TechImport Perú.
 *
 * <p>Un pedido contiene la información general de la orden de compra,
 * incluyendo su estado, total y dirección de envío. Los detalles específicos
 * de cada producto se encuentran en {@link DetallePedido}.</p>
 *
 * <p>Los estados válidos del pedido son:
 * {@code PENDIENTE}, {@code PROCESANDO}, {@code ENVIADO},
 * {@code ENTREGADO} y {@code CANCELADO}.</p>
 *
 * @author TechImport Perú - Proyecto Universitario UTP (9.° ciclo)
 * @version 1.0
 * @see DetallePedido
 */
public class Pedido {

    // ──────────────────────────────────────────────
    //  Constantes de validación
    // ──────────────────────────────────────────────

    /**
     * Conjunto de estados válidos para un pedido.
     * Utilizado internamente para validar el campo {@code estado}.
     */
    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            "PENDIENTE", "PROCESANDO", "ENVIADO", "ENTREGADO", "CANCELADO"
    );

    // ──────────────────────────────────────────────
    //  Atributos privados (encapsulamiento completo)
    // ──────────────────────────────────────────────

    /** Identificador único del pedido. */
    private int id;

    /** Identificador del usuario que realizó el pedido. */
    private int usuarioId;

    /** Fecha y hora en que se registró el pedido. */
    private LocalDateTime fechaPedido;

    /**
     * Estado actual del pedido.
     * Valores permitidos: PENDIENTE, PROCESANDO, ENVIADO, ENTREGADO, CANCELADO.
     */
    private String estado;

    /** Monto total del pedido en soles (PEN). No puede ser negativo. */
    private double total;

    /** Dirección de envío del pedido. */
    private String direccionEnvio;

    // ──────────────────────────────────────────────
    //  Constructores
    // ──────────────────────────────────────────────

    /**
     * Constructor sin argumentos.
     * Requerido para frameworks de serialización y persistencia.
     */
    public Pedido() {
        // Constructor vacío por defecto
    }

    /**
     * Constructor con todos los argumentos para inicializar un pedido completo.
     *
     * @param id            identificador único del pedido
     * @param usuarioId     identificador del usuario que realiza el pedido
     * @param fechaPedido   fecha y hora de registro del pedido
     * @param estado        estado actual (debe ser un estado válido)
     * @param total         monto total (no puede ser negativo)
     * @param direccionEnvio dirección de envío (no puede estar vacía)
     */
    public Pedido(int id, int usuarioId, LocalDateTime fechaPedido,
                  String estado, double total, String direccionEnvio) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fechaPedido = fechaPedido;
        setEstado(estado);
        setTotal(total);
        setDireccionEnvio(direccionEnvio);
    }

    // ──────────────────────────────────────────────
    //  Getters y Setters con validaciones
    // ──────────────────────────────────────────────

    /**
     * Obtiene el identificador único del pedido.
     *
     * @return el id del pedido
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del pedido.
     *
     * @param id el nuevo id del pedido
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el identificador del usuario que realizó el pedido.
     *
     * @return el id del usuario
     */
    public int getUsuarioId() {
        return usuarioId;
    }

    /**
     * Establece el identificador del usuario que realizó el pedido.
     *
     * @param usuarioId el id del usuario
     */
    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    /**
     * Obtiene la fecha y hora de registro del pedido.
     *
     * @return la fecha del pedido
     */
    public LocalDateTime getFechaPedido() {
        return fechaPedido;
    }

    /**
     * Establece la fecha y hora de registro del pedido.
     *
     * @param fechaPedido la fecha del pedido
     */
    public void setFechaPedido(LocalDateTime fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    /**
     * Obtiene el estado actual del pedido.
     *
     * @return el estado del pedido
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado actual del pedido.
     * Solo se permiten los valores: {@code PENDIENTE}, {@code PROCESANDO},
     * {@code ENVIADO}, {@code ENTREGADO}, {@code CANCELADO}.
     *
     * @param estado el nuevo estado del pedido
     * @throws IllegalArgumentException si el estado no es válido
     */
    public void setEstado(String estado) {
        // Validación: solo estados permitidos
        Preconditions.checkArgument(
                ESTADOS_VALIDOS.contains(estado),
                "Estado de pedido inválido: %s. Estados permitidos: %s",
                estado, ESTADOS_VALIDOS);
        this.estado = estado;
    }

    /**
     * Obtiene el monto total del pedido.
     *
     * @return el total en soles (PEN)
     */
    public double getTotal() {
        return total;
    }

    /**
     * Establece el monto total del pedido.
     * Valida que no sea un valor negativo.
     *
     * @param total el monto total
     * @throws IllegalArgumentException si el total es negativo
     */
    public void setTotal(double total) {
        // Validación: el total no puede ser negativo
        Preconditions.checkArgument(total >= 0,
                "El total no puede ser negativo");
        this.total = total;
    }

    /**
     * Obtiene la dirección de envío del pedido.
     *
     * @return la dirección de envío
     */
    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    /**
     * Establece la dirección de envío del pedido.
     * Valida que no esté vacía ni contenga solo espacios en blanco.
     *
     * @param direccionEnvio la dirección de envío
     * @throws IllegalArgumentException si la dirección está vacía o en blanco
     */
    public void setDireccionEnvio(String direccionEnvio) {
        // Validación: la dirección de envío es obligatoria
        Preconditions.checkArgument(StringUtils.isNotBlank(direccionEnvio),
                "La dirección de envío no puede estar vacía");
        this.direccionEnvio = StringUtils.trim(direccionEnvio);
    }

    // ──────────────────────────────────────────────
    //  Métodos estándar: toString, equals, hashCode
    // ──────────────────────────────────────────────

    /**
     * Devuelve una representación en cadena de texto del pedido.
     *
     * @return representación legible del pedido
     */
    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", fechaPedido=" + fechaPedido +
                ", estado='" + estado + '\'' +
                ", total=" + total +
                ", direccionEnvio='" + direccionEnvio + '\'' +
                '}';
    }

    /**
     * Compara este pedido con otro objeto para determinar igualdad.
     * Dos pedidos se consideran iguales si tienen el mismo {@code id}.
     *
     * @param o el objeto a comparar
     * @return {@code true} si los objetos son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return id == pedido.id;
    }

    /**
     * Calcula el código hash del pedido basado en {@code id}.
     *
     * @return el código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
