package org.regeorged.dev.handlers;

import jakarta.persistence.EntityManager;
import org.regeorged.dev.repository.annotations.Sqlfile;
import org.regeorged.dev.persistence.query.utils.QueryBuilder;
import org.regeorged.dev.persistence.query.utils.QueryHelper;
import org.regeorged.dev.repository.annotations.Query;
import org.regeorged.dev.repository.impl.TRepository;

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
        Sqlfile sqlfileAnnotation = method.getAnnotation(Sqlfile.class);
        Class<?> returnType = method.getReturnType();
        if (queryAnnotation != null) {
            QueryHelper queryHelper = new QueryHelper();
            String queryString = queryAnnotation.value();
            jakarta.persistence.Query query;
            if (queryAnnotation.nativeQuery()) {
                query = entityManager.createNativeQuery(queryString, clazz);
            } else {
                query = entityManager.createQuery(queryString);
            }
            if (List.class.isAssignableFrom(returnType)) {
                return queryHelper.getResultList(query, method, args);
            } else {
                return queryHelper.getSingleResult(query, method, args);
            }
        }else if (sqlfileAnnotation !=null){
            // New logic for @Sqlfile annotation
            QueryBuilder queryBuilder = new QueryBuilder(sqlfileAnnotation.value());

            // Assuming args are in the same order as paramKeys
            for (int i = 0; i < sqlfileAnnotation.paramKeys().length; i++) {
                String key = sqlfileAnnotation.paramKeys()[i];
                Object value = args[i];

                queryBuilder.replace(key, value);
            }

            String finalQuery = queryBuilder.build();
            System.out.println(finalQuery + " finalquery");

            // Execute finalQuery
            jakarta.persistence.Query query = entityManager.createNativeQuery(finalQuery,clazz);
            if (List.class.isAssignableFrom(returnType)) {
                return query.getResultList();
            } else {
                return query.getSingleResult();
            }
        }

        return method.invoke(repository, args);
    }

}