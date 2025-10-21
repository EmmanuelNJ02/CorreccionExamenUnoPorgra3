package service_layer;

import domain_layer.Project;
import domain_layer.Task;
import domain_layer.User;

import java.util.List;

public interface IService {
    // Lectura
    List<Project> getAllProjects();
    List<User> getAllUsers();

    // Mutaciones
    void addProject(Project project);
    void addTaskToProject(String projectId, Task task);
    void updateTask(String projectId, Task updatedTask);

    // IDs autogenerados
    String generateProjectId();
    String generateTaskId();

    // Observadores
    void addObserver(IServiceObserver observer);
    void removeObserver(IServiceObserver observer);
}
