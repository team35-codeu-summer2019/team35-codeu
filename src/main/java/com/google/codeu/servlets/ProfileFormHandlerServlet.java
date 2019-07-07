package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.User;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * When the user submits the form, Blobstore processes the file upload
 * and then forwards the request to this servlet. This servlet can then
 * process the request using the file URL we get from Blobstore.
 */
@WebServlet("/profile-form-handler")
public class ProfileFormHandlerServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String userEmail = userService.getCurrentUser().getEmail();
    ArrayList<String> followers = datastore.getUser(userEmail).getFollowers();
    ArrayList<String> followings = datastore.getUser(userEmail).getFollowings();
    String aboutMe = Jsoup.clean(request.getParameter("about"), Whitelist.relaxed());
    String name = Jsoup.clean(request.getParameter("display_name"), Whitelist.none());
    String imageUrl = FormHandlerServlet.getUploadedFileUrl(request, "image");
    if (imageUrl == null) {
      User user = datastore.getUser(userEmail);
      if (user != null) {
        imageUrl = user.getImageUrl();
      } else {
        imageUrl = "./img/user-profile.png";
      }
    }
    User user = new User(userEmail, aboutMe, name, imageUrl, followers, followings);
    datastore.storeUser(user);

    response.sendRedirect("/user-page.html?user=" + userEmail);
  }
}
