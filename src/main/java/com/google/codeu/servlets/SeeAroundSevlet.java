package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/all-messages")
public class SeeAroundSevlet extends HttpServlet {
	private Datastore datastore;

	@Override
	public void init() {
		datastore = new Datastore();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");

		List<Message> m = datastore.getAllMessages();
		Gson gson = new Gson();
		String json = gson.toJson(m);
		response.getOutputStream().println(json);
	}
}
