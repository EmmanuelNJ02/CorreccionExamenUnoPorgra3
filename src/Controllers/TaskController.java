package Controllers;

import domain_layer.Task;
import service_layer.IService;

public class TaskController {
    private final IService service;

    public TaskController(IService service) {
        this.service = service;
    }

    public void updateTask(String projectId, Task updatedTask) {
        service.updateTask(projectId, updatedTask);
    }
}
