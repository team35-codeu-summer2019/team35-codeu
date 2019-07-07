package com.google.codeu.data;

import java.util.ArrayList;

public class User {

  private String email;
  private String aboutMe;
  private String name;
  private String imageUrl;
  private ArrayList<String> followers;
  private ArrayList<String> followings;

  /**
   * Create User.
   * @param email user's email
   * @param aboutMe user's about me
   * @param name user's name
   * @param imageUrl user's avatar link
   */
  public User(String email, String aboutMe, String name, String imageUrl, ArrayList<String> followers, ArrayList<String> followings) {
    this.email = email;
    this.aboutMe = aboutMe;
    this.name = name;
    this.imageUrl = imageUrl;
    this.followers = followers;
    this.followings = followings;
  }

  public String getEmail() {
    return email;
  }

  public String getAboutMe() {
    return aboutMe;
  }
  
  public String getName() {
    return name;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public ArrayList<String> getFollowers() {
    return followers;
  }

  public void setFollowers(ArrayList<String> followers) {
    this.followers = followers;
  }

  public ArrayList<String> getFollowings() {
    return followings;
  }

  public void setFollowings(ArrayList<String> followings) {
    this.followings = followings;
  }
}
