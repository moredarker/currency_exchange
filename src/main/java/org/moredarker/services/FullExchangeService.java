package org.moredarker.services;

import com.google.gson.annotations.Expose;
import org.moredarker.dao.CurrenciesDAOImpl;
import org.moredarker.entity.Currency;
import org.moredarker.entity.ExchangeRate;
import org.moredarker.services.exclusion.Hidden;

public class FullExchangeService {
    @Expose
    private int id;

    private Currency baseCurrency;

    private Currency targetCurrency;

    private double rate;

    @Hidden
    private double amount;

    @Hidden
    private double converted;

    public FullExchangeService(ExchangeRate exchangeRate) {
        CurrenciesDAOImpl currenciesDAO = new CurrenciesDAOImpl();

        this.id = exchangeRate.getId();
        this.baseCurrency = currenciesDAO.getById(exchangeRate.getBaseCurrencyId());
        this.targetCurrency = currenciesDAO.getById(exchangeRate.getTargetCurrencyid());
        this.rate = exchangeRate.getRate();
    }

    public FullExchangeService(ExchangeRate exchangeRate, double amount) {
        this(exchangeRate);

        this.amount = amount;
        exchangeCurrencies();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Currency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getConverted() {
        return converted;
    }

    public void setConverted(double converted) {
        this.converted = converted;
    }

    public FullExchangeService getFullExchangeService(String baseCurrencyCode) {
        if (!baseCurrencyCode.equals(this.getBaseCurrency().getCode())) {
            Currency baseCurrency = this.getBaseCurrency();
            this.setBaseCurrency(this.getTargetCurrency());
            this.setTargetCurrency(baseCurrency);
            this.setRate(1 / this.getRate());
            exchangeCurrencies();
        }

        return this;
    }

    public void exchangeCurrencies() {
        converted = rate * amount;
    }
}

