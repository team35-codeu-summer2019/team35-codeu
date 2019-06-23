package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@WebServlet("/locations")
public class StatsTopPlacesServlet extends HttpServlet {

	private Datastore datastore;
	private JsonArray placeRatingArray;

	@Override
	public void init() {
		datastore = new Datastore();
	}

	private static class placeRatingArray {
		String place;
		float rating;

		private placeRatingArray (String place, float rating) {
			this.place = place;
			this.rating = rating;
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException{
		response.setContentType("application/json");


	}
}
