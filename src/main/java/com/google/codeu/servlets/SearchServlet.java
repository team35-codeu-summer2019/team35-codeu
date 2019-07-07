
package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Response with JSON representation of list user matched with pattern.
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    String pattern = "(.*)" + request.getParameter("q") + "(.*)";

    Set<String> userList = datastore.getActiveUsers();
    Set<String> matchedUsers = new HashSet<>();

    for (String user : userList) {
      if (user.matches(pattern)) {
        matchedUsers.add(user);
      }
    }
    Gson gson = new Gson();
    String json = gson.toJson(matchedUsers);

    response.getOutputStream().println(json);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String pattern = request.getParameter("text");
    response.sendRedirect("search.html?q=" + pattern);
  }
}
