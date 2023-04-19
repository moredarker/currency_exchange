package org.moredarker.dao;

import java.util.List;

public interface CurrenciesDAO<T> {
    List<T> getAll();

    void save(String code, String fullname, String sign);

    void update(T t, String[] params);

    void delete(T t);
}
