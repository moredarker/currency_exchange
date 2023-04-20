package org.moredarker.servlets;

import java.io.*;
import java.util.List;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.moredarker.repository.CurrencyRepository;
import org.moredarker.entity.Currency;
import org.moredarker.services.JsonResponse;

@WebServlet(value = "/currencies")
@MultipartConfig
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyRepository currencyRepository = new CurrencyRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Currency> currencies = currencyRepository.getAll();
        new JsonResponse<>(currencies, response).send();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");

        if (currencyRepository.getByCode(code) != null) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "валюта с таким кодом уже занесена в базу");
            return;
        }

        String fullname = request.getParameter("fullname");
        String sign = request.getParameter("sign");

        currencyRepository.save(new Currency(code, fullname, sign));
        new JsonResponse<>(currencyRepository.getByCode(code), response).send();
    }
}
