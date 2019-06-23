/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.servlets;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.cloud.language.v1.*;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.PlaceRating;
import com.google.codeu.data.UserLocation;
import com.google.codeu.data.Message;
import com.google.gson.Gson;

import java.io.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;



/** Handles fetching and saving {@link Message} instances. */
@WebServlet("/messages")
public class MessageServlet extends HttpServlet {

  private Datastore datastore;

  @Override
  public void init() {
    datastore = new Datastore();
  }

  public List<String> getWorldCities() throws FileNotFoundException, IOException{

    URL url = new URL("https://raw.githubusercontent.com/team35-codeu-summer2019/team35-codeu/master/src/main/java/com/google/codeu/servlets/WORLD-CITIES.txt");
    List<String> result = new ArrayList<>();
    Scanner s = new Scanner(url.openStream());
    s.nextLine(); // skip first line

    while (s.hasNextLine()) {
      String temp1 = s.nextLine();
      String temp2 = temp1.substring(6);
      System.out.println(temp2);
      result.add(temp2);
    }
    s.close();
    return result;
  }

  private String insertMediaTag(String content) {

    String regex = "(^|\\s|<br>|<p>|<span>)(https?://\\S+\\.(png|jpg|gif))(\\s|<br>|</p>|</span>|$)";
    String replacement =  "<img src=\"$2\" alt=\"$2\" >";
    String newContent = content.replaceAll(regex, replacement);

    regex = "(^|\\s|<br>|<p>|<span>)!\\[(.*)]\\((https?://\\S+\\.(png|jpg|gif))\\)(\\s|<br>|</p>|</span>|$)";
    replacement = "<figure> <img src=\"$3\" alt=\"$3\">"
            + "<figcaption> $2 </figcatption>" + "<figure>";
    newContent = newContent.replaceAll(regex, replacement);

    regex = "(^|\\s|<br>|<p>|<span>)(https?://\\S+\\.(mp4|webm|ogg))(\\s|<br>|</p>|</span>|$)";
    replacement = "<video controls> <source src=\"$2\"> </video>";
    newContent = newContent.replaceAll(regex, replacement);

    regex = "(^|\\s|<br>|<p>|<span>)(https?://\\S+\\.(mp3|wav|ogg))(\\s|<br>|</p>|</span>|$)";
    replacement = "<audio controls> <source src=\"$2\"> </audio>";
    newContent = newContent.replaceAll(regex, replacement);

    return newContent;
  }

  /**
   * Responds with a JSON representation of {@link Message} data for a specific
   * user. Responds with an empty array if the user is not provided.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json");

    String user = request.getParameter("user");

    if (user == null || user.equals("")) {
      // Request is invalid, return empty array
      response.getWriter().println("[]");
      return;
    }

    List<Message> messages = datastore.getMessages(user);
    Gson gson = new Gson();
    String json = gson.toJson(messages);

    response.getWriter().println(json);
  }

  /** Stores a new {@link Message}. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String user = userService.getCurrentUser().getEmail();
    String userEnteredContent = request.getParameter("text");

    Whitelist whitelist = Whitelist.relaxed();
    String sanitizedContent = Jsoup.clean(userEnteredContent, whitelist);

    String textWithMedia = insertMediaTag(sanitizedContent);

    Message message = new Message(user, textWithMedia);
    datastore.storeMessage(message);

    // store a userLocation here
    IPInfo ipInfo = IPInfo.builder().setToken("5099035df7d924").setCountryFile(new File("iptoaddress.json")).build();
    String ipAddress = request.getRemoteAddr();
    try {
      IPResponse ipResponse = ipInfo.lookupIP(ipAddress);
      // Print out the hostname
      System.out.println("Testing starts here");
      System.out.println(ipAddress);
      System.out.println(ipResponse);
      System.out.println(ipResponse.getCountryName());
      System.out.println(ipResponse.getCountryCode());
      System.out.println(ipResponse.getHostname());

      UserLocation userLocation = new UserLocation(user, ipResponse.getCountryCode());
      datastore.storeLocation(userLocation);

    } catch (RateLimitedException ex) {
      System.out.println("Exceed rate limit");
    }

    // store a place rating
    Document doc = Document.newBuilder()
        .setContent(userEnteredContent).setType(Document.Type.PLAIN_TEXT).build();
    try (LanguageServiceClient language = LanguageServiceClient.create()) {

      // Get the rating from the sentiment analysis
      Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
      float score = sentiment.getScore();
      System.out.printf("Sentiment Analysis Score is %.2f", score);

      // Get the place from NER
      AnalyzeEntitiesRequest nerRequest = AnalyzeEntitiesRequest.newBuilder()
          .setDocument(doc)
          .setEncodingType(EncodingType.UTF16)
          .build();
      AnalyzeEntitiesResponse nerResponse = language.analyzeEntities(nerRequest);

      // Print the response
      float maximum = 0;
      String maximumEntity = "";
      // List<String> allCities = getWorldCities();
      for (Entity entity : nerResponse.getEntitiesList()) {
        String entityName = entity.getName();
        String entityType = entity.getType().toString();
        if (entityType == "LOCATION") {
          System.out.printf("Entity: %s", entityName);
          System.out.printf("Type is: %s", entityType);
          System.out.printf("Salience: %.3f\n", entity.getSalience());
          if (entity.getSalience() > maximum) {
            maximum = entity.getSalience();
              maximumEntity = entityName;
            }
//          for(String str: allCities) {
//            if(str.trim().contains(entityName)){
//
//            }
//          }
        }
      }
      System.out.printf("Maximum Entity: %s, Salience: %.3f\n", maximumEntity, maximum);

      // Store into the database
      PlaceRating placeRating = new PlaceRating(maximumEntity, score);
      datastore.storePlaceRating(placeRating);
    }  


    response.sendRedirect("/user-page.html?user=" + user);
  }
}
