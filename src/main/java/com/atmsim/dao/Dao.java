package com.atmsim.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {

    Optional<E> get(K id);

    List<E> getAll();

    long save(E entity);

    int update(E entity);

    int delete(E entity);
}
