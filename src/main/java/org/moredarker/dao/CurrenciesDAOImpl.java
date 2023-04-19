package org.moredarker.dao;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.moredarker.entity.Currency;

import java.sql.SQLException;
import java.util.List;

public class CurrenciesDAOImpl implements CurrenciesDAO<Currency> {
    private final QueryRunner queryRunner;

    public CurrenciesDAOImpl() {
        queryRunner = new QueryRunner(DataSourceFactory.getMySQLDataSource());
    }

    public Currency getById(int id) {
        try {
            ResultSetHandler<Currency> beanHandler = new BeanHandler<>(Currency.class);
            return queryRunner.query("select * from currencies " +
                    "where id = '" + id + "'", beanHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Currency findByCode(String code) {
        try {
            ResultSetHandler<Currency> beanHandler = new BeanHandler<>(Currency.class);
            return queryRunner.query("select * from currencies " +
                    "where code = '" + code + "'", beanHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Currency> getAll() {
        try {
            ResultSetHandler<List<Currency>> beanListHandler = new BeanListHandler<Currency>(Currency.class);
            return queryRunner.query("select * from currencies", beanListHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(String code, String fullname, String sign) {
        try {
            queryRunner.update("insert into currencies(code, fullname, sign) " +
                    "values(?, ?, ?)", code, fullname, sign);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Currency currency, String[] params) {

    }

    @Override
    public void delete(Currency currency) {

    }

}
