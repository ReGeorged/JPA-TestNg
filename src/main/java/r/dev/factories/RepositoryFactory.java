package r.dev.factories;

import jakarta.persistence.EntityManager;
import r.dev.irepository.ITRepository;

import java.lang.reflect.Constructor;

public class RepositoryFactory {

    private EntityManager entityManager;

    public RepositoryFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <E, K> ITRepository<E, K> createRepository(Class<E> entityClass) {
        try {
            // Get the class of the repository
            Class<?> repositoryClass = Class.forName(entityClass.getName() + "Repository");

            // Create a new instance of the repository
            Constructor<?> constructor = repositoryClass.getConstructor(Class.class, EntityManager.class);
            return (ITRepository<E, K>) constructor.newInstance(entityClass, entityManager);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create repository for " + entityClass.getName(), e);
        }
    }
}
