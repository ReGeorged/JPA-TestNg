package inj;

import annotations.Connection;
import annotations.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import providers.PersistenceProviderFactory;
import repository.TRepository;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class RepositoryInjector {
    private static final ThreadLocal<List<EntityManager>> injectedBag = ThreadLocal.withInitial(ArrayList::new);

    public static void injectRepositories(Object instance) {
        Field[] fields = instance.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Repository.class)) {
                String persistenceUnitName = field.getAnnotation(Connection.class).value();
                EntityManagerFactory emf = PersistenceProviderFactory.getInstance(persistenceUnitName);
                EntityManager em = emf.createEntityManager();
                injectedBag.get().add(em);

                // Get the entity class from the generic type argument of TRepository
                ParameterizedType tRepositoryType = (ParameterizedType) field.getGenericType();
                Class<?> repositoryEntityClass = (Class<?>) tRepositoryType.getActualTypeArguments()[0];

                TRepository repository = new TRepository(repositoryEntityClass, em);
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    field.set(instance, repository);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject TRepository", e);
                } finally {
                    field.setAccessible(accessible);
                }
            }
        }
    }

    public static void closeConnections() {
        injectedBag.get().forEach(EntityManager::close);
        injectedBag.remove();
    }
}
