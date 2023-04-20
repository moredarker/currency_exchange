package org.moredarker.servlets;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.moredarker.repository.ExchangeRateRepository;
import org.moredarker.entity.ExchangeRate;
import org.moredarker.services.FullExchangeService;
import org.moredarker.services.JsonResponse;

import java.io.IOException;

@WebServlet(value = "/exchangeRate/*")
@MultipartConfig
public class ExchangeRateThatServlet extends MyServlet {
    private final ExchangeRateRepository exchangeRateRepository = new ExchangeRateRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "укажите коды валют, например: .../exchangeRate/USDEUR");
            return;
        }

        String currenciesCodes = request.getPathInfo().replaceFirst("/", "").toUpperCase();
        String baseCurrencyCode = currenciesCodes.substring(0, 3);
        String targetCurrencyCode = currenciesCodes.substring(3, 6);

        ExchangeRate exchangeRate = exchangeRateRepository.getByCodes(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRate != null) {
            FullExchangeService fullExchangeService = new FullExchangeService(exchangeRate).getFullExchangeService(baseCurrencyCode);
            new JsonResponse<>(fullExchangeService, response).send();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "такого обменного курса в базе нет");
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "укажите коды валют, например: .../exchangeRate/USDEUR");
            return;
        }

        String currenciesCodes = request.getPathInfo().replaceFirst("/", "").toUpperCase();
        String baseCurrencyCode = currenciesCodes.substring(0, 3);
        String targetCurrencyCode = currenciesCodes.substring(3, 6);
        ExchangeRate exchangeRate = exchangeRateRepository.getByCodes(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRate == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "такого обменного курса в базе нет");
            return;
        }

        exchangeRate.setRate(Double.parseDouble(request.getReader().readLine().split("=")[1]));
        FullExchangeService fullExchangeService = new FullExchangeService(exchangeRate).getFullExchangeService(baseCurrencyCode);
        exchangeRateRepository.update(fullExchangeService.getExchangeRate());

        exchangeRate = exchangeRateRepository.getById(exchangeRate.getId());
        fullExchangeService = new FullExchangeService(exchangeRate).getFullExchangeService(baseCurrencyCode);
        new JsonResponse<>(fullExchangeService, response).send();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "укажите id обменного курса, например: .../exchange_rate/2");
            return;
        }

        int exchangeRateId = Integer.parseInt(request.getPathInfo().replaceFirst("/", ""));
        int result = exchangeRateRepository.delete(exchangeRateId);

        if (result != 0) {
            new JsonResponse<>("delete succesfull", response).send();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "такого обменного курса в базе нет");
        }
    }
}
