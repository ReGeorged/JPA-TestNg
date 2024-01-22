package org.regeorged.dev.testng.listeners;

import org.regeorged.dev.inj.ApplicationContext;
import org.regeorged.dev.inj.annotations.Inject;
import org.regeorged.dev.persistence.providers.PersistenceProviderFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;

import java.io.IOException;
import java.lang.reflect.Field;

public class StepsListener implements ITestListener, ISuiteListener {

    @Override
    public void onStart(ITestContext context) {
        try {
            ApplicationContext applicationContext = new ApplicationContext();
            Object testInstance = context.getAllTestMethods()[0].getInstance();
            Field[] fields = testInstance.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(Inject.class) != null) {
                    var fieldClass = applicationContext.getInstance(field.getType());
                    Object toInject = fieldClass;
                    field.setAccessible(true);
                    try {
                        field.set(testInstance, toInject);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Failed to inject field: " + field.getName(), e);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onFinish(ISuite suite) {
        // Code to run after all tests in the suite have run
        PersistenceProviderFactory.closeAllEntityManagers();
    }
}
