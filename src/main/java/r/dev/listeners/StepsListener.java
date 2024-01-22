package r.dev.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import r.demo.ApplicationContext;
import r.dev.annotations.Inject;

import java.io.IOException;
import java.lang.reflect.Field;

public class StepsListener implements ITestListener {

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
}
