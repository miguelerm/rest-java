package todo.restapi.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.sun.jersey.api.NotFoundException;

import todo.restapi.models.Task;
import todo.restapi.repos.TaskRepository;

@Path("/tasks")
public class TasksResource {

	@Context
	UriInfo uriInfo;
	
	TaskRepository repo = new TaskRepository();
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Task[] get(@QueryParam("isDone") Boolean onlyCompleted) {
		if (onlyCompleted == null) {
			return repo.getAll();
		} else if (onlyCompleted) {
			return repo.getCompleted();
		} else {
			return repo.getPending();
		}
	}
	
	@GET
	@Path("{id: \\d+}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Task getById(@PathParam("id") int id) {
		
		Task task = repo.getById(id);
		
		if (task == null) {
			throw new NotFoundException("Tarea " + Integer.toString(id) + " no encontrada.");
		}
		
		return task;
	}
	
	@POST
	@Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response add(Task model) {
		int id = repo.add(model.getTitle(), model.getSummary());
		return Response.created(uriInfo.getAbsolutePathBuilder().path(Integer.toString(id)).build()).build();
	}
	
	@PUT
	@Path("{id: \\d+}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response update(@PathParam("id") int id, Task model) {
		
		Task task = repo.getById(id);
		
		if (task == null) {
			throw new NotFoundException("Tarea " + Integer.toString(id) + " no encontrada.");
		}
		
		task.setTitle(model.getTitle());
		task.setSummary(model.getSummary());
		task.setDone(model.isDone());
		
		return Response.ok().entity(task).build();
		
	}
	
	@DELETE
	@Path("{id: \\d+}")
	public Response delete(@PathParam("id") int id) {
		
		try {
			
			repo.remove(id);
			return Response.ok().build();
			
		} catch (IllegalStateException e) {
			throw new NotFoundException("Tarea " + Integer.toString(id) + " no encontrada.");
		}			

	}
	
}
