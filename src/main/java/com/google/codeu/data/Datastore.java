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

import java.util.*;

/** Provides access to the data stored in Datastore. */
public class Datastore {

  private DatastoreService datastore;

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Stores the Message in Datastore. */
  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());
    datastore.put(messageEntity);
  }

  /** Stores the User in Datastore. */
  public void storeUser(User user) {
    Entity userEntity = new Entity("User", user.getEmail());
    userEntity.setProperty("email", user.getEmail());
    userEntity.setProperty("aboutMe", user.getAboutMe());
    datastore.put(userEntity);
  }

  /** Store the geolocation in Datastore.*/
  public void storeLocation(Location location){
    Entity locationEntity = new Entity("Location", location.getId().toString());
    locationEntity.setProperty("user", location.getUser());
    locationEntity.setProperty("country",location.getCountry());
    datastore.put(locationEntity);
    System.out.println("location stored");
  }

  /**
   * Gets messages posted by a specific user.
   *
   * @return a list of messages posted by the user, or empty list if user has
   *         never posted a message. List is sorted by time descending.
   */
  public List<Message> getMessages(String user) {
    Query query = new Query("Message").setFilter(new Query.FilterPredicate("user", FilterOperator.EQUAL, user))
        .addSort("timestamp", SortDirection.DESCENDING);
//    PreparedQuery results = datastore.prepare(query);
//
//    for (Entity entity : results.asIterable()) {
//      try {
//        String idString = entity.getKey().getName();
//        UUID id = UUID.fromString(idString);
//        String text = (String) entity.getProperty("text");
//        long timestamp = (long) entity.getProperty("timestamp");
//
//        Message message = new Message(id, user, text, timestamp);
//        messages.add(message);
//      } catch (Exception e) {
//        System.err.println("Error reading message.");
//        System.err.println(entity.toString());
//        e.printStackTrace();
//      }
//    }
    return this.getMessagesHelper(query);
  }

  private List<Message> getMessagesHelper(Query query){
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

  public List<Message> getAllMessages(){
    Query query = new Query("Message")
            .addSort("timestamp", SortDirection.DESCENDING);
    return this.getMessagesHelper(query);
  }

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
    User user = new User(email, aboutMe);

    return user;
  }

  /** Returns the total number of messages for all users. */
  public int getTotalMessageCount(){
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(1000));
  }

  /** Returns the average length of messages for all users. */
  public float getAverageMessageLength(){
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

  /** Returns the total number of users. */
  public int getTotalUser(){
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(1000));
  }

  public Set<String> getUsers(){
    Set<String> users = new HashSet<>();
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    for(Entity entity : results.asIterable()) {
      users.add((String) entity.getProperty("user"));
    }
    return users;
  }

  public Set<String> getCountries(){
    Set<String> countries = new HashSet<>();
    Query query = new Query("Location");
    PreparedQuery queryResults = datastore.prepare(query);
    for(Entity entity : queryResults.asIterable()){
      countries.add((String) entity.getProperty("country"));
    }
    System.out.println("countries");
    System.out.println(countries);
    return countries;
  }
}


