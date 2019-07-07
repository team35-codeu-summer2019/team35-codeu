package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
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

	public static Map<Message, Integer> sortByValue(Map<Message, Integer> hm)
	{
		// Create a list from elements of HashMap
		List<Map.Entry<Message, Integer> > list =
			new LinkedList<Map.Entry<Message, Integer> >(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<Message, Integer> >() {
			public int compare(Map.Entry<Message, Integer> o1,
			                   Map.Entry<Message, Integer> o2)
			{
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<Message, Integer> temp = new LinkedHashMap<Message, Integer>();
		for (Map.Entry<Message, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException {
		List<Message> m = datastore.getAllMessages();
		Map<Message, Integer> messageWithLikes = new HashMap<>();

		for(int i = 0; i < m.size(); i ++){
			 messageWithLikes.put(m.get(i), datastore.getLikesByPost(m.get(i).getId().toString()).size());
		}

		Map<Message, Integer> sortedMessageWithLikes = sortByValue(messageWithLikes);
		Map<Message, Integer> result = new HashMap<>();

		if(messageWithLikes.size() <= 10){
			System.out.println("Less than 10 is executed");
			result = sortedMessageWithLikes;
		}else{
			System.out.println("More than 10 is executed");
			int length = sortedMessageWithLikes.size();
			int count = 0;

			Iterator it = sortedMessageWithLikes.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				if(length-count<=10){  // Meaning the last 10
					result.put((Message) pair.getKey(), Integer.parseInt(pair.getValue().toString()));
				}
				count++;
				it.remove();
			}
		}
		System.out.println("Map result");
		System.out.println(result);

		List<Message> resultMessages = new ArrayList<>();
		Gson gson = new Gson();
		String json = gson.toJson(resultMessages);

		response.getOutputStream().println(json);
	}
}
