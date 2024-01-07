package inj;

import annotations.Connection;
import providers.PersistenceProviderFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ConnectionInjector {

    private static final ThreadLocal<List<EntityManager>> injectedBag = ThreadLocal.withInitial(ArrayList::new);

    public static void injectConnections(Object instance) {
        Field[] fields = instance.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(Connection.class)) {
                String persistenceUnitName = field.getAnnotation(Connection.class).value();
                EntityManagerFactory emf = PersistenceProviderFactory.getInstance(persistenceUnitName);
                EntityManager em = emf.createEntityManager();

                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    field.set(instance, em);
                    injectedBag.get().add(em);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject EntityManager", e);
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
