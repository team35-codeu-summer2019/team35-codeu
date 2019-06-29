package com.google.codeu.data;

public class User {

  private String email;
  private String aboutMe;
  private String name;
  private String imageUrl;

  /**
   * Create User.
   * @param email user's email
   * @param aboutMe user's about me
   * @param name user's name
   * @param imageUrl user's avatar link
   */
  public User(String email, String aboutMe, String name, String imageUrl) {
    this.email = email;
    this.aboutMe = aboutMe;
    this.name = name;
    this.imageUrl = imageUrl;
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
}
