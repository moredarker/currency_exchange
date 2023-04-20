package org.moredarker.repository;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.moredarker.entity.Currency;

import java.sql.SQLException;
import java.util.List;

public class CurrencyRepository implements CrudRepository<Currency> {
    private final QueryRunner queryRunner;

    public CurrencyRepository() {
        queryRunner = new QueryRunner(DataSourceFactory.getMySQLDataSource());
    }

    @Override
    public Currency getById(int id) {
        try {
            ResultSetHandler<Currency> beanHandler = new BeanHandler<>(Currency.class);
            return queryRunner.query(
                    "select * from currencies " +
                        "where id = '" + id + "'", beanHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Currency getByCode(String code) {
        try {
            ResultSetHandler<Currency> beanHandler = new BeanHandler<>(Currency.class);
            return queryRunner.query(
                    "select * from currencies " +
                        "where code = '" + code + "'", beanHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Currency> getAll() {
        try {
            ResultSetHandler<List<Currency>> beanListHandler = new BeanListHandler<>(Currency.class);
            return queryRunner.query("select * from currencies", beanListHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(Currency currency) {
        try {
            queryRunner.update(
                    "insert into currencies(code, fullname, sign) " +
                        "values(?, ?, ?)",
                        currency.getCode(),
                        currency.getFullname(),
                        currency.getSign());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Currency currency) {

    }

    @Override
    public int delete(int id) {
        try {
            return queryRunner.update(
                    "delete from currencies where id = ?",
                        id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
