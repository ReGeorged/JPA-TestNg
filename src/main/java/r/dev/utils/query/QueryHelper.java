package r.dev.utils.query;

import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;
import org.springframework.data.repository.query.Param;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

public class QueryHelper {


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
