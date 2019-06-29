package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Comment;
import com.google.codeu.data.Datastore;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/comment")
public class CommentServlet extends HttpServlet {
  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    String id = request.getParameter("id");
    response.setContentType("application/json");
    Comment c = datastore.getCommentById(id);
    Gson gson = new Gson();
    String json = gson.toJson(c);
    response.getOutputStream().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String user = userService.getCurrentUser().getEmail();
    String text = request.getParameter("text");
    String post = request.getParameter("postId");

    Comment c = new Comment(user, text, post);
    datastore.storeComment(c);
  }

  @Override
  public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      resp.sendRedirect("/index.html");
      return;
    }

    datastore.deleteComment(req.getParameter("id"));
  }
}
