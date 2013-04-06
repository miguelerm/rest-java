package todo.restapi.repos;

import java.util.ArrayList;
import java.util.List;

import todo.restapi.models.Task;

public class TaskRepository {
	
	private final static List<Task> tasks = new ArrayList<Task>();
	
	public TaskRepository() {
		
		if (tasks.isEmpty()) {
			
			Task firstTask = new Task();
			Task secondTask = new Task();
			Task thirdTask = new Task();
			
			firstTask.setId(1);
			firstTask.setTitle("Asistir a JUG-GT");
			firstTask.setSummary("Jueves 4 en el Intecap");
			firstTask.setDone(true);
			
			secondTask.setId(2);
			secondTask.setTitle("Aprender REST");
			secondTask.setSummary("Investigar conceptos e implementaciones en Java.");
			secondTask.setDone(false);

			thirdTask.setId(3);
			thirdTask.setTitle("Aprender OAuth 2.0");
			thirdTask.setSummary("Investigar conceptos e implementaciones en Java.");
			thirdTask.setDone(false);
			
			tasks.add(firstTask);
			tasks.add(secondTask);		
			tasks.add(thirdTask);
			
		}	
		
	}
	
	// Obtiene el id maximo de la lista de tareas y le suma uno.
	private int getNextId() {
		
		int id = 0;
		
		for (int i = 0; i < tasks.size(); i++) {
			int taskId = tasks.get(i).getId();
			
			if (tasks.get(i).getId() > id) {
				id = taskId;
			}
		}
		
		return id + 1;
	}
	
	// Obtiene todas las tareas.
	public Task[] getAll() {
		return tasks.toArray(new Task[tasks.size()]);
	}
	
	// Obtiene la tarea con el id indicado.
	public Task getById(int id) {
		
		for (int i = 0; i < tasks.size(); i++) {
			Task item = tasks.get(i);
			if (item.getId() == id){
				return item;
			}
		}
		
		return null;
	}
	
	// Obtiene las tareas finalizadas.
	public Task[] getCompleted() {
		
		List<Task> result = new ArrayList<Task>();
		
		for (int i = 0; i < tasks.size(); i++) {
			Task item = tasks.get(i);
			if (item.isDone()){
				result.add(item);
			}
		}
		
		return result.toArray(new Task[result.size()]);
		
	}
	
	// Obtiene las tareas pendientes de realizar
	public Task[] getPending() {
		
		List<Task> result = new ArrayList<Task>();
		
		for (int i = 0; i < tasks.size(); i++) {
			Task item = tasks.get(i);
			if (!item.isDone()){
				result.add(item);
			}
		}
		
		return result.toArray(new Task[result.size()]);
		
	}
	
	// Agrega una nueva tarea al repositorio.
	// Obtiene el id generado para persistir la tarea.
	public int add(String title, String summary) {
		
		Task newTask = new Task();
		
		newTask.setTitle(title);
		newTask.setSummary(summary);
		newTask.setDone(false);
		newTask.setId(getNextId());
		
		tasks.add(newTask);
		
		return newTask.getId();
	}
	
	// Modifica los valores de una tarea especifica.
	public void update(int id, String title, String summary, boolean isDone) {
		
		Task task = getById(id);
		
		if (task != null) {
			task.setTitle(title);
			task.setSummary(summary);
			task.setDone(isDone);
		} else {
			throw new IllegalStateException("No existe la tarea " + id);
		}
		
	}
	
	public void remove(int id) {
		
		Task task = getById(id);
		
		if (task == null) {
			throw new IllegalStateException("No existe la tarea " + id);
		}
		
		tasks.remove(task);
		
	}

}
