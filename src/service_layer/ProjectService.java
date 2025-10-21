package service_layer;

import data_access_layer.IFileStore;
import domain_layer.Project;
import domain_layer.Task;
import domain_layer.User;

import java.util.ArrayList;
import java.util.List;

public class ProjectService implements IService {

    private final IFileStore fileStore;
    private final List<IServiceObserver> observers = new ArrayList<>();

    private final List<Project> projects;
    private final List<User> users;
    private int projectSeq = 1;
    private int taskSeq = 1;

    public ProjectService(IFileStore fileStore) {
        this.fileStore = fileStore;
        this.users = new ArrayList<>(fileStore.loadUsers());
        this.projects = new ArrayList<>(fileStore.loadProjects());
        // Seed default users (managers) if XML has none
        if (this.users.isEmpty()) {
            this.users.add(new User("U001", "Bill Gates", "bill.gates@example.com"));
            this.users.add(new User("U002", "Satya Nadella", "satya.nadella@example.com"));
            this.users.add(new User("U003", "Mark Zuckerberg", "mark.zuckerberg@example.com"));
            this.users.add(new User("U004", "Sam Altman", "sam.altman@example.com"));
            this.fileStore.saveUsers(this.users);
        }
        // Initialize sequences from existing IDs if present
        for (Project p : projects) {
            try {
                int n = Integer.parseInt(p.getId().replaceAll("[^0-9]", ""));
                projectSeq = Math.max(projectSeq, n + 1);
            } catch (Exception ignored) {}
            for (Task t : p.getTasks()) {
                try {
                    int n = Integer.parseInt(t.getId().replaceAll("[^0-9]", ""));
                    taskSeq = Math.max(taskSeq, n + 1);
                } catch (Exception ignored) {}
            }
        }
    }

    @Override
    public List<Project> getAllProjects() {
        return new ArrayList<>(projects);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    @Override
    public void addProject(Project project) {
        if (project.getId() == null || project.getId().isEmpty()) {
            project.setId(generateProjectId());
        }
        projects.add(project);
        fileStore.saveProjects(projects);
        notifyObservers();
    }

    @Override
    public void addTaskToProject(String projectId, Task task) {
        if (task.getId() == null || task.getId().isEmpty()) {
            task.setId(generateTaskId());
        }
        for (Project p : projects) {
            if (p.getId().equals(projectId)) {
                p.addTask(task);
                break;
            }
        }
        fileStore.saveProjects(projects);
        notifyObservers();
    }

    @Override
    public void updateTask(String projectId, Task updatedTask) {
        for (Project p : projects) {
            if (p.getId().equals(projectId)) {
                p.getTasks().replaceAll(task ->
                        task.getId().equals(updatedTask.getId()) ? updatedTask : task
                );
                break;
            }
        }
        fileStore.saveProjects(projects);
        notifyObservers();
    }

    @Override
    public String generateProjectId() {
        return String.format("PRJ-%04d", projectSeq++);
    }

    @Override
    public String generateTaskId() {
        return String.format("T-%03d", taskSeq++);
    }

    @Override
    public void addObserver(IServiceObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IServiceObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (IServiceObserver obs : observers) {
            obs.onDataChanged();
        }
    }
}
