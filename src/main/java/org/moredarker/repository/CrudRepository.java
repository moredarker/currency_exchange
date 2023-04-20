package org.moredarker.repository;

import java.util.List;

public interface CrudRepository<T> {
    T getById(int id);

    List<T> getAll();

    void save(T t);

    void update(T t);

    int delete(int id);
}
