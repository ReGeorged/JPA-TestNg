package r.di;

import org.burningwave.core.classes.FieldCriteria;
import org.di.framework.Injector;
import org.di.framework.annotations.Autowired;
import org.di.framework.annotations.Qualifier;
import r.dev.annotations.Repository;
import r.dev.providers.PersistenceProviderFactory;
import r.dev.proxy.RepositoryProxy;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import static org.burningwave.core.assembler.StaticComponentContainer.Fields;

public class NewRepoInjectorUtil {


    private NewRepoInjectorUtil() {
        super();
    }

    /**
     * Perform injection recursively, for each service inside the Client class
     */
    public static void autowire(NewRepoInjector injector, Class<?> classz, Object classInstance) throws Exception {
        Collection<Field> fields = Fields.findAllAndMakeThemAccessible(
                FieldCriteria.forEntireClassHierarchy().allThoseThatMatch(field -> field.isAnnotationPresent(Repository.class)),
                classz
        );
        for (Field field : fields) {
            if (field.isAnnotationPresent(Repository.class)) {
                String databaseName = field.getAnnotation(Repository.class).value();
                var entityManager = PersistenceProviderFactory.getInstance(databaseName).createEntityManager();
                RepositoryProxy proxyFactory = RepositoryProxy.getInstance(entityManager);
                ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                Class<?> entityClass = (Class<?>) genericType.getActualTypeArguments()[0];

                Object repository = proxyFactory.createProxy(field.getType(), entityClass);
                field.setAccessible(true);
                field.set(classInstance, repository);
            }
        }
    }
}
