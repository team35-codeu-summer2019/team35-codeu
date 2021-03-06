package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Saving;
import com.google.codeu.data.User;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/saving")
public class SavingServlet extends HttpServlet {
	private Datastore datastore;

	@Override
	public void init() {
		datastore = new Datastore();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException{
		String userEmail = request.getParameter("user");
		String post = request.getParameter("post"); // this one should be the post id

		User user = datastore.getUser(userEmail);

		Gson gson = new Gson();
		String result;
		if(datastore.getSavingByUser(userEmail).isEmpty()){
			result = "Not Stored";
		}else{
			ArrayList<String> savingList = datastore.getSavingByUser(userEmail);
			if(savingList.contains(post)){
				result = "Stored";
			}else{
				result = "Not Stored";
			}
		}

		String json = gson.toJson(result);
		System.out.println(json);
		response.getOutputStream().println(json);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws IOException {
		String userEmail = request.getParameter("user");
		String post = request.getParameter("post"); // this one should be the post id

		User user = datastore.getUser(userEmail);
		ArrayList<String> savingList  = datastore.getSavingByUser(userEmail);
		if(savingList.isEmpty()){
			savingList = new ArrayList<>();
		}
    savingList.add(post);
    Saving saving = new Saving(userEmail, savingList);
    datastore.storeSaving(saving);
    Gson gson = new Gson();
    String json = gson.toJson(saving);
    response.getOutputStream().println(json);
	}

	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
		throws IOException {
		String userEmail = request.getParameter("user");
		String post = request.getParameter("post"); // this one should be the post id

		System.out.println("If delete is being called");
		User user = datastore.getUser(userEmail);
		ArrayList<String> savingList  = datastore.getSavingByUser(userEmail);
		System.out.println(savingList);
		if(!savingList.isEmpty()){
			System.out.println(savingList.contains(post));
			if(savingList.contains(post)){
				savingList.remove(post);

				Saving saving = new Saving(userEmail, savingList);
				datastore.storeSaving(saving);
				System.out.println(saving);

				Gson gson = new Gson();
				String json = gson.toJson(saving);
				response.getOutputStream().print(json);
			}
		}
	}
}
