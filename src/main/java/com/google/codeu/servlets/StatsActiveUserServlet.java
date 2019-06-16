package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/top-10-active-users")
public class StatsActiveUserServlet extends HttpServlet {

	private Datastore datastore;
	private JsonArray userMsgArray;

	@Override
	public void init() {
		datastore = new Datastore();
	}


	private static class userMsg {
		String user;
		int numberMsg;

		private userMsg (String user, int numberMsg) {
			this.user = user;
			this.numberMsg = numberMsg;
		}
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

	/**
	 * Responds with site statistics in JSON.
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
					throws IOException {
		response.setContentType("application/json");

		List<Message> messages = datastore.getAllMessages();

		// Get all users
		ArrayList<String> users = new ArrayList<>();
		for (int i = 0; i < messages.size(); i++) {
			users.add(messages.get(i).getUser());
		}
		System.out.println("ArrayList users");
		System.out.println(users);

		// Get all unique countries
		Set<String> uniqueUsers = new HashSet<>(users);
		System.out.println("Set uniqueUsers");
		System.out.println(uniqueUsers);

		// Then input to a map
		Map<String, Integer> userFreq = new HashMap<>();
		for(String item:uniqueUsers){
			int freq = Collections.frequency(users, item);
			userFreq.put(item,freq);
		}
		System.out.println("Map userFreq");
		System.out.println(userFreq);

		// Get the top 10
		Map<String, Integer> sortedUserFreq = sortByValue(userFreq);
		Map<String, Integer> result = new HashMap<>();
		System.out.println("Map sortedUserFreq");
		System.out.println(sortedUserFreq);

		if(userFreq.size() <= 10){
			System.out.println("Less than 10 is executed");
			result = sortedUserFreq;
		}else{
			System.out.println("More than 10 is executed");
			int length = sortedUserFreq.size();
			int count = 0;

			Iterator it = sortedUserFreq.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				if(length-count<=10){  // Meaning the last 10
					result.put(pair.getKey().toString(), Integer.parseInt(pair.getValue().toString()));
				}
				count++;
				it.remove();
			}
		}
		System.out.println("Map result");
		System.out.println(result);

		// Convert the result to json array
		userMsgArray = new JsonArray();
		Gson gson = new Gson();

		Iterator it = result.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			String user = pair.getKey().toString();
			int numberMsg = Integer.parseInt(pair.getValue().toString());
			userMsgArray.add(gson.toJsonTree(new userMsg(user, numberMsg)));
		}
		System.out.println("userMsgArray");
		System.out.println(userMsgArray);

		response.getOutputStream().println(userMsgArray.toString());

	}
}
