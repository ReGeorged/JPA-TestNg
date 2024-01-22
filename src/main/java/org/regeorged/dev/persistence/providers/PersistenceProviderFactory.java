package org.regeorged.dev.persistence.providers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PersistenceProviderFactory {
    private static final Map<String, EntityManagerFactory> instances = new HashMap<>();
    private static ConcurrentLinkedQueue<EntityManager> entityManagers = new ConcurrentLinkedQueue<>();

    public static EntityManager getInstance(String persistenceUnitName) {
        EntityManagerFactory emf = instances.get(persistenceUnitName);
        if (emf == null) {
            synchronized (PersistenceProviderFactory.class) {
                emf = instances.get(persistenceUnitName);
                if (emf == null) {
                    emf = Persistence.createEntityManagerFactory(persistenceUnitName);
                    instances.put(persistenceUnitName, emf);
                }
            }
        }
        EntityManager em = emf.createEntityManager();
        entityManagers.add(em);
        return em;
    }

    public static void closeAllEntityManagers() {
        EntityManager em;
        while ((em = entityManagers.poll()) != null) {
            if (em.isOpen()) {
                em.close();
            }
        }
    }
}
