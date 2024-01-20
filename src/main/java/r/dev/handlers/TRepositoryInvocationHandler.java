package r.dev.handlers;

import jakarta.persistence.EntityManager;
import org.springframework.data.repository.query.Param;
import r.dev.annotations.Query;
import r.dev.repository.TRepository;
import r.dev.utils.query.QueryHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class TRepositoryInvocationHandler<E, K> implements InvocationHandler {
    private TRepository<E, K> repository;

    public TRepositoryInvocationHandler(Class<E> entityClass, EntityManager entityManager) {
        this.repository = new TRepository<>(entityClass, entityManager);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Query queryAnnotation = method.getAnnotation(Query.class);
        if (queryAnnotation != null) {
            QueryHelper queryHelper = new QueryHelper();
            String queryString = queryAnnotation.value();
            jakarta.persistence.Query query = repository.getEntityManager().createQuery(queryString);

            if (List.class.isAssignableFrom(method.getReturnType())) {
                return queryHelper.getResultList(query, method, args);
            } else {
                return queryHelper.getSingleResult(query, method, args);
            }
        }

        return method.invoke(repository, args);
    }
}