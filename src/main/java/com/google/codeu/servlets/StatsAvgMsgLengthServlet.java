package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.codeu.data.Datastore;
import com.google.gson.JsonObject;

/**
 * Handles fetching site statistics.
 */
@WebServlet("/avg-msg-length")
public class StatsAvgMsgLengthServlet extends HttpServlet{

    private Datastore datastore;

    @Override
    public void init() {
        datastore = new Datastore();
    }

    /**
     * Responds with site statistics in JSON.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");

        float avgMessageLength = datastore.getAverageMessageLength();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("avgMessageLength", avgMessageLength);
        response.getOutputStream().println(jsonObject.toString());
    }
}