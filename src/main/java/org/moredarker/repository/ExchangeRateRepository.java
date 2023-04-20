package org.moredarker.repository;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.moredarker.entity.ExchangeRate;

import java.sql.SQLException;
import java.util.List;

public class ExchangeRateRepository implements CrudRepository<ExchangeRate> {
    private final QueryRunner queryRunner;

    public ExchangeRateRepository() {
        queryRunner = new QueryRunner(DataSourceFactory.getMySQLDataSource());
    }

    @Override
    public ExchangeRate getById(int id) {
        try {
            ResultSetHandler<ExchangeRate> beanHandler = new BeanHandler<>(ExchangeRate.class);

            return queryRunner.query(
                    "select id as id, " +
                        "base_currency_id as baseCurrencyId," +
                        "target_currency_id as targetCurrencyId," +
                        "rate as rate " +
                        "from exchange_rates " +
                        "where id = '" + id + "'"
                    , beanHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ExchangeRate getByCodes(String baseCurrencyCode, String targetCurrencyCode) {
        try {
            ResultSetHandler<ExchangeRate> beanHandler = new BeanHandler<>(ExchangeRate.class);

            return queryRunner.query(
                    "select id as id, " +
                        "base_currency_id as baseCurrencyId," +
                        "target_currency_id as targetCurrencyId," +
                        "rate as rate " +
                        "from exchange_rates " +
                        "where (base_currency_id = (select id from currencies where code = '" + baseCurrencyCode +
                        "') and target_currency_id = (select id from currencies where code = '" + targetCurrencyCode +
                        "')) or (base_currency_id = (select id from currencies where code = '" + targetCurrencyCode +
                        "') and target_currency_id = (select id from currencies where code = '" + baseCurrencyCode +
                        "'))"
                    , beanHandler);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ExchangeRate> getAll() {
        try {
            ResultSetHandler<List<ExchangeRate>> beanListHandler = new BeanListHandler<>(ExchangeRate.class);

            return queryRunner.query(
                    "select id as id, " +
                        "base_currency_id as baseCurrencyId," +
                        "target_currency_id as targetCurrencyId," +
                        "rate as rate from exchange_rates", beanListHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void save(ExchangeRate exchangeRate) {
        try {
            queryRunner.update(
                    "insert into exchange_rates(base_currency_id, target_currency_id, rate) " +
                        "values(?, ?, ?)",
                        exchangeRate.getBaseCurrencyId(),
                        exchangeRate.getTargetCurrencyid(),
                        exchangeRate.getRate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(ExchangeRate exchangeRate) {
        try {
            queryRunner.update(
                    "update exchange_rates set rate = ? " +
                        "where id = ?",
                        exchangeRate.getRate(),
                        exchangeRate.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int delete(int id) {
        try {
            return queryRunner.update(
                    "delete from exchange_rates where id = ?",
                    id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
