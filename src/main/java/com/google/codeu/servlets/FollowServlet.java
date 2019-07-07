package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/follow")
public class FollowServlet extends HttpServlet {
	private Datastore datastore;
	private JsonArray resultArray;

	@Override
	public void init() {
		datastore = new Datastore();
	}

	private static class result {
		String type;
		String user;
		ArrayList<String> resultArrayList;

		private result (String type, String user, ArrayList<String> resultArrayList) {
			this.type = type;
			this.user = user;
			this.resultArrayList = resultArrayList;
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String user = request.getParameter("followeremail");
		String followUser = request.getParameter("followingemail");

		User u = datastore.getUser(user);   // u is going to follow fu, so add u's following list, add fu's follower list
		User fu = datastore.getUser(followUser);

		ArrayList<String> currU = u.getFollowings();
		ArrayList<String> currFu = fu.getFollowers();


		if(currU==null){
			currU = new ArrayList<>();
			currU.add(followUser);
		}else if(currU.contains(followUser)){
			System.out.println(followUser + " has already been added");
		}else{
			currU.add(followUser);
		}

		if(currFu==null){
			currFu = new ArrayList<>();
			currFu.add(user);
		}else if(currFu.contains(user)){
			System.out.println(user + " has already been added");
		}else{
			currFu.add(user);
		}

		u.setFollowings(currU);
		fu.setFollowers(currFu);

		resultArray = new JsonArray();
		Gson gson = new Gson();
		resultArray.add(gson.toJsonTree(new result("Follower", user, currU)));
		resultArray.add(gson.toJsonTree(new result("Following", followUser, currFu)));

		response.getOutputStream().println(resultArray.toString());
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String user = request.getParameter("followeremail");
		String followUser = request.getParameter("followingemail");

		User u = datastore.getUser(user);
		User fu = datastore.getUser(followUser);

		ArrayList<String> currU = u.getFollowings();
		ArrayList<String> currFu = fu.getFollowers();

		if(currU!=null){
			try{
				currU.remove(followUser);
			}catch (Exception e) {
				System.out.println("There is no such followed user");
			}
		}

		if(currFu!=null){
			try{
				currFu.remove(user);
			}catch (Exception e) {
				System.out.println("There is no such user following");
			}
		}

		u.setFollowings(currU);
		fu.setFollowers(currFu);

		resultArray = new JsonArray();
		Gson gson = new Gson();
		resultArray.add(gson.toJsonTree(new result("Follower", user, currU)));
		resultArray.add(gson.toJsonTree(new result("Following", followUser, currFu)));

		response.getOutputStream().println(resultArray.toString());
	}
}
