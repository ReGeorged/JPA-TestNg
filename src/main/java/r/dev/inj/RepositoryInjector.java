package r.dev.inj;

import r.dev.annotations.Repository;
import r.dev.processors.RepositoryInjectionUtil;
import r.dev.providers.PersistenceProviderFactory;
import r.dev.proxy.RepositoryProxy;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class RepositoryInjector {
    public static void inject(Object object) throws Exception {
        Class<?> clazz = object.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Repository.class)) {
                String databaseName = field.getAnnotation(Repository.class).value();
                var entityManager = PersistenceProviderFactory.getInstance(databaseName).createEntityManager();
                RepositoryProxy proxyFactory = RepositoryProxy.getInstance(entityManager);
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                Class<?> entityClass = (Class<?>) genericType.getActualTypeArguments()[0];

                Object repository = proxyFactory.createProxy(field.getType(), entityClass);
                field.setAccessible(true);
                field.set(object, repository);

//                RepositoryInjectionUtil.injectField(field, object, repository);
            }
        }
    }
}
