package com.google.codeu.data;

import java.util.ArrayList;
import java.util.UUID;

public class User {

  private String email;
  private String aboutMe;
  private String name;
  private String imageUrl;


  public User(String email, String aboutMe, String name, String imageUrl) {
    this.email = email;
    this.aboutMe = aboutMe;
    this.name = name;
    this.imageUrl = imageUrl;
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

}
