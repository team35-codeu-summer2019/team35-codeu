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
public class StatsUserLocationServlet extends HttpServlet {

  private Datastore datastore;
  private JsonArray locationFreqArray;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  private static class locationFrequency {
    String location;
    int frequency;

    private locationFrequency (String location, int frequency) {
      this.location = location;
      this.frequency = frequency;
    }
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

		// Map to the json array
    locationFreqArray = new JsonArray();
    Gson gson = new Gson();

    Iterator it = countryFreq.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry)it.next();
      String country = pair.getKey().toString();
      int frequency = Integer.parseInt(pair.getValue().toString());
      locationFreqArray.add(gson.toJsonTree(new locationFrequency(country, frequency)));
    }
    System.out.println("locationFreqArray");
    System.out.println(locationFreqArray);

    response.getOutputStream().println(locationFreqArray.toString());
  }
}
