package com.example.Autoservis.generic;

import java.util.List;

public interface GenericService<T extends Object> {

    T save(T entity);

    T update(T entity);

    void delete(T entity);

    void delete(Long id);

    List<T> findAll();
}