package listeners;

import annotations.Connection;
import inj.ConnectionInjector;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import steps.UserSteps;

import java.lang.reflect.Field;

public class ConnectionListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        Object testInstance = testResult.getInstance();
        injectConnectionsRecursively(testInstance);
    }

    private void injectConnectionsRecursively(Object instance) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            try {
                Object fieldValue = field.get(instance);
                if (fieldValue != null) {
                    for (Field innerField : fieldValue.getClass().getDeclaredFields()) {
                        if (innerField.isAnnotationPresent(Connection.class)) {
                            ConnectionInjector.injectConnections(fieldValue);
                            injectConnectionsRecursively(fieldValue);
                            break;
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to inject EntityManager", e);
            } finally {
                field.setAccessible(accessible);
            }
        }
    }


    @Override
    public void afterInvocation (IInvokedMethod method, ITestResult testResult){
        ConnectionInjector.closeConnections();
    }


}

