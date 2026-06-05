package com.techimport.peru.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica para operaciones CRUD (Crear, Leer, Actualizar, Eliminar).
 *
 * <p>Esta interfaz aplica el Principio de Segregación de Interfaces (ISP) del patrón SOLID,
 * proporcionando un contrato mínimo y cohesivo para el acceso a datos. Las implementaciones
 * concretas deben especificar el tipo de entidad {@code T} y el tipo de identificador {@code ID}.</p>
 *
 * <p>Ejemplo de uso:</p>
 * <pre>{@code
 *   public interface UsuarioDAO extends GenericDAO<Usuario, Integer> {
 *       // Métodos adicionales específicos de Usuario
 *   }
 * }</pre>
 *
 * @param <T>  el tipo de la entidad gestionada por este DAO
 * @param <ID> el tipo del identificador único de la entidad
 *
 * @author TechImport Perú
 * @version 1.0
 * @since 2026-06-07
 */
public interface GenericDAO<T, ID> {

    /**
     * Busca una entidad por su identificador único.
     *
     * @param id el identificador único de la entidad a buscar; no debe ser {@code null}
     * @return un {@link Optional} que contiene la entidad si fue encontrada,
     *         o {@link Optional#empty()} si no existe
     */
    Optional<T> buscarPorId(ID id);

    /**
     * Recupera todas las entidades activas del repositorio.
     *
     * <p>La definición de "activas" depende de la implementación concreta.
     * Por ejemplo, puede filtrar registros marcados como eliminados (soft delete).</p>
     *
     * @return una lista con todas las entidades activas; nunca {@code null},
     *         puede ser una lista vacía si no hay registros
     */
    List<T> listarTodos();

    /**
     * Crea (persiste) una nueva entidad en el repositorio.
     *
     * @param entidad la entidad a crear; no debe ser {@code null}
     * @return {@code true} si la entidad fue creada exitosamente,
     *         {@code false} en caso contrario
     */
    boolean crear(T entidad);

    /**
     * Actualiza una entidad existente en el repositorio.
     *
     * <p>La entidad debe contener un identificador válido que corresponda
     * a un registro existente en el repositorio.</p>
     *
     * @param entidad la entidad con los datos actualizados; no debe ser {@code null}
     * @return {@code true} si la entidad fue actualizada exitosamente,
     *         {@code false} en caso contrario
     */
    boolean actualizar(T entidad);

    /**
     * Elimina una entidad del repositorio por su identificador.
     *
     * <p>La implementación puede realizar una eliminación física (hard delete)
     * o lógica (soft delete), según la política del negocio.</p>
     *
     * @param id el identificador único de la entidad a eliminar; no debe ser {@code null}
     * @return {@code true} si la entidad fue eliminada exitosamente,
     *         {@code false} en caso contrario
     */
    boolean eliminar(ID id);
}
