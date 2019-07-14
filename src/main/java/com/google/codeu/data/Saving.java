package com.google.codeu.data;

import java.util.ArrayList;
import java.util.UUID;

public class Saving {
	private String user;
	private ArrayList<String> post; // this stores the post id

	public Saving(String user, ArrayList<String> post) {
		this.user = user;
		this.post = post;
	}


	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public ArrayList<String> getPost() {
		return post;
	}

	public void setPost(ArrayList<String> post) {
		this.post = post;
	}
}
