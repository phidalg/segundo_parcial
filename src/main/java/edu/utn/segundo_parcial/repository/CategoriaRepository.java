package edu.utn.segundo_parcial.repository;

import edu.utn.segundo_parcial.model.Categoria;

/**
 * Repositorio específico para la entidad Categoria.
 * Extiende BaseRepository heredando todas las operaciones CRUD comunes.
 * No requiere métodos adicionales.
 */
public class CategoriaRepository extends BaseRepository<Categoria> {

    /**
     * Constructor que llama al constructor de la clase base con Categoria.class
     */
    public CategoriaRepository() {
        super(Categoria.class);
    }
}