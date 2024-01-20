package r.dev.handlers;

import jakarta.persistence.EntityManager;
import r.dev.repository.TRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TRepositoryInvocationHandler<E, K> implements InvocationHandler {

    private TRepository<E, K> repository;

    public TRepositoryInvocationHandler(Class<E> entityClass, EntityManager entityManager) {
        this.repository = new TRepository<>(entityClass, entityManager);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(repository, args);
    }
}
