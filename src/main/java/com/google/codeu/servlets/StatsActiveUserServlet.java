package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/top-10-active-users")
public class StatsActiveUserServlet extends HttpServlet {

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

		List<Message> messages = datastore.getAllMessages();

		// Get all users
		ArrayList<String> users = new ArrayList<>();
		for (int i = 0; i < messages.size(); i++) {
			users.add(messages.get(i).getUser());
		}

		// Get the occurence of each user

	}
}
