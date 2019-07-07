package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.codeu.data.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/all-messages")
public class SeeAroundSevlet extends HttpServlet {
	private Datastore datastore;
	private JsonArray postArray;

	@Override
	public void init() {
		datastore = new Datastore();
	}

	private static class Post {
		String imageUrl;
		String user;
		String message;
		String postId;
		int likes;
		boolean ifHaveComment;

		private Post (String imageUrl, String user, String message, String postId, int likes, boolean ifHaveComment) {
			this.imageUrl = imageUrl;
			this.user = user;
			this.message = message;
			this.postId = postId;
			this.likes = likes;
			this.ifHaveComment = ifHaveComment;
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json");

		List<Message> m = datastore.getAllMessages();
		postArray = new JsonArray();
		Gson gson = new Gson();

		for(int i = 0; i < m.size(); i++){
			String user = m.get(i).getUser();
			String imageUrl = datastore.getUser(user).getImageUrl();
			String message = m.get(i).getText();
			String postId = m.get(i).getId().toString();
			int likes = datastore.getLikesByPost(m.get(i).getId().toString()).size();
			boolean ifHaveComment = !(datastore.getCommentByPost(m.get(i).getId().toString()).isEmpty());
			Post post = new Post(imageUrl, user, message, postId, likes, ifHaveComment);
			postArray.add(gson.toJsonTree(post));
		}

		System.out.println(postArray);
		response.getOutputStream().println(postArray.toString());
	}
}
