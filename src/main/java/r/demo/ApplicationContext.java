package r.demo;

import j.di2.Injector;
import org.burningwave.core.assembler.ComponentContainer;
import org.burningwave.core.classes.ClassHunter;
import org.burningwave.core.classes.SearchConfig;
import r.dev.annotations.Repository;
import r.dev.annotations.Steps;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ApplicationContext {
    private final Map<Class<?>, Object> objectRegistryMap = new HashMap<>();
    private final RepositoryInjector repositoryInjector = new RepositoryInjector();

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

    public Class<?>[] getClasses(String packageName, boolean recursive) throws ClassNotFoundException, IOException {
        ComponentContainer componentConatiner = ComponentContainer.getInstance();
        ClassHunter classHunter = componentConatiner.getClassHunter();
        String packageRelPath = packageName.replace(".", "/");
        SearchConfig config = SearchConfig.forResources(
                packageRelPath
        );
        if (!recursive) {
            config.findInChildren();
        }

        try (ClassHunter.SearchResult result = classHunter.findBy(config)) {
            Collection<Class<?>> classes = result.getClasses();
            return classes.toArray(new Class[classes.size()]);
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
                Object repositoryInstance = repositoryInjector.createRepositoryInstance(field);
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                field.set(object, repositoryInstance);
                field.setAccessible(accessible);
            }
        }
    }
}