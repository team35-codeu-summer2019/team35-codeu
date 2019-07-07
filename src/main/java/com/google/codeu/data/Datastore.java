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

package com.google.codeu.data;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Provides access to the data stored in Datastore.
 */
public class Datastore {

  private DatastoreService datastore;

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /**
   * Stores the Message in Datastore.
   */
  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());
    datastore.put(messageEntity);
  }


  /**
   * @param comment to store the comments
   */
  public void storeComment(Comment comment) {
    Entity commentEntity = new Entity("Comment", comment.getId().toString());
    commentEntity.setProperty("user", comment.getUser());
    commentEntity.setProperty("text", comment.getText());
    commentEntity.setProperty("post", comment.getPost());
    commentEntity.setProperty("timestamp", comment.getTimestamp());
    datastore.put(commentEntity);
  }

  /**
   * @param like to store the likes
   */
  public void storeLike(Like like) {
    Entity likeEntity = new Entity("Like", like.getId().toString());
    likeEntity.setProperty("user", like.getUser());
    likeEntity.setProperty("post", like.getPost());
    likeEntity.setProperty("timestamp", like.getTimestamp());
    datastore.put(likeEntity);
  }

  /**
   * Stores the User in Datastore.
   */
  public void storeUser(User user) {
    Entity userEntity = new Entity("User", user.getEmail());
    userEntity.setProperty("email", user.getEmail());
    userEntity.setProperty("aboutMe", user.getAboutMe());
    userEntity.setProperty("name", user.getName());
    userEntity.setProperty("imageUrl", user.getImageUrl());
    userEntity.setProperty("followers", user.getFollowers());
    userEntity.setProperty("followings", user.getFollowings());
    datastore.put(userEntity);
  }

  /**
   * Store the geolocation in Datastore.
   */
  public void storeLocation(UserLocation userLocation) {
    Entity locationEntity = new Entity("UserLocation", userLocation.getId().toString());
    locationEntity.setProperty("user", userLocation.getUser());
    locationEntity.setProperty("country", userLocation.getCountry());
    datastore.put(locationEntity);
    System.out.println("userLocation stored");
  }

  /**
   * Store the rating for a place in Datastore.
   */
  public void storePlaceRating(PlaceRating placeRating) {
    Entity placeRatingEntity = new Entity("PlaceRating", placeRating.getId().toString());
    placeRatingEntity.setProperty("place", placeRating.getPlace());
    placeRatingEntity.setProperty("rating", placeRating.getRating());
    datastore.put(placeRatingEntity);
    System.out.println("placeRating stored");
  }

  /* ============= Likes ================= */

  /**
   * @param id
   * @return list of likes under a post
   */
  public List<Like> getLikesByPost(String id) {
    List<Like> likes = new ArrayList<Like>();
    Query query = new Query("Like").setFilter(new Query.FilterPredicate("post", FilterOperator.EQUAL, id))
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      likes.add(new Like((UUID) entity.getProperty("id"), (String) entity.getProperty("user"), (String) entity.getProperty("post"), (Long) entity.getProperty("timestamp")));
    }
    return likes;
  }

  /**
   * @param id
   * @return if delete is successful, return true; else false
   */
  public boolean deleteLike(String id) {
    try{
      Key key = KeyFactory.createKey("Like", id);
      Entity entity = datastore.get(key);
      datastore.delete(entity.getKey());
      return true;
    }catch(Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  /* ============= Comments ================= */

  /**
   * @param id
   * @return list of likes under a post
   */
  public List<Comment> getCommentByPost(String id) {
    List<Comment> comments = new ArrayList<Comment>();
    Query query = new Query("Comment").setFilter(new Query.FilterPredicate("post", FilterOperator.EQUAL, id))
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      comments.add(new Comment((UUID) entity.getProperty("id"), (String) entity.getProperty("user"), (String) entity.getProperty("text"), (String) entity.getProperty("post"), (Long) entity.getProperty("timestamp")));
    }
    return comments;
  }

  /**
   * @param id
   * @return if delete is successful, return true; else false
   */
  public boolean deleteComment(String id) {
    try{
      Key key = KeyFactory.createKey("Comment", id);
      Entity entity = datastore.get(key);
      datastore.delete(entity.getKey());
      return true;
    }catch(Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  /**
   * @param id
   * @return comment
   */
  public Comment getCommentById(String id) {
    try{
      Key key = KeyFactory.createKey("Comment", id);
      Entity entity = datastore.get(key);
      return new Comment((UUID) entity.getProperty("id"), (String) entity.getProperty("user"), (String) entity.getProperty("text"), (String) entity.getProperty("post"), (Long) entity.getProperty("timestamp"));
    }catch(Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }

  /* ============= Posts and Messages ================= */

  /**
   * @param id
   * @return message
   */
  public Message getPostById(String id) {
    try{
      Key key = KeyFactory.createKey("Message", id);
      Entity entity = datastore.get(key);
      return new Message((UUID) entity.getProperty("id"), (String) entity.getProperty("user"), (String) entity.getProperty("text"), (Long) entity.getProperty("timestamp"));
    }catch(Exception ex) {
      ex.printStackTrace();
      return null;
    }
  }


  /**
   * Gets messages posted by a specific user.
   *
   * @return a list of messages posted by the user, or empty list if user has
   * never posted a message. List is sorted by time descending.
   */
  public List<Message> getMessages(String user) {
    Query query = new Query("Message").setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user))
            .addSort("timestamp", SortDirection.DESCENDING);
    return this.getMessagesHelper(query);
  }


  private List<Message> getMessagesHelper(Query query) {
    List<Message> messages = new ArrayList<>();
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String user = (String) entity.getProperty("user");
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");

        Message message = new Message(id, user, text, timestamp);
        messages.add(message);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }
    return messages;
  }


  public List<Message> getAllMessages() {
    Query query = new Query("Message")
            .addSort("timestamp", SortDirection.DESCENDING);
    return this.getMessagesHelper(query);
  }


  /**
   * Returns the total number of messages for all users.
   */
  public int getTotalMessageCount() {
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(1000));
  }


  /**
   * Returns the average length of messages for all users.
   */
  public float getAverageMessageLength() {
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);

    Datastore datastore = new Datastore();
    int numberOfMessages = datastore.getTotalMessageCount();

    int totalLength = 0;
    for (Entity entity : results.asIterable()) {
      String message = (String) entity.getProperty("text");
      totalLength += message.length();
    }

    float result = (float) totalLength / numberOfMessages;
    return result;
  }

  /* ============= Users ================= */

  /**
   * Returns the User owned by the email address, or null if no matching User was
   * found.
   */
  public User getUser(String email) {

    Query query = new Query("User").setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, email));
    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    if (userEntity == null) {
      return null;
    }

    String aboutMe = (String) userEntity.getProperty("aboutMe");
    String name = (String) userEntity.getProperty("name");
    String imageUrl = (String) userEntity.getProperty("imageUrl");
    ArrayList<String> followers = (ArrayList) userEntity.getProperty("followers");
    ArrayList<String> followings = (ArrayList) userEntity.getProperty("followings");
    User user = new User(email, aboutMe, name, imageUrl, followers, followings);
    
    return user;
  }


  public Set<String> getUsers() {
    Set<String> users = new HashSet<>();
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      users.add((String) entity.getProperty("user"));
    }
    return users;
  }

  public ArrayList<String> getFollowingFollowerNumber(String email, String type){
    Query query = new Query("User").setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, email));
    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    ArrayList<String> requestedResult = (ArrayList<String>) userEntity.getProperty(type);
    return requestedResult;
  }

  /* ============= Stats Related ================= */

  public ArrayList<String> getCountries() {
    ArrayList<String> countries = new ArrayList<String>();
    Query query = new Query("UserLocation");
    PreparedQuery queryResults = datastore.prepare(query);
    for (Entity entity : queryResults.asIterable()) {
      countries.add((String) entity.getProperty("country"));
    }
    System.out.println("countries");
    System.out.println(countries);
    return countries;
  }

  public Set<String> getUniqueCities() {
    ArrayList<String> cities = new ArrayList<>();
    Query query = new Query("PlaceRating");
    PreparedQuery queryResults = datastore.prepare(query);
    for (Entity entity : queryResults.asIterable()) {
      cities.add((String) entity.getProperty("place"));
    }
    Set<String> uniqueCities = new HashSet<>(cities);
    System.out.println("unique cities in place rating ");
    System.out.println(uniqueCities);
    return uniqueCities;
  }

  public float getAverageRating(String place) {
    Query query = new Query("PlaceRating");
    PreparedQuery queryResults = datastore.prepare(query);

    float summedResult = 0;
    int count = 0;
    for (Entity entity : queryResults.asIterable()) {
      if (entity.getProperty("place").equals(place)) {
        count++;
        summedResult += Float.parseFloat(entity.getProperty("rating").toString());
        System.out.printf("This entity is %s, now count is %d, now summedResult is %.3f", entity.getProperty("place"), count, summedResult);
      }
    }

    float averageResult = (float) (summedResult / count);
    System.out.printf("The average result is %.3f", averageResult);
    return averageResult;
  }
}
