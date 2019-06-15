package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet("/locations")
public class StatsUserLocationServlet extends HttpServlet {

	private Datastore datastore;

	@Override
	public void init() {
		datastore = new Datastore();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
					throws IOException{
		response.setContentType("application/json");


		Set<String> countries = datastore.getCountries();

		Gson gson = new Gson();
		String json = gson.toJson(countries);
		response.getWriter().println(json);
	}
}
