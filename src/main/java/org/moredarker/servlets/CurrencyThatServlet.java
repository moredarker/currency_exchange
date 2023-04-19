package org.moredarker.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.moredarker.dao.CurrenciesDAOImpl;
import org.moredarker.entity.Currency;
import org.moredarker.services.JsonResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyThatServlet extends HttpServlet {
    private final CurrenciesDAOImpl currenciesDAO = new CurrenciesDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() == null || request.getPathInfo().equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "укажите код валюты, например: .../currency/USD");
            return;
        }

        String currencyCode = request.getPathInfo().replaceFirst("/", "").toUpperCase();

        Currency currency = currenciesDAO.findByCode(currencyCode);

        if (currency != null) {
            new JsonResponse<>(currency, response).send();
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "такой валюты в базе нет");
        }
    }
}
