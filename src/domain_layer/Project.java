package domain_layer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Project {
    private String id; // autogenerado: P001, P002...
    private String description;
    private User manager; // encargado general
    private final List<Task> tasks = new ArrayList<>();

    public Project() {}

    public Project(String id, String description, User manager) {
        this.id = id;
        this.description = description;
        this.manager = manager;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public User getManager() { return manager; }
    public void setManager(User manager) { this.manager = manager; }

    public List<Task> getTasks() { return tasks; }

    public void addTask(Task task) { tasks.add(task); }
    public boolean removeTask(Task task) { return tasks.remove(task); }

    @Override
    public String toString() {
        return id + " - " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
