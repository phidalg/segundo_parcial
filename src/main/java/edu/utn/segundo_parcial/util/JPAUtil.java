package edu.utn.segundo_parcial.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase utilitaria para manejar el EntityManagerFactory y EntityManager
 * Sigue el patrón Singleton para el EntityManagerFactory
 */
public class JPAUtil {

    // Nombre de la unidad de persistencia definida en persistence.xml
    private static final String PERSISTENCE_UNIT_NAME = "persistenceUnit";

    // Instancia única del EntityManagerFactory (Singleton)
    private static EntityManagerFactory entityManagerFactory;

    /**
     * Constructor privado para evitar instanciación
     */
    private JPAUtil() {
        // Constructor vacío y privado
    }

    /**
     * Obtiene la instancia única del EntityManagerFactory
     * @return EntityManagerFactory
     */
    public static EntityManagerFactory getEntityManagerFactory() {
        if (entityManagerFactory == null) {
            try {
                entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
            } catch (Exception e) {
                System.err.println("Error al crear EntityManagerFactory: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("No se pudo inicializar JPA", e);
            }
        }
        return entityManagerFactory;
    }

    /**
     * Crea un nuevo EntityManager
     * @return EntityManager
     */
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /**
     * Cierra el EntityManagerFactory cuando la aplicación termina
     * Debe llamarse al finalizar el programa
     */
    public static void closeEntityManagerFactory() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            entityManagerFactory = null;
            System.out.println("EntityManagerFactory cerrado correctamente.");
        }
    }

    /**
     * Método de utilidad para cerrar un EntityManager de forma segura
     * @param entityManager EntityManager a cerrar
     */
    public static void closeEntityManager(EntityManager entityManager) {
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
    }
}