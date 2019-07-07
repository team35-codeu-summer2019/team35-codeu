package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/follow")
public class FollowServlet extends HttpServlet {
	private Datastore datastore;

	@Override
	public void init() {
		datastore = new Datastore();
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserService userService = UserServiceFactory.getUserService();
		String user = userService.getCurrentUser().getEmail();
		String followUser = request.getParameter("email");

		User u = datastore.getUser(user);   // u is going to follow fu, so add u's following list, add fu's follower list
		User fu = datastore.getUser(followUser);

		ArrayList<String> currU = u.getFollowings();
		currU.add(followUser);

		ArrayList<String> currFu = fu.getFollowers();
		currFu.add(user);
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		String user = userService.getCurrentUser().getEmail();
		String followUser = request.getParameter("email");

		User u = datastore.getUser(user);
		User fu = datastore.getUser(followUser);

		ArrayList<String> currU = u.getFollowings();
		currU.remove(followUser);

		ArrayList<String> currFu = fu.getFollowers();
		currFu.remove(user);
	}
}
