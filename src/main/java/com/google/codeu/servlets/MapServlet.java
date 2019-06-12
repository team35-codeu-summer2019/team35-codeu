package com.google.codeu.servlets;


import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Scanner;

@WebServlet("/map-data")
public class MapServlet extends HttpServlet {
    /**
     * Returns Map data as a JSON array, e.g. [{"lat": 38.4404675, "lng": -122.7144313}]
     */
    private JsonArray mapArray;

    @Override
    public void init() {
        mapArray = new JsonArray();
        Gson gson = new Gson();
        Scanner scanner = new Scanner(getServletContext().getResourceAsStream("/WEB-INF/country.csv"));
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] cells = line.split(",");

            String abbreviation = cells[0].replaceAll("[^\\x00-\\x7F]", "");
            double lat = Double.parseDouble(cells[1].replaceAll("[^\\x00-\\x7F]", ""));
            double lng = Double.parseDouble(cells[2].replaceAll("[^\\x00-\\x7F]", ""));
            String countryName = cells[3].replaceAll("[^\\x00-\\x7F]", "");

            mapArray.add(gson.toJsonTree(new Country(countryName,lat, lng,abbreviation)));
        }
        scanner.close();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.getOutputStream().println(mapArray.toString());
    }

    private static class Country{
        String countryName;
        double lat;
        double lng;
        String abbreviation;

        private Country(String countryName, double lat, double lng, String abbreviation) {
            this.countryName = countryName;
            this.lat = lat;
            this.lng = lng;
            this.abbreviation = abbreviation;
        }
    }
}
