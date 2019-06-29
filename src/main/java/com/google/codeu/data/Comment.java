package com.google.codeu.data;

import java.util.UUID;

public class Comment {
  private UUID id;
  private String user;
  private String text;
  private String post;
  private long timestamp;

  public Comment(UUID id, String user, String text, String post, long timestamp) {
    this.id = id;
    this.user = user;
    this.text = text;
    this.post = post;
    this.timestamp = timestamp;
  }

  public Comment() {
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

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
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
