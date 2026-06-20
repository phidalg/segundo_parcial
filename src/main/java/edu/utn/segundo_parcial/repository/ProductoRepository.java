package edu.utn.segundo_parcial.repository;

import edu.utn.segundo_parcial.model.Producto;
import edu.utn.segundo_parcial.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Repositorio específico para la entidad Producto.
 * Extiende BaseRepository heredando todas las operaciones CRUD comunes.
 * Agrega métodos específicos para consultas de productos.
 */
public class ProductoRepository extends BaseRepository<Producto> {

    /**
     * Constructor que llama al constructor de la clase base con Producto.class
     */
    public ProductoRepository() {
        super(Producto.class);
    }

    /**
     * Busca productos activos que pertenecen a una categoría específica.
     *
     * La consulta JPQL filtra por:
     * - Productos activos (eliminado = false)
     * - Productos que pertenecen a la categoría con el ID proporcionado
     *
     * @param categoriaId ID de la categoría para filtrar productos
     * @return List<Producto> con los productos activos de esa categoría
     */
    public List<Producto> buscarPorCategoria(Long categoriaId) {
        EntityManager entityManager = null;

        try {
            entityManager = JPAUtil.getEntityManager();

            // JPQL con parámetro nombrado para filtrar por categoría y estado activo
            String jpql = "SELECT p FROM Producto p " +
                    "WHERE p.categoria.id = :categoriaId " +
                    "AND p.eliminado = false";

            TypedQuery<Producto> query = entityManager.createQuery(jpql, Producto.class);
            query.setParameter("categoriaId", categoriaId);

            return query.getResultList();

        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
}