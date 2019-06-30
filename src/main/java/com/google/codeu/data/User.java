package com.google.codeu.data;

import java.util.ArrayList;
import java.util.UUID;

public class User {

  private UUID id;
  private String email;
  private String aboutMe;
  private String name;
  private String imageUrl;
  private ArrayList<UUID> followers;
  private ArrayList<UUID> followings;

  public User(String email, String aboutMe, String name, String imageUrl, ArrayList<UUID> followers, ArrayList<UUID> followings) {
    this(UUID.randomUUID(), email, aboutMe, name, imageUrl, followers, followings);
  }

  public User(UUID id, String email, String aboutMe, String name, String imageUrl, ArrayList<UUID> followers, ArrayList<UUID> followings) {
    this.id = id;
    this.email = email;
    this.aboutMe = aboutMe;
    this.name = name;
    this.imageUrl = imageUrl;
    this.followers = followers;
    this.followings = followings;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAboutMe() {
    return aboutMe;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public ArrayList<UUID> getFollowers() {
    return followers;
  }

  public void setFollowers(ArrayList<UUID> followers) {
    this.followers = followers;
  }

  public ArrayList<UUID> getFollowings() {
    return followings;
  }

  public void setFollowings(ArrayList<UUID> followings) {
    this.followings = followings;
  }
}
