package com.google.codeu.data;

import java.util.UUID;

public class Like {
  private UUID id;
  private String user;
  private String post;
  private long timestamp;

  public Like() {
  }

  public Like(UUID id, String user, String post, long timestamp) {
    this.id = id;
    this.user = user;
    this.post = post;
    this.timestamp = timestamp;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPost() {
    return post;
  }

  public void setPost(String post) {
    this.post = post;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
}
