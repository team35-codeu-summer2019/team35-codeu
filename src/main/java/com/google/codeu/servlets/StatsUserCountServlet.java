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
@WebServlet("/stats/user")
public class StatsUserCountServlet extends HttpServlet {

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

		int userCount = datastore.getTotalUser();

		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("userMessageCount", userCount);
		response.getOutputStream().println(jsonObject.toString());
	}
}