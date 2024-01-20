package r.dev.proxy;

import jakarta.persistence.EntityManager;
import r.dev.handlers.TRepositoryInvocationHandler;
import r.dev.irepository.ITRepository;

import java.lang.reflect.Proxy;

public class RepositoryProxy<T extends ITRepository<E, K>, E, K> {
    private EntityManager entityManager;

    public RepositoryProxy(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public T createProxy(Class<T> repositoryInterface, Class<E> entityClass) {
        return (T) Proxy.newProxyInstance(
                repositoryInterface.getClassLoader(),
                new Class<?>[]{repositoryInterface},
                new TRepositoryInvocationHandler<>(entityClass, entityManager)
        );
    }
}