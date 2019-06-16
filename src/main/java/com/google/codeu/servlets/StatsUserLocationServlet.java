package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.gson.Gson;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


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

		//Get all countries
		ArrayList<String> countries = datastore.getCountries();

		// Get all unique countries
		Set<String> uniqueCountries = new HashSet<>(countries);

		// Into a map
		Map<String, Integer> countryFreq = new HashMap<>();
		for(String item:uniqueCountries){
			int freq = Collections.frequency(countries, item);
			countryFreq.put(item,freq);
		}

		System.out.println(countryFreq);

		Gson gson = new Gson();
		String json = gson.toJson(countryFreq);
		response.getWriter().println(json);
	}
}
