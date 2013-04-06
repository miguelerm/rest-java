package todo.clients.console;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import todo.clients.models.Task;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class TodoConsoleApplication {

    private static WebResource service;
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if (args == null || args.length == 0) {
			printHelp();
			return;
		}
		
		URI uri = UriBuilder.fromUri("http://localhost:8080/todo.restapi/api/v1").build();
		
		ClientConfig config = new DefaultClientConfig();
	    Client client = Client.create(config);
	    service = client.resource(uri);
		
		String command = args[0].toLowerCase();
		
		if (command.equals("list") && args.length == 1) {			
			list();
		} else if (command.equals("list") && args.length == 2 && args[1].equalsIgnoreCase("completed")) {
			listCompleted();
		} else if (command.equals("list") && args.length == 2 && args[1].equalsIgnoreCase("pendings")) {
			listPendings();
		} else if (command.equals("add") && args.length == 3) {
			add(args[1], args[2]);
		} else if (command.equals("update") && args.length == 4) {
			update(Integer.parseInt(args[1]), args[2], args[3]);
		} else if (command.equals("delete") && args.length == 2) {
			delete(Integer.parseInt(args[1]));
		} else if (command.equals("complete") && args.length == 2) {
			complete(Integer.parseInt(args[1]));
		} else {
			printHelp();
		}		

	}

	private static void add(String title, String summary) {
		
		Task task = new Task();
		
		task.setTitle(title);
		task.setSummary(summary);
		
		ClientResponse currentResponse = service
				.path("tasks")
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, task);
		
		System.out.println("Status: " + currentResponse.getStatus());
	}

	private static void update(int id, String title, String summary) {
		update(id, title, summary, null);
	}
	
	private static void complete(int id) {
		update(id, null, null, true);
	}
	
	private static void update(int id, String title, String summary, Boolean done) {

		Task task = service
				.path("tasks").path(Integer.toString(id))
				.accept(MediaType.APPLICATION_JSON)
				.get(Task.class);
		
		if (title != null) {
			task.setTitle(title);
		}
		
		if (summary != null) {
			task.setSummary(summary);
		}
		
		if (done != null) {
			task.setDone(done.booleanValue());
		}
		
		ClientResponse currentResponse = service
				.path("tasks").path(Integer.toString(id))
				.accept(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, task);
		
		System.out.println("Status: " + currentResponse.getStatus());
		
	}

	private static void delete(int id) {
		ClientResponse currentResponse = service
				.path("tasks").path(Integer.toString(id))
				.accept(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class);
		
		System.out.println("Status: " + currentResponse.getStatus());
	}


	private static void listPendings() {
		
		Task[] tasks = service
				.path("tasks")
				.queryParam("isDone", "false")
				.accept(MediaType.APPLICATION_JSON)
				.get(Task[].class);
		
		printList(tasks);
	}

	private static void listCompleted() {
		
		Task[] tasks = service
				.path("tasks")
				.queryParam("isDone", "true")
				.accept(MediaType.APPLICATION_JSON)
				.get(Task[].class);
		
		printList(tasks);
	}

	private static void list() {
		
		Task[] tasks = service
				.path("tasks")
				.accept(MediaType.APPLICATION_JSON)
				.get(Task[].class);
		
		printList(tasks);
		
	}
	
	private static void printList(Task[] tasks) {
		for (int i = 0; i < tasks.length; i++) {
			
			Task item = tasks[i];
			
			String id = Integer.toString(item.getId());
			String isDone = item.isDone() ? "[x]" : "[ ]";
			String title = item.getTitle();
			
			if (title != null)
			{
				if (title.length() > 10) {
					title = title.substring(0, 9);
				}
			}
			
			System.out.println(isDone + " " + id + ": " + title);
			
		}
	}

	private static void printHelp() {
		System.out.println("help");
	}

}
