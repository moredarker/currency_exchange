package org.moredarker.entity;

public class ExchangeRate {
    private int id;

    private int baseCurrencyId;

    private int targetCurrencyid;

    private double rate;


    public ExchangeRate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(int baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public int getTargetCurrencyid() {
        return targetCurrencyid;
    }

    public void setTargetCurrencyid(int targetCurrencyid) {
        this.targetCurrencyid = targetCurrencyid;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", baseCurrencyId=" + baseCurrencyId +
                ", targetCurrencyid=" + targetCurrencyid +
                ", rate=" + rate +
                '}';
    }
}
