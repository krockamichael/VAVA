package com.example.Autoservis.generic;

public interface GenericService<T extends Object> {

    T save(T entity);

    T update(T entity);
}