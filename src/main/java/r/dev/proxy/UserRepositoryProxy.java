package r.dev.proxy;

import jakarta.persistence.EntityManager;
import r.dev.handlers.TRepositoryInvocationHandler;
import r.dev.irepository.ITRepository;
import r.repositories.UserRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class UserRepositoryProxy {

    private EntityManager entityManager;

    public UserRepositoryProxy(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public <T extends ITRepository<E, K>, E, K> T createProxy(Class<T> repositoryInterface, Class<E> entityClass) {
        return (T) Proxy.newProxyInstance(
                repositoryInterface.getClassLoader(),
                new Class<?>[]{repositoryInterface},
                new TRepositoryInvocationHandler<>(entityClass, entityManager)
        );
    }
}
