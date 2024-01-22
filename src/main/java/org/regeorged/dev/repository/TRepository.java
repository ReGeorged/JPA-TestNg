package org.regeorged.dev.repository;

import java.util.List;
import java.util.Optional;

public interface TRepository<E,K> {


    Optional<E> findById(K id);
    E save(E entity);
    void delete(K id);
    void update(E entity);
    List<E> findAll();
    E getRandom();
    E getRandom(int max);

}