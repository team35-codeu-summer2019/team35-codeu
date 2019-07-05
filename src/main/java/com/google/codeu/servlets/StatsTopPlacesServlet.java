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


@WebServlet("/top-10-places")
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

  public static Map<String, Float> sortByFloatValue(Map<String, Float> hm){
    List<Map.Entry<String, Float> > list = new LinkedList<>(hm.entrySet());

    Collections.sort(list, new Comparator<Map.Entry<String, Float> >(){
      public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2){
        return (o1.getValue().compareTo(o2.getValue()));
      }
    });

    HashMap<String, Float> temp = new LinkedHashMap<>();
    for (Map.Entry<String, Float> aa : list){
      temp.put(aa.getKey(), aa.getValue());
    }
    return temp;
  }


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException{
    response.setContentType("application/json");

    // Get all unique cities
    Set<String> uniqueCities = datastore.getUniqueCities();
    Map<String, Float> result = new HashMap<>();

    // Get the average scores for all the unique cities and put into a map
    for(String item : uniqueCities){
      float rating = datastore.getAverageRating(item);
      result.put(item,rating);
    }

    // Sort the uniqueCity - averageRating map
    Map<String, Float> sortedResult = sortByFloatValue(result);

    // Get the Top 10 from the whole map
    Map<String, Float> finalResult = new HashMap<>();
    int sortedResultLength = sortedResult.size();
    if(sortedResultLength <= 10){
      System.out.println("Less than 10");
      finalResult = sortedResult;
    }else{
      System.out.println("More than 10");
      int count = 0;

      Iterator it = sortedResult.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry pair = (Map.Entry)it.next();
        if(sortedResultLength-count<=10){  // Meaning the last 10
          finalResult.put(pair.getKey().toString(), Float.parseFloat(pair.getValue().toString()));
        }
        count++;
        it.remove();
      }
    }
    System.out.println("Top 10 places (hasn't converted to json array) ");
    System.out.println(finalResult);

    // Convert the result to json array
    placeRatingArray = new JsonArray();
    Gson gson = new Gson();
    Iterator it = finalResult.entrySet().iterator();
    while(it.hasNext()){
      Map.Entry pair = (Map.Entry)it.next();
      String place = pair.getKey().toString();
      float rating = Float.parseFloat(pair.getValue().toString());
      placeRatingArray.add(gson.toJsonTree(new placeRatingArray(place, rating)));
    }
    System.out.println("placeRatingArray");
    System.out.println(placeRatingArray);
    response.getOutputStream().println(placeRatingArray.toString());
  }
}
