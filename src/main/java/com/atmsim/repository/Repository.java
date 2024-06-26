package com.atmsim.repository;

import java.util.List;

public interface Repository<ID, T> {
    List<T> findAll();

    T findById(ID id);

    void save(T entity);

    void delete(T entity);
}
