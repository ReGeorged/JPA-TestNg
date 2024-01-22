package org.regeorged.dev.repository.proxy;

import jakarta.persistence.EntityManager;
import org.regeorged.dev.handlers.TRepositoryInvocationHandler;
import org.regeorged.dev.repository.TRepository;

import java.lang.reflect.Proxy;

public class RepositoryProxy<T extends TRepository<E, K>, E, K> {
    private static final ThreadLocal<RepositoryProxy> INSTANCE = new ThreadLocal<>();
    private EntityManager entityManager;

    private RepositoryProxy(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static <T extends TRepository<E, K>, E, K> RepositoryProxy<T, E, K> getInstance(EntityManager entityManager) {
        if (INSTANCE.get() == null) {
            INSTANCE.set(new RepositoryProxy<>(entityManager));
        }
        return INSTANCE.get();
    }

    public T createProxy(Class<T> repositoryInterface, Class<E> entityClass) {
        return (T) Proxy.newProxyInstance(
                repositoryInterface.getClassLoader(),
                new Class<?>[]{repositoryInterface},
                new TRepositoryInvocationHandler<>(entityClass, entityManager)
        );
    }
}