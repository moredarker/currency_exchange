package org.moredarker.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.moredarker.repository.ExchangeRateRepository;
import org.moredarker.entity.ExchangeRate;
import org.moredarker.services.FullExchangeService;
import org.moredarker.services.JsonResponse;

import java.io.IOException;

@WebServlet(value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeRateRepository exchangeRateRepository = new ExchangeRateRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseCurrencyCode = request.getParameter("from").toUpperCase();
        String targetCurrencyCode = request.getParameter("to").toUpperCase();
        double amount = Double.parseDouble(request.getParameter("amount"));

        ExchangeRate exchangeRate = exchangeRateRepository.getByCodes(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRate == null) {
            //cny -> usd
            ExchangeRate exchangeRateFrom = exchangeRateRepository.getByCodes("USD", baseCurrencyCode);
            //usd -> rub
            ExchangeRate exchangeRateTo = exchangeRateRepository.getByCodes("USD", targetCurrencyCode);

            if (exchangeRateFrom == null || exchangeRateTo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "такого обменного курса в базе нет");
                return;
            } else {
                FullExchangeService fullExchangeServiceFrom = new FullExchangeService(exchangeRateFrom)
                        .getFullExchangeService("USD");
                FullExchangeService fullExchangeServiceTo = new FullExchangeService(exchangeRateTo)
                        .getFullExchangeService("USD");

                double rate = fullExchangeServiceTo.getRate() / fullExchangeServiceFrom.getRate();

                exchangeRate = new FullExchangeService(baseCurrencyCode, targetCurrencyCode).getExchangeRate();
                exchangeRate.setRate(rate);
                exchangeRateRepository.save(exchangeRate);

                exchangeRate = exchangeRateRepository.getByCodes(baseCurrencyCode, targetCurrencyCode);
            }
        }

        FullExchangeService fullExchangeService = new FullExchangeService(exchangeRate, amount)
                .getFullExchangeService(baseCurrencyCode);
        new JsonResponse<>(fullExchangeService, response).send(true);
    }
}
