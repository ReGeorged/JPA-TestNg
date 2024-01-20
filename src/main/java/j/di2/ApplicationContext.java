package j.di2;


import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.SearchConfig;
import r.dev.annotations.Repository;
import r.dev.annotations.Steps;
import r.dev.irepository.ITRepository;
import r.dev.providers.PersistenceProviderFactory;
import r.dev.proxy.RepositoryProxy;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class ApplicationContext {
    private final Map<Class<?>, Object> objectRegistryMap = new HashMap<>();

    public ApplicationContext(Class<?> clazz) throws ClassNotFoundException, IOException {
        Class<?>[] classes = getClasses(clazz.getPackageName(), true);
        for (Class<?> loadingClass : classes) {
            try {
                if (loadingClass.isAnnotationPresent(Steps.class)) {
                    Object newInstance = loadingClass.getDeclaredConstructor().newInstance();
                    objectRegistryMap.put(loadingClass, newInstance);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public <T> T getInstance(Class<T> clazz) throws Exception {
        T object = (T) objectRegistryMap.get(clazz);

        Field[] declaredFields = clazz.getDeclaredFields();
        injectAnnotatedFields(object, declaredFields);

        return object;
    }

    private <T> void injectAnnotatedFields(T object, Field[] declaredFields) throws Exception {
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Repository.class)) {
                field.setAccessible(true);

                Object repository = createRepositoryInstance(field);
                objectRegistryMap.put(field.getType(), repository);

                field.set(object, repository);
                injectAnnotatedFields(repository, field.getType().getDeclaredFields());
            }
        }
    }

    private Object createRepositoryInstance(Field field) throws Exception {
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

    public Class<?>[] getClasses(String packageName, boolean recursive) throws ClassNotFoundException, IOException {
        ComponentContainer componentContainer = ComponentContainer.getInstance();
        ClassHunter classHunter = componentContainer.getClassHunter();
        String packageRelPath = packageName.replace(".", "/");
        SearchConfig config = SearchConfig.forResources(packageRelPath);
        if (!recursive) {
            config.findInChildren();
        }

        try (ClassHunter.SearchResult result = classHunter.findBy(config)) {
            Collection<Class<?>> classes = result.getClasses();
            return classes.toArray(new Class[classes.size()]);
        }
    }
}