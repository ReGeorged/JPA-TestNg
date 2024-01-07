package listeners;

import annotations.Repository;
import inj.RepositoryInjector;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import repository.TRepository;

import java.lang.reflect.Field;

public class RepositoryListener implements IInvokedMethodListener {
    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        Object testInstance = testResult.getInstance();
        injectRepositoriesRecursively(testInstance);
    }

    private void injectRepositoriesRecursively(Object instance) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (field.getType() == Repository.class) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    Object fieldValue = field.get(instance);
                    if (fieldValue != null) {
                        RepositoryInjector.injectRepositories(fieldValue);
                        injectRepositoriesRecursively(fieldValue);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject TRepository", e);
                } finally {
                    field.setAccessible(accessible);
                }
            }
        }
    }

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        RepositoryInjector.closeConnections();
    }
}
