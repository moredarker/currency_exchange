package org.moredarker.dao;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.moredarker.entity.ExchangeRate;

import java.sql.SQLException;
import java.util.List;

public class ExchangeRateDAOImpl {
    private final QueryRunner queryRunner;

    public ExchangeRateDAOImpl() {
        queryRunner = new QueryRunner(DataSourceFactory.getMySQLDataSource());
    }

    public ExchangeRate getById(int id) {
        try {
            ResultSetHandler<ExchangeRate> beanHandler = new BeanHandler<>(ExchangeRate.class);
            return queryRunner.query(
                    "select id as id, " +
                        "base_currency_id as baseCurrencyId," +
                        "target_currency_id as targetCurrencyId," +
                        "rate as rate " +
                        "   from exchange_rates " +
                        "where id = '" + id + "'",
                    beanHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ExchangeRate findByCode(String baseCode, String targetCode) {
        try {
            ResultSetHandler<ExchangeRate> beanHandler = new BeanHandler<>(ExchangeRate.class);

            CurrenciesDAOImpl currenciesDAO = new CurrenciesDAOImpl();
            int baseCurrencyId = currenciesDAO.findByCode(baseCode).getId();
            int targetCurrencyId = currenciesDAO.findByCode(targetCode).getId();

            return queryRunner.query(
                    "select id as id, " +
                        "base_currency_id as baseCurrencyId," +
                        "target_currency_id as targetCurrencyId," +
                        "rate as rate " +
                            "from exchange_rates " +
                        "where (base_currency_id = '" + baseCurrencyId + "' and " +
                        "target_currency_id = '" + targetCurrencyId + "') or " +
                        "(base_currency_id = '" + targetCurrencyId + "' and " +
                        "target_currency_id = '" + baseCurrencyId + "')"
                    ,
                    beanHandler);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<ExchangeRate> getAll() {
        try {
            ResultSetHandler<List<ExchangeRate>> beanListHandler = new BeanListHandler<>(ExchangeRate.class);
            return queryRunner.query("select id as id, " +
                    "base_currency_id as baseCurrencyId," +
                    "target_currency_id as targetCurrencyId," +
                    "rate as rate from exchange_rates", beanListHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void save(String baseCode, String targetCode, double rate) {
        CurrenciesDAOImpl currenciesDAO = new CurrenciesDAOImpl();
        int baseCurrencyId = currenciesDAO.findByCode(baseCode).getId();
        int targetCurrencyId = currenciesDAO.findByCode(targetCode).getId();

        try {
            queryRunner.update("insert into exchange_rates(base_currency_id, target_currency_id, rate) " +
                    "values(?, ?, ?)", baseCurrencyId, targetCurrencyId, rate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, double rate) {
        try {
            queryRunner.update("update exchange_rates set rate = ? " +
                    "where id = ?", rate, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(ExchangeRate exchangeRateDAO) {

    }
}
