package r.dev.handlers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import r.dev.annotations.Query;
import r.dev.repository.TRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class TRepositoryInvocationHandler<E, K> implements InvocationHandler {
    private TRepository<E, K> repository;

    public TRepositoryInvocationHandler(Class<E> entityClass, EntityManager entityManager) {
        this.repository = new TRepository<>(entityClass, entityManager);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Query queryAnnotation = method.getAnnotation(Query.class);
        if (queryAnnotation != null) {
            String queryString = queryAnnotation.value();
            jakarta.persistence.Query query = repository.getEntityManager().createQuery(queryString);

            // Set the parameter values
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                query.setParameter(parameters[i].getName(), args[i]);
            }

            return query.getResultList();
        }
        return method.invoke(repository, args);
    }
}
