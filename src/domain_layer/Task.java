package domain_layer;

import java.time.LocalDate;
import java.util.Objects;

public class Task {
    private String id; // autogenerado: T001, T002...
    private String description;
    private LocalDate expectedFinishDate;
    private Priority priority;
    private Status status;
    private User responsible;

    public Task() {}

    public Task(String id, String description, LocalDate expectedFinishDate,
                Priority priority, Status status, User responsible) {
        this.id = id;
        this.description = description;
        this.expectedFinishDate = expectedFinishDate;
        this.priority = priority;
        this.status = status;
        this.responsible = responsible;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getExpectedFinishDate() { return expectedFinishDate; }
    public void setExpectedFinishDate(LocalDate expectedFinishDate) { this.expectedFinishDate = expectedFinishDate; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public User getResponsible() { return responsible; }
    public void setResponsible(User responsible) { this.responsible = responsible; }

    @Override
    public String toString() {
        return id + " - " + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
