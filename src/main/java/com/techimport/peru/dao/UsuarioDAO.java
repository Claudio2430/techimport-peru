package com.techimport.peru.dao;

import com.techimport.peru.modelo.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de acceso a datos específica para la entidad {@link Usuario}.
 *
 * <p>Extiende {@link GenericDAO} proporcionando las operaciones CRUD estándar
 * y agrega métodos de consulta especializados para la gestión de usuarios,
 * tales como búsqueda por correo electrónico, filtrado por rol y verificación
 * de existencia de email.</p>
 *
 * <p>Esta interfaz sigue el Principio de Segregación de Interfaces (ISP),
 * exponiendo únicamente los métodos relevantes para el dominio de usuarios.</p>
 *
 * @author TechImport Perú
 * @version 1.0
 * @since 2026-06-07
 * @see GenericDAO
 * @see Usuario
 */
public interface UsuarioDAO extends GenericDAO<Usuario, Integer> {

    /**
     * Busca un usuario por su dirección de correo electrónico.
     *
     * <p>Este método es utilizado principalmente durante el proceso de autenticación
     * para localizar la cuenta del usuario.</p>
     *
     * @param email la dirección de correo electrónico del usuario a buscar;
     *              no debe ser {@code null} ni estar vacío
     * @return un {@link Optional} que contiene el {@link Usuario} si fue encontrado,
     *         o {@link Optional#empty()} si no existe un usuario con ese email
     */
    Optional<Usuario> buscarPorEmail(String email);

    /**
     * Recupera todos los usuarios activos que poseen el rol especificado.
     *
     * <p>Solo se incluyen usuarios cuyo campo {@code activo} sea {@code true}.</p>
     *
     * @param rol el nombre del rol por el cual filtrar (e.g., "ADMIN", "CLIENTE");
     *            no debe ser {@code null} ni estar vacío
     * @return una lista de usuarios activos con el rol indicado; nunca {@code null},
     *         puede ser una lista vacía si no se encuentran coincidencias
     */
    List<Usuario> buscarPorRol(String rol);

    /**
     * Verifica si ya existe un usuario registrado con la dirección de correo electrónico indicada.
     *
     * <p>Útil para validaciones previas al registro de un nuevo usuario,
     * evitando duplicados en la base de datos.</p>
     *
     * @param email la dirección de correo electrónico a verificar;
     *              no debe ser {@code null} ni estar vacío
     * @return {@code true} si ya existe al menos un usuario con ese email,
     *         {@code false} en caso contrario
     */
    boolean existeEmail(String email);
}
