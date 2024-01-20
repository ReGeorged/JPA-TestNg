package r.dev.utils.query;

import jakarta.persistence.*;
import r.dev.annotations.Param;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class QueryHelper {

    public Query createQueryFromMethodName(String methodName, Object[] args, EntityManager entityManager, Class<?> entityClass) {
        String field = methodName.split("By")[1];
        field = Character.toLowerCase(field.charAt(0)) + field.substring(1); // convert first character to lowercase
        String jpql = "select e from " + entityClass.getSimpleName() + " e where e." + getColumnName(entityClass, field) + " = :value";
        Query query = entityManager.createQuery(jpql);
        query.setParameter("value", args[0]);
        return query;
    }

    private String getColumnName(Class<?> entityClass, String fieldName) {
        try {
            Field field = entityClass.getDeclaredField(fieldName);
            Column column = field.getAnnotation(Column.class);
            if (column != null && !column.name().isEmpty()) {
                return column.name();
            }
        } catch (NoSuchFieldException e) {
            // handle exception
        }
        return fieldName;
    }


    public  List<?> getResultList(Query query, Method method, Object[] args) {
        setParameters(query, method, args);
        try {
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute query", e);
        }
    }

    public  Object getSingleResult(Query query, Method method, Object[] args) {
        setParameters(query, method, args);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // or handle it in another way
        } catch (NonUniqueResultException e) {
            throw new RuntimeException("Query returned more than one results", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute query", e);
        }
    }

    private  void setParameters(Query query, Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Param paramAnnotation = parameters[i].getAnnotation(Param.class);
            if (paramAnnotation != null) {
                query.setParameter(paramAnnotation.value(), args[i]);
            } else {
                query.setParameter(i + 1, args[i]);
            }
        }
    }
}
