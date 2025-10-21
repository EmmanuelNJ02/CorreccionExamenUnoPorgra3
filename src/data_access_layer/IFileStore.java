package data_access_layer;

import domain_layer.Project;
import domain_layer.User;

import java.util.List;

public interface IFileStore {
    List<User> loadUsers();
    List<Project> loadProjects();

    void saveUsers(List<User> users);
    void saveProjects(List<Project> projects);
}
