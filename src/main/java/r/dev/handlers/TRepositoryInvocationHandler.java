package r.dev.handlers;

import jakarta.persistence.EntityManager;
import r.dev.annotations.Query;
import r.dev.repository.TRepository;
import r.dev.utils.query.QueryHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

public class TRepositoryInvocationHandler<E, K> implements InvocationHandler {
    private TRepository<E, K> repository;
    private Class<?> clazz;
    private EntityManager entityManager;

    public TRepositoryInvocationHandler(Class<E> entityClass, EntityManager entityManager) {
        this.clazz = entityClass;
        this.entityManager = entityManager;
        this.repository = new TRepository<>(entityClass, entityManager);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Query queryAnnotation = method.getAnnotation(Query.class);
        if (queryAnnotation != null) {
            QueryHelper queryHelper = new QueryHelper();
            String queryString = queryAnnotation.value();
            jakarta.persistence.Query query;
            if (queryAnnotation.nativeQuery()) {
                query = entityManager.createNativeQuery(queryString, clazz);
            } else {
                query = entityManager.createQuery(queryString);
            }
            if (List.class.isAssignableFrom(method.getReturnType())) {
                return queryHelper.getResultList(query, method, args);
            } else {
                return queryHelper.getSingleResult(query, method, args);
            }
        } else if (method.getName().startsWith("findBy") || method.getName().startsWith("getBy") || method.getName().startsWith("readBy") || method.getName().startsWith("queryBy")) {
            QueryHelper queryHelper = new QueryHelper();
            jakarta.persistence.Query query = queryHelper.createQueryFromMethodName(method.getName(), args, entityManager, clazz);
            return queryHelper.getResultList(query, method, args);
        }
        return method.invoke(repository, args);
    }

}