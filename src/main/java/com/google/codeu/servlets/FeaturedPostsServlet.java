package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.codeu.data.Saving;
import com.google.codeu.data.User;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/featured-posts")
public class FeaturedPostsServlet extends HttpServlet {
	private Datastore datastore;

	@Override
	public void init() {
		datastore = new Datastore();
	}

	public static Map<String, Integer> sortByValue(Map<String, Integer> hm)
	{
		// Create a list from elements of HashMap
		List<Map.Entry<String, Integer> > list =
			new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
			public int compare(Map.Entry<String, Integer> o1,
			                   Map.Entry<String, Integer> o2)
			{
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
		for (Map.Entry<String, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException {
		List<Message> m = datastore.getAllMessages();
		Map<String, Integer> messageWithLikes = new HashMap<>();

		// Get all messages and their assciated likes
		for(int i = 0; i < m.size(); i ++){
			 messageWithLikes.put(m.get(i).getId().toString(), datastore.getLikesByPost(m.get(i).getId().toString()).size());
		}

		// Sort the result and the top 10 messages with the highest number of like (#of like has to be larger than 0)
		Map<String, Integer> sortedMessageWithLikes = sortByValue(messageWithLikes);
		System.out.println(sortedMessageWithLikes);
		Map<String, Integer> result = new HashMap<>();

		if(messageWithLikes.size() <= 10){
			System.out.println("Less than 10 is executed");

			Iterator it = sortedMessageWithLikes.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				if (Integer.parseInt(pair.getValue().toString()) > 0) {
					result.put((String) pair.getKey(), Integer.parseInt(pair.getValue().toString()));
				}
			}
		}else{
			System.out.println("More than 10 is executed");
			int length = sortedMessageWithLikes.size();
			int count = 0;

			Iterator it = sortedMessageWithLikes.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				if((length-count<=10) & (Integer.parseInt(pair.getValue().toString()) > 0)){  // Meaning the last 10 and they have to own more than 0 likes
					result.put((String) pair.getKey(), Integer.parseInt(pair.getValue().toString()));
				}
				count++;
				it.remove();
			}
		}
		System.out.println("Map result");
		System.out.println(result);

		// Convert the result back to json
		List<Message> messages = new ArrayList<Message>();
		Iterator it = result.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			Message e = datastore.getPostById(pair.getKey().toString());
			System.out.println("Debugging point 1");
			System.out.println(e.getId()); // Debugging stage: this gives null
			messages.add(e);
			it.remove();
		}
		System.out.println("Final Message");
		System.out.println(messages);

		Gson gson = new Gson();
		String json = gson.toJson(messages);

		response.getOutputStream().println(json);
	}
}
