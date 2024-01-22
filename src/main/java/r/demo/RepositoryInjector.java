package r.demo;

import r.dev.annotations.Repository;
import r.dev.irepository.ITRepository;
import r.dev.providers.PersistenceProviderFactory;
import r.dev.proxy.RepositoryProxy;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class RepositoryInjector {
    private final Map<Class<?>, Object> repositoryRegistryMap = new HashMap<>();

    public <T> T getRepositoryInstance(Class<T> clazz) throws Exception {
        T object = (T) repositoryRegistryMap.get(clazz);

        Field[] declaredFields = clazz.getDeclaredFields();
        injectAnnotatedFields(object, declaredFields);

        return object;
    }

    private <T> void injectAnnotatedFields(T object, Field[] declaredFields) throws Exception {
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Repository.class)) {
                Object repositoryInstance = createRepositoryInstance(field);
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                field.set(object, repositoryInstance);
                field.setAccessible(accessible);
            }
        }
    }

    public Object createRepositoryInstance(Field field) throws Exception {
        String databaseName = field.getAnnotation(Repository.class).value();
        var entityManager = PersistenceProviderFactory.getInstance(databaseName).createEntityManager();
        RepositoryProxy proxyFactory = RepositoryProxy.getInstance(entityManager);
        // Get the actual entity class from the field's generic type
        Class<?> fieldType = field.getType();
        Class<?> entityClass = null;

        for (Type genericInterface : fieldType.getGenericInterfaces()) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) genericInterface;
                if (paramType.getRawType() == ITRepository.class) {
                    entityClass = (Class<?>) paramType.getActualTypeArguments()[0];
                }
            }
        }
        return proxyFactory.createProxy(fieldType, entityClass);
    }
}
