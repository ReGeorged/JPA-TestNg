package org.regeorged.dev.inj;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.SearchConfig;
import org.regeorged.dev.inj.annotations.Inject;
import org.regeorged.dev.inj.annotations.Steps;
import org.regeorged.dev.persistence.providers.PersistenceProviderFactory;
import org.regeorged.dev.repository.TRepository;
import org.regeorged.dev.repository.annotations.Repository;
import org.regeorged.dev.repository.proxy.RepositoryProxy;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class ApplicationContext {
    private final Map<Class<?>, Object> objectRegistryMap = new HashMap<>();

    public ApplicationContext() throws ClassNotFoundException, IOException {
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().scan()) {
            for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(Steps.class.getName())) {
                Class<?> loadingClass = Class.forName(classInfo.getName());
                try {
                    Object newInstance = loadingClass.getDeclaredConstructor().newInstance();
                    ThreadLocal<Object> threadLocalInstance = new ThreadLocal<>();
                    threadLocalInstance.set(newInstance);
                    objectRegistryMap.put(loadingClass, threadLocalInstance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public <T> T getInstance(Class<T> clazz) throws Exception {
        T object = (T) objectRegistryMap.get(clazz);
        ThreadLocal<Object> threadLocalInstance = (ThreadLocal<Object>) object;
        Field[] declaredFields = clazz.getDeclaredFields();
        injectAnnotatedFields(threadLocalInstance.get(), declaredFields);
        return (T) threadLocalInstance.get();
    }

    private <T> void injectAnnotatedFields(T object, Field[] declaredFields) throws Exception {
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Repository.class)) {
                field.setAccessible(true);

                Object repository = createRepositoryInstance(field);
                objectRegistryMap.put(field.getType(), repository);

                field.set(object, repository);
                injectAnnotatedFields(repository, field.getType().getDeclaredFields());
            } else if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Object stepsInstance = getInstance(field.getType());
                field.set(object, stepsInstance);
            }
        }
    }

    private Object createRepositoryInstance(Field field) throws Exception {
        String databaseName = field.getAnnotation(Repository.class).value();
        var entityManager = PersistenceProviderFactory.getInstance(databaseName);
        RepositoryProxy proxyFactory = RepositoryProxy.getInstance(entityManager);
        // Get the actual entity class from the field's generic type
        Class<?> fieldType = field.getType();
        Class<?> entityClass = null;

        for (Type genericInterface : fieldType.getGenericInterfaces()) {
            if (genericInterface instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) genericInterface;
                if (paramType.getRawType() == TRepository.class) {
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