package org.moredarker.servlets;

import java.io.*;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.moredarker.dao.CurrenciesDAOImpl;
import org.moredarker.entity.Currency;
import org.moredarker.services.JsonResponse;

@WebServlet(value = "/currencies")
@MultipartConfig
public class CurrenciesServlet extends HttpServlet {
    private final CurrenciesDAOImpl currenciesDAO = new CurrenciesDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Currency> currencies = currenciesDAO.getAll();
        new JsonResponse<>(currencies, response).send();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String code = request.getParameter("code");

        if (currenciesDAO.findByCode(code) != null) {
            response.sendError(HttpServletResponse.SC_CONFLICT, "валюта с таким кодом уже занесена в базу");
            return;
        }

        String fullname = request.getParameter("fullname");
        String sign = request.getParameter("sign");

        currenciesDAO.save(code, fullname, sign);
        new JsonResponse<>(currenciesDAO.findByCode(code), response).send();
    }
}
