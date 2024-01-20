package j.di2;


import j.annotations.ComponentScan;
import j.annotations.Configuration;
import r.dev.annotations.Repository;
import r.dev.annotations.Steps;
import r.dev.irepository.ITRepository;
import r.dev.providers.PersistenceProviderFactory;
import r.dev.proxy.RepositoryProxy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationContext {
    Map<Class<?>, Object> objectRegistryMap = new HashMap<>();

    ApplicationContext(Class<?> clazz) {
        initializeContext(clazz);
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
                    entityClass =  (Class<?>) paramType.getActualTypeArguments()[0];
                }
            }
        }

        System.out.println("Entity class: " + entityClass);
        return proxyFactory.createProxy(fieldType, entityClass);
    }


    private void initializeContext(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Configuration.class)) {
            throw new RuntimeException("Please provide a valid configuration file!");
        } else {
            ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
            String packageValue = componentScan.value();
            Set<Class<?>> classes = findClasses(packageValue);

            for (Class<?> loadingClass : classes) {
                try {
                    if (loadingClass.isAnnotationPresent(Steps.class)) {
                        Constructor<?> constructor = loadingClass.getDeclaredConstructor();
                        Object newInstance = constructor.newInstance();
                        objectRegistryMap.put(loadingClass, newInstance);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Set<Class<?>> findClasses(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .collect(Collectors.toSet());
    }

    private Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}