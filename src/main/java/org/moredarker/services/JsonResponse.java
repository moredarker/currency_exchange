package org.moredarker.services;

import com.google.gson.*;
import jakarta.servlet.http.HttpServletResponse;
import org.moredarker.services.exclusion.ExposeAnnotationExclusionStrategy;
import org.moredarker.services.exclusion.HiddenAnnotationExclusionStrategy;

import java.io.IOException;
import java.io.PrintWriter;

public class JsonResponse<T> {
    private final T body;
    private final HttpServletResponse response;
    private String jsonResponse;

    public JsonResponse(T body, HttpServletResponse response) {
        this.body = body;
        jsonResponse = new GsonBuilder()
                .setExclusionStrategies(new HiddenAnnotationExclusionStrategy())
                .create()
                .toJson(body);
        this.response = response;
    }

    public void send() throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        out.print(jsonResponse);
        out.flush();
    }

    public void send(boolean excludeId) throws IOException {
        jsonResponse = new GsonBuilder()
            .setExclusionStrategies(new ExposeAnnotationExclusionStrategy())
            .create()
            .toJson(body);

        send();
    }

//    public void send(boolean excludeId) throws IOException {
//        int id = jsonResponse.indexOf("id") - 2;
//        int base = jsonResponse.indexOf("base") - 1;
//        jsonResponse = "{" + jsonResponse.substring(base, jsonResponse.length() - 1) + ",\"" + jsonResponse.substring(2, id) + "}";
//        this.send();
//    }
}
