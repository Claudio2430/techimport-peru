package com.techimport.peru.modelo;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

/**
 * Entidad que representa un pago asociado a un pedido en el sistema
 * TechImport Perú.
 *
 * <p>Registra la información del método de pago utilizado, el monto,
 * la fecha de procesamiento, el estado de la transacción y una referencia
 * única proporcionada por la pasarela de pagos.</p>
 *
 * <p>Métodos de pago soportados: {@code TARJETA_CREDITO}, {@code TARJETA_DEBITO},
 * {@code PAYPAL}, {@code TRANSFERENCIA}.</p>
 *
 * <p>Estados de pago válidos: {@code PENDIENTE}, {@code COMPLETADO},
 * {@code FALLIDO}, {@code REEMBOLSADO}.</p>
 *
 * @author TechImport Perú - Proyecto Universitario UTP (9.° ciclo)
 * @version 1.0
 * @see Pedido
 */
public class Pago {

    // ──────────────────────────────────────────────
    //  Constantes de validación
    // ──────────────────────────────────────────────

    /**
     * Conjunto de métodos de pago aceptados por el sistema.
     * Utilizado internamente para validar el campo {@code metodoPago}.
     */
    private static final Set<String> METODOS_PAGO_VALIDOS = Set.of(
            "TARJETA_CREDITO", "TARJETA_DEBITO", "PAYPAL", "TRANSFERENCIA"
    );

    /**
     * Conjunto de estados válidos para un pago.
     * Utilizado internamente para validar el campo {@code estado}.
     */
    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            "PENDIENTE", "COMPLETADO", "FALLIDO", "REEMBOLSADO"
    );

    // ──────────────────────────────────────────────
    //  Atributos privados (encapsulamiento completo)
    // ──────────────────────────────────────────────

    /** Identificador único del pago. */
    private int id;

    /** Identificador del pedido asociado a este pago. */
    private int pedidoId;

    /**
     * Método de pago utilizado.
     * Valores permitidos: TARJETA_CREDITO, TARJETA_DEBITO, PAYPAL, TRANSFERENCIA.
     */
    private String metodoPago;

    /** Monto del pago en soles (PEN). Debe ser mayor a 0. */
    private double monto;

    /** Fecha y hora en que se procesó el pago. */
    private LocalDateTime fechaPago;

    /**
     * Estado actual del pago.
     * Valores permitidos: PENDIENTE, COMPLETADO, FALLIDO, REEMBOLSADO.
     */
    private String estado;

    /** Referencia única de la transacción proporcionada por la pasarela de pagos. */
    private String referenciaTransaccion;

    // ──────────────────────────────────────────────
    //  Constructores
    // ──────────────────────────────────────────────

    /**
     * Constructor sin argumentos.
     * Requerido para frameworks de serialización y persistencia.
     */
    public Pago() {
        // Constructor vacío por defecto
    }

    /**
     * Constructor con todos los argumentos para inicializar un pago completo.
     *
     * @param id                    identificador único del pago
     * @param pedidoId              identificador del pedido asociado
     * @param metodoPago            método de pago (debe ser válido)
     * @param monto                 monto del pago (debe ser mayor a 0)
     * @param fechaPago             fecha y hora de procesamiento
     * @param estado                estado del pago (debe ser válido)
     * @param referenciaTransaccion referencia única de la transacción
     */
    public Pago(int id, int pedidoId, String metodoPago, double monto,
                LocalDateTime fechaPago, String estado,
                String referenciaTransaccion) {
        this.id = id;
        this.pedidoId = pedidoId;
        setMetodoPago(metodoPago);
        setMonto(monto);
        this.fechaPago = fechaPago;
        setEstado(estado);
        setReferenciaTransaccion(referenciaTransaccion);
    }

    // ──────────────────────────────────────────────
    //  Getters y Setters con validaciones
    // ──────────────────────────────────────────────

    /**
     * Obtiene el identificador único del pago.
     *
     * @return el id del pago
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del pago.
     *
     * @param id el nuevo id del pago
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el identificador del pedido asociado.
     *
     * @return el id del pedido
     */
    public int getPedidoId() {
        return pedidoId;
    }

    /**
     * Establece el identificador del pedido asociado.
     *
     * @param pedidoId el id del pedido
     */
    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    /**
     * Obtiene el método de pago utilizado.
     *
     * @return el método de pago
     */
    public String getMetodoPago() {
        return metodoPago;
    }

    /**
     * Establece el método de pago utilizado.
     * Solo se permiten los valores: {@code TARJETA_CREDITO}, {@code TARJETA_DEBITO},
     * {@code PAYPAL}, {@code TRANSFERENCIA}.
     *
     * @param metodoPago el método de pago
     * @throws IllegalArgumentException si el método de pago no es válido
     */
    public void setMetodoPago(String metodoPago) {
        // Validación: solo métodos de pago permitidos
        Preconditions.checkArgument(
                METODOS_PAGO_VALIDOS.contains(metodoPago),
                "Método de pago inválido: %s. Métodos permitidos: %s",
                metodoPago, METODOS_PAGO_VALIDOS);
        this.metodoPago = metodoPago;
    }

    /**
     * Obtiene el monto del pago.
     *
     * @return el monto en soles (PEN)
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Establece el monto del pago.
     * Valida que sea estrictamente mayor a cero.
     *
     * @param monto el monto del pago
     * @throws IllegalArgumentException si el monto no es mayor a 0
     */
    public void setMonto(double monto) {
        // Validación: el monto debe ser positivo
        Preconditions.checkArgument(monto > 0,
                "El monto debe ser mayor a 0");
        this.monto = monto;
    }

    /**
     * Obtiene la fecha y hora de procesamiento del pago.
     *
     * @return la fecha del pago
     */
    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    /**
     * Establece la fecha y hora de procesamiento del pago.
     *
     * @param fechaPago la fecha del pago
     */
    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    /**
     * Obtiene el estado actual del pago.
     *
     * @return el estado del pago
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado actual del pago.
     * Solo se permiten los valores: {@code PENDIENTE}, {@code COMPLETADO},
     * {@code FALLIDO}, {@code REEMBOLSADO}.
     *
     * @param estado el nuevo estado del pago
     * @throws IllegalArgumentException si el estado no es válido
     */
    public void setEstado(String estado) {
        // Validación: solo estados de pago permitidos
        Preconditions.checkArgument(
                ESTADOS_VALIDOS.contains(estado),
                "Estado de pago inválido: %s. Estados permitidos: %s",
                estado, ESTADOS_VALIDOS);
        this.estado = estado;
    }

    /**
     * Obtiene la referencia única de la transacción.
     *
     * @return la referencia de transacción
     */
    public String getReferenciaTransaccion() {
        return referenciaTransaccion;
    }

    /**
     * Establece la referencia única de la transacción.
     * Valida que no esté vacía ni contenga solo espacios en blanco.
     *
     * @param referenciaTransaccion la referencia de la transacción
     * @throws IllegalArgumentException si la referencia está vacía o en blanco
     */
    public void setReferenciaTransaccion(String referenciaTransaccion) {
        // Validación: la referencia de transacción es obligatoria
        Preconditions.checkArgument(
                StringUtils.isNotBlank(referenciaTransaccion),
                "La referencia de transacción no puede estar vacía");
        this.referenciaTransaccion = StringUtils.trim(referenciaTransaccion);
    }

    // ──────────────────────────────────────────────
    //  Métodos estándar: toString, equals, hashCode
    // ──────────────────────────────────────────────

    /**
     * Devuelve una representación en cadena de texto del pago.
     *
     * @return representación legible del pago
     */
    @Override
    public String toString() {
        return "Pago{" +
                "id=" + id +
                ", pedidoId=" + pedidoId +
                ", metodoPago='" + metodoPago + '\'' +
                ", monto=" + monto +
                ", fechaPago=" + fechaPago +
                ", estado='" + estado + '\'' +
                ", referenciaTransaccion='" + referenciaTransaccion + '\'' +
                '}';
    }

    /**
     * Compara este pago con otro objeto para determinar igualdad.
     * Dos pagos se consideran iguales si tienen el mismo {@code id}.
     *
     * @param o el objeto a comparar
     * @return {@code true} si los objetos son iguales
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pago pago = (Pago) o;
        return id == pago.id;
    }

    /**
     * Calcula el código hash del pago basado en {@code id}.
     *
     * @return el código hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
