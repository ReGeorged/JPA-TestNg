package r.dev.processors;

import java.lang.reflect.Field;

public class RepositoryInjectionUtil {

    public static void injectField(Field field, Object obj, Object value) {
        boolean wasAccessible = field.canAccess(obj);
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to inject field", e);
        } finally {
            field.setAccessible(wasAccessible);
        }
    }
}
