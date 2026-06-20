package edu.utn.segundo_parcial.repository;

import edu.utn.segundo_parcial.util.JPAUtil;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio genérico abstracto que implementa operaciones CRUD comunes
 * para cualquier entidad JPA que tenga un campo 'eliminado' (soft delete)
 *
 * @param <T> Tipo de la entidad que manejará este repositorio
 */
public abstract class BaseRepository<T> {

    private final Class<T> entityClass;

    /**
     * Constructor que recibe la clase de la entidad
     * @param entityClass Clase de la entidad (ej: Categoria.class, Producto.class)
     */
    public BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    /**
     * Persiste o actualiza una entidad usando merge()
     * Abre y cierra su propio EntityManager
     *
     * @param entity Entidad a guardar o actualizar
     * @return La entidad persistida con su ID generado
     * @throws RuntimeException Si ocurre un error durante la operación
     */
    public T guardar(T entity) {
        EntityManager entityManager = null;
        EntityTransaction entityTransaction = null;

        try {
            entityManager = JPAUtil.getEntityManager();
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            T mergedEntity = entityManager.merge(entity);

            entityTransaction.commit();
            return mergedEntity;

        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw new RuntimeException("Error al guardar la entidad: " + entityClass.getSimpleName(), e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    /**
     * Busca una entidad por su ID
     * Retorna Optional.empty() si no existe
     *
     * @param id Identificador de la entidad
     * @return Optional<T> con la entidad encontrada o vacío
     */
    public Optional<T> buscarPorId(Long id) {
        EntityManager entityManager = null;

        try {
            entityManager = JPAUtil.getEntityManager();
            T entity = entityManager.find(entityClass, id);
            return Optional.ofNullable(entity);

        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    /**
     * Lista todos los registros activos (eliminado = false)
     * Usa JPQL para filtrar por el campo eliminado
     *
     * @return List<T> con las entidades activas
     */
    public List<T> listarActivos() {
        EntityManager entityManager = null;

        try {
            entityManager = JPAUtil.getEntityManager();

            // JPQL para listar solo entidades no eliminadas
            String jpql = "SELECT e FROM " + entityClass.getSimpleName() +
                    " e WHERE e.eliminado = false";

            TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
            return query.getResultList();

        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    /**
     * Eliminado lógico. Establece eliminado=true
     *
     * @param id Identificador de la entidad a eliminar lógicamente
     * @return true si se encontró y eliminó, false si no existe
     */
    public boolean eliminarLogico(Long id) {
        EntityManager entityManager = null;
        EntityTransaction entityTransaction = null;

        try {
            entityManager = JPAUtil.getEntityManager();
            entityTransaction = entityManager.getTransaction();
            entityTransaction.begin();

            T entity = entityManager.find(entityClass, id);

            if (entity == null) {
                entityTransaction.rollback();
                return false;
            }

            // Establecer eliminado = true usando reflexión
            try {
                java.lang.reflect.Method setEliminado =
                        entityClass.getMethod("setEliminado", boolean.class);
                setEliminado.invoke(entity, true);

                entityManager.merge(entity);
                entityTransaction.commit();
                return true;
            } catch (NoSuchMethodException e) {
                entityTransaction.rollback();
                throw new RuntimeException("La entidad " + entityClass.getSimpleName() +
                        " no tiene el método setEliminado()", e);
            }
        } catch (Exception e) {
            if (entityTransaction != null && entityTransaction.isActive()) {
                entityTransaction.rollback();
            }
            throw new RuntimeException("Error al eliminar lógicamente...", e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}