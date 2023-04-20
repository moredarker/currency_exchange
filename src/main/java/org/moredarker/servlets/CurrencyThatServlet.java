package org.moredarker.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.moredarker.repository.CurrencyRepository;
import org.moredarker.entity.Currency;
import org.moredarker.services.JsonResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyThatServlet extends HttpServlet {
    private final CurrencyRepository currencyRepository = new CurrencyRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "укажите код валюты, например: .../currency/USD");
            return;
        }

        String currencyCode = request.getPathInfo().replaceFirst("/", "").toUpperCase();

        Currency currency = currencyRepository.getByCode(currencyCode);

        if (currency != null) {
            new JsonResponse<>(currency, response).send();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "такой валюты в базе нет");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "укажите id валюты, например: .../currency/3");
            return;
        }

        int currencyId = Integer.parseInt(request.getPathInfo().replaceFirst("/", ""));
        int result = currencyRepository.delete(currencyId);

        if (result != 0) {
            new JsonResponse<>("delete succesfull", response).send();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "такой валюты в базе нет");
        }
    }
}
