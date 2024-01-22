package org.regeorged.dev.persistence.providers;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class PersistenceProviderFactory {
    private static final Map<String, EntityManagerFactory> instances = new HashMap<>();
    private PersistenceProviderFactory() {}
    public static EntityManagerFactory getInstance(String persistenceUnitName) {
        if (!instances.containsKey(persistenceUnitName)) {
            synchronized (PersistenceProviderFactory.class) {
                if (!instances.containsKey(persistenceUnitName)) {
                    instances.put(persistenceUnitName, Persistence.createEntityManagerFactory(persistenceUnitName));
                }
            }
        }
        return instances.get(persistenceUnitName);
    }
}
