package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;



/**
 * Handles fetching site statistics.
 */
@WebServlet("/user-message-count")
public class StatsUserServlet extends HttpServlet {

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

        String user = request.getParameter("user");

        if (user == null || user.equals("")) {
            // Request is invalid, return empty response
            return;
        }

        List<Message> message = datastore.getMessages(user);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userMessageCount", message.size());
        response.getOutputStream().println(jsonObject.toString());
    }
}