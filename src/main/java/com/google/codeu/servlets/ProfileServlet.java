package com.google.codeu.servlets;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


/**
 * Handles fetching and saving user data.
 */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  /**
   * Responds with profile data of a particular user.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json");

    String user = request.getParameter("user");
    if (user == null || user.equals("")) {
      // Request is invalid, return empty response
      return;
    }
    System.out.println(user);
    User userData = datastore.getUser(user);

    if (userData == null) {
      return;
    }
    System.out.println("here3");
    Gson gson = new Gson();
    String json = gson.toJson(userData);

    response.getOutputStream().println(json);
  }
}
