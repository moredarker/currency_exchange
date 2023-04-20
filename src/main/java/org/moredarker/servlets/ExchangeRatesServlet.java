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
import java.util.ArrayList;
import java.util.List;

@WebServlet(value = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ExchangeRateRepository exchangeRateRepository = new ExchangeRateRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<ExchangeRate> exchangeRates = exchangeRateRepository.getAll();

        List<FullExchangeService> fullExchangeServices = new ArrayList<>();
        for (ExchangeRate exchangeRate : exchangeRates) {
            fullExchangeServices.add(new FullExchangeService(exchangeRate));
        }

        new JsonResponse<>(fullExchangeServices, response).send();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String baseCode = request.getParameter("baseCurrencyCode");
        String targetCode = request.getParameter("targetCurrencyCode");

        if (exchangeRateRepository.getByCodes(baseCode, targetCode) != null) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "такой обменный курс уже занесен в базу");
            return;
        }

        ExchangeRate exchangeRate = new FullExchangeService(baseCode, targetCode).getExchangeRate();
        exchangeRate.setRate(Double.parseDouble(request.getParameter("rate")));
        exchangeRateRepository.save(exchangeRate);

        exchangeRate = exchangeRateRepository.getByCodes(baseCode, targetCode);
        FullExchangeService fullExchangeService = new FullExchangeService(exchangeRate);
        new JsonResponse<>(fullExchangeService, response).send();
    }
}
