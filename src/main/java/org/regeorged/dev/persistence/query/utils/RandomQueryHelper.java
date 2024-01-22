package org.regeorged.dev.persistence.query.utils;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;

public class RandomQueryHelper {
    private EntityManager entityManager;
    private CriteriaBuilder criteriaBuilder;
    private Expression<?> expression;

    private String getDialect() {
        Session session = entityManager.unwrap(Session.class);
        SessionFactory sessionFactory = session.getSessionFactory();
        return ((SessionFactoryImplementor) sessionFactory).getJdbcServices().getDialect().toString();
    }

    public Expression<?> getDialectSpecificRandomFunction() {
        String dialect = getDialect();
        switch (dialect) {
            case "org.hibernate.dialect.PostgreSQLDialect":
                return criteriaBuilder.function("RANDOM", Double.class);
            case "org.hibernate.dialect.MySQLDialect":
                return criteriaBuilder.function("RAND", Double.class);
            case "org.hibernate.dialect.OracleDialect":
                return criteriaBuilder.function("DBMS_RANDOM.VALUE", Double.class);
            case "org.hibernate.dialect.SQLServerDialect":
                return criteriaBuilder.function("NEWID", Double.class);
            default:
                throw new RuntimeException("Dialect not supported");
        }
    }
}