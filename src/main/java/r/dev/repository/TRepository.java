package r.dev.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import r.dev.irepository.ITRepository;
import r.dev.utils.RandomQueryHelper;
import java.util.List;
import java.util.Optional;

public class TRepository<E, K> implements ITRepository<E, K> {

    private Class<E> clazz;
    private EntityManager entityManager;

    public TRepository(Class<E> clazz, EntityManager entityManager) {
        this.clazz = clazz;
        this.entityManager = entityManager;
    }

    public TRepository() {
    }

    @Override
    public Optional<E> findById(K id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public E save(E entity) {
        entityManager.getTransaction().begin();
        try {
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            return entity;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Failed to save entity".formatted(clazz.getClass().getName()), e);
        }
    }

    @Override
    public void delete(K id) {
        entityManager.getTransaction().begin();
        try {
            entityManager.remove(entityManager.find(clazz, id));
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Failed to delete entity".formatted(clazz.getClass().getName()), e);
        }

    }

    @Override
    public void update(E entity) {
        entityManager.getTransaction().begin();
        try {
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new RuntimeException("Failed to update entity %s".formatted(clazz.getClass().getName()), e);
        }

    }

    @Override
    public List<E> findAll() {
        var criteria = entityManager.getCriteriaBuilder().createQuery(clazz);
        criteria.from(clazz);
        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public E getRandom() {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<E> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        RandomQueryHelper randomQueryHelper = new RandomQueryHelper();
        var randomExpression = randomQueryHelper.getDialectSpecificRandomFunction();
        criteriaQuery.orderBy(criteriaBuilder.asc((Expression<?>) randomExpression));
        TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(1);
        return query.getSingleResult();

    }

    @Override
    public E getRandom(int max) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        var criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<E> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);
        RandomQueryHelper randomQueryHelper = new RandomQueryHelper();
        var randomExpression = randomQueryHelper.getDialectSpecificRandomFunction();
        criteriaQuery.orderBy(criteriaBuilder.asc((Expression<?>) randomExpression));
        TypedQuery<E> query = entityManager.createQuery(criteriaQuery);
        query.setMaxResults(max);
        return query.getSingleResult();

    }
}
