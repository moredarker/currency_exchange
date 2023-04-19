package org.moredarker.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.moredarker.dao.ExchangeRateDAOImpl;
import org.moredarker.entity.ExchangeRate;
import org.moredarker.services.FullExchangeService;
import org.moredarker.services.JsonResponse;

import java.io.IOException;

@WebServlet(value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeRateDAOImpl exchangeRateDAO = new ExchangeRateDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseCurrencyCode = request.getParameter("from");
        String targetCurrencyCode = request.getParameter("to");
        double amount = Double.parseDouble(request.getParameter("amount"));

        ExchangeRate exchangeRate = exchangeRateDAO.findByCode(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRate == null) {
            ExchangeRate exchangeRateFrom = exchangeRateDAO.findByCode("USD", baseCurrencyCode);
            ExchangeRate exchangeRateTo = exchangeRateDAO.findByCode("USD", targetCurrencyCode);

            if (exchangeRateFrom == null || exchangeRateTo == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "такого обменного курса в базе нет");
                return;
            } else {
                FullExchangeService fullExchangeServiceFrom = new FullExchangeService(exchangeRateFrom)
                        .getFullExchangeService("USD");
                FullExchangeService fullExchangeServiceTo = new FullExchangeService(exchangeRateTo)
                        .getFullExchangeService("USD");

                double rate = fullExchangeServiceFrom.getRate() / fullExchangeServiceTo.getRate();
                exchangeRateDAO.save(targetCurrencyCode, baseCurrencyCode, rate);
                exchangeRate = exchangeRateDAO.findByCode(baseCurrencyCode, targetCurrencyCode);
            }
        }

        FullExchangeService fullExchangeService = new FullExchangeService(exchangeRate, amount)
                .getFullExchangeService(baseCurrencyCode);
        new JsonResponse<>(fullExchangeService, response).send(true);
    }
}
