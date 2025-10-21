package Controllers;

import domain_layer.Project;
import domain_layer.Task;
import domain_layer.User;
import service_layer.IService;
import service_layer.IServiceObserver;

import java.util.List;

public class ProjectController implements IServiceObserver {
    private final IService service;

    public ProjectController(IService service) {
        this.service = service;
        this.service.addObserver(this);
    }

    // --------- Métodos para Proyectos ---------
    public List<Project> getProjects() {
        return service.getAllProjects();
    }

    public void addProject(Project project) {
        service.addProject(project);
    }

    // --------- Métodos para Tareas ---------
    public void addTask(String projectId, Task task) {
        service.addTaskToProject(projectId, task);
    }

    public void updateTask(String projectId, Task updatedTask) {
        service.updateTask(projectId, updatedTask);
    }

    // --------- Usuarios y IDs ---------
    public List<User> getUsers() { return service.getAllUsers(); }
    public String nextProjectId() { return service.generateProjectId(); }
    public String nextTaskId() { return service.generateTaskId(); }

    @Override
    public void onDataChanged() {
        // Aquí se notificaría a la vista que debe refrescar la lista
        System.out.println("Datos actualizados en ProjectController");
    }
}
