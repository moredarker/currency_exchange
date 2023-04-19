package org.moredarker.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.moredarker.dao.ExchangeRateDAOImpl;
import org.moredarker.entity.ExchangeRate;
import org.moredarker.services.FullExchangeService;
import org.moredarker.services.JsonResponse;

import java.io.IOException;

@WebServlet(value = "/exchangeRate/*")
@MultipartConfig
public class ExchangeRateThatServlet extends MyServlet {
    private final ExchangeRateDAOImpl exchangeRateDAO = new ExchangeRateDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "укажите коды валют, например: .../exchangeRate/USDEUR");
            return;
        }

        String currenciesCodes = request.getPathInfo().replaceFirst("/", "").toUpperCase();
        String baseCurrencyCode = currenciesCodes.substring(0, 3);
        String targetCurrencyCode = currenciesCodes.substring(3, 6);

        ExchangeRate exchangeRate = exchangeRateDAO.findByCode(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRate != null) {
            FullExchangeService fullExchangeService = new FullExchangeService(exchangeRate).getFullExchangeService(baseCurrencyCode);
            new JsonResponse<>(fullExchangeService, response).send();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "такого обменного курса в базе нет");
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "укажите коды валют, например: .../exchangeRate/USDEUR");
            return;
        }

        String currenciesCodes = request.getPathInfo().replaceFirst("/", "").toUpperCase();
        String baseCurrencyCode = currenciesCodes.substring(0, 3);
        String targetCurrencyCode = currenciesCodes.substring(3, 6);
        ExchangeRate exchangeRate = exchangeRateDAO.findByCode(baseCurrencyCode, targetCurrencyCode);

        if (exchangeRate == null) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "такого обменного курса в базе нет");
            return;
        }

        int id = exchangeRate.getId();
        exchangeRate.setRate(Double.parseDouble(request.getReader().readLine().split("=")[1]));
        FullExchangeService fullExchangeService = new FullExchangeService(exchangeRate).getFullExchangeService(baseCurrencyCode);
        exchangeRateDAO.update(id, fullExchangeService.getRate());

        exchangeRate = exchangeRateDAO.getById(id);
        fullExchangeService = new FullExchangeService(exchangeRate).getFullExchangeService(baseCurrencyCode);
        new JsonResponse<>(fullExchangeService, response).send();
    }
}
