package r.dev.processors;

import r.dev.annotations.Repository;
import r.dev.providers.PersistenceProviderFactory;
import r.dev.proxy.RepositoryProxy;
import r.entities.UsersEntity;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class RepositoryProcessor {
    public static void process(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Repository.class)) {
                String databaseName = field.getAnnotation(Repository.class).value();
                var entityManager = PersistenceProviderFactory.getInstance(databaseName).createEntityManager();
                RepositoryProxy proxyFactory = RepositoryProxy.getInstance(entityManager);
                // Get the actual entity class from the field's generic type
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                Class<?> entityClass = (Class<?>) genericType.getActualTypeArguments()[0];

                Object repository = proxyFactory.createProxy(field.getType(), entityClass);
                field.setAccessible(true);
                field.set(object, repository);
            }
        }
    }
}