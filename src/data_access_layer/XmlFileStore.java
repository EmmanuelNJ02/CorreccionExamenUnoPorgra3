package data_access_layer;

import domain_layer.Project;
import domain_layer.Task;
import domain_layer.User;
import domain_layer.Priority;
import domain_layer.Status;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class XmlFileStore implements IFileStore {

    private final File file;

    public XmlFileStore(String filePath) {
        this.file = new File(filePath);
    }

    // ----------- Load Users -----------
    @Override
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        if (!file.exists()) return users;

        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(file);
            NodeList userNodes = doc.getElementsByTagName("user");

            for (int i = 0; i < userNodes.getLength(); i++) {
                Element e = (Element) userNodes.item(i);
                String id = e.getElementsByTagName("id").item(0).getTextContent();
                String name = e.getElementsByTagName("name").item(0).getTextContent();
                String email = e.getElementsByTagName("email").item(0).getTextContent();
                users.add(new User(id, name, email));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    // ----------- Save Users -----------
    @Override
    public void saveUsers(List<User> users) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("data");
            doc.appendChild(root);

            Element usersElement = doc.createElement("users");
            root.appendChild(usersElement);

            for (User u : users) {
                Element userEl = doc.createElement("user");

                Element id = doc.createElement("id");
                id.setTextContent(u.getId());
                userEl.appendChild(id);

                Element name = doc.createElement("name");
                name.setTextContent(u.getName());
                userEl.appendChild(name);

                Element email = doc.createElement("email");
                email.setTextContent(u.getEmail());
                userEl.appendChild(email);

                usersElement.appendChild(userEl);
            }

            // Preserve existing projects
            Element projectsElement = doc.createElement("projects");
            root.appendChild(projectsElement);
            for (Project p : loadProjects()) {
                Element projectEl = doc.createElement("project");

                Element id = doc.createElement("id");
                id.setTextContent(p.getId());
                projectEl.appendChild(id);

                Element desc = doc.createElement("description");
                desc.setTextContent(p.getDescription());
                projectEl.appendChild(desc);

                Element managerId = doc.createElement("managerId");
                managerId.setTextContent(p.getManager() != null ? p.getManager().getId() : "");
                projectEl.appendChild(managerId);

                Element tasksElement = doc.createElement("tasks");
                for (Task t : p.getTasks()) {
                    Element taskEl = doc.createElement("task");

                    Element tid = doc.createElement("id");
                    tid.setTextContent(t.getId());
                    taskEl.appendChild(tid);

                    Element tdesc = doc.createElement("description");
                    tdesc.setTextContent(t.getDescription());
                    taskEl.appendChild(tdesc);

                    Element date = doc.createElement("expectedFinishDate");
                    date.setTextContent(t.getExpectedFinishDate().toString());
                    taskEl.appendChild(date);

                    Element priority = doc.createElement("priority");
                    priority.setTextContent(t.getPriority().name());
                    taskEl.appendChild(priority);

                    Element status = doc.createElement("status");
                    status.setTextContent(t.getStatus().name());
                    taskEl.appendChild(status);

                    Element responsibleId = doc.createElement("responsibleId");
                    responsibleId.setTextContent(t.getResponsible() != null ? t.getResponsible().getId() : "");
                    taskEl.appendChild(responsibleId);

                    tasksElement.appendChild(taskEl);
                }
                projectEl.appendChild(tasksElement);
                projectsElement.appendChild(projectEl);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(file));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------- Load Projects -----------
    @Override
    public List<Project> loadProjects() {
        List<Project> projects = new ArrayList<>();
        if (!file.exists()) return projects;

        try {
            Document doc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder().parse(file);
            NodeList projectNodes = doc.getElementsByTagName("project");
            // Build user map for resolution
            List<User> users = loadUsers();
            java.util.Map<String, User> userMap = new java.util.HashMap<>();
            for (User u : users) userMap.put(u.getId(), u);

            for (int i = 0; i < projectNodes.getLength(); i++) {
                Element e = (Element) projectNodes.item(i);
                String id = e.getElementsByTagName("id").item(0).getTextContent();
                String desc = e.getElementsByTagName("description").item(0).getTextContent();

                // Manager (user reference)
                String managerId = e.getElementsByTagName("managerId").item(0).getTextContent();
                User manager = userMap.getOrDefault(managerId, new User(managerId, "", ""));

                Project p = new Project(id, desc, manager);

                // Load tasks
                NodeList taskNodes = e.getElementsByTagName("task");
                for (int j = 0; j < taskNodes.getLength(); j++) {
                    Element t = (Element) taskNodes.item(j);
                    String tid = t.getElementsByTagName("id").item(0).getTextContent();
                    String tdesc = t.getElementsByTagName("description").item(0).getTextContent();
                    String dateStr = t.getElementsByTagName("expectedFinishDate").item(0).getTextContent();
                    LocalDate date = LocalDate.parse(dateStr);
                    Priority priority = Priority.valueOf(t.getElementsByTagName("priority").item(0).getTextContent());
                    Status status = Status.valueOf(t.getElementsByTagName("status").item(0).getTextContent());
                    String responsibleId = t.getElementsByTagName("responsibleId").item(0).getTextContent();
                    User responsible = userMap.getOrDefault(responsibleId, new User(responsibleId, "", ""));

                    Task task = new Task(tid, tdesc, date, priority, status, responsible);
                    p.addTask(task);
                }

                projects.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projects;
    }

    // ----------- Save Projects -----------
    @Override
    public void saveProjects(List<Project> projects) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("data");
            doc.appendChild(root);

            // Write users first (preserve current users)
            Element usersElement = doc.createElement("users");
            root.appendChild(usersElement);
            for (User u : loadUsers()) {
                Element userEl = doc.createElement("user");

                Element id = doc.createElement("id");
                id.setTextContent(u.getId());
                userEl.appendChild(id);

                Element name = doc.createElement("name");
                name.setTextContent(u.getName());
                userEl.appendChild(name);

                Element email = doc.createElement("email");
                email.setTextContent(u.getEmail());
                userEl.appendChild(email);

                usersElement.appendChild(userEl);
            }

            Element projectsElement = doc.createElement("projects");
            root.appendChild(projectsElement);

            for (Project p : projects) {
                Element projectEl = doc.createElement("project");

                Element id = doc.createElement("id");
                id.setTextContent(p.getId());
                projectEl.appendChild(id);

                Element desc = doc.createElement("description");
                desc.setTextContent(p.getDescription());
                projectEl.appendChild(desc);

                Element managerId = doc.createElement("managerId");
                managerId.setTextContent(p.getManager() != null ? p.getManager().getId() : "");
                projectEl.appendChild(managerId);

                Element tasksElement = doc.createElement("tasks");
                for (Task t : p.getTasks()) {
                    Element taskEl = doc.createElement("task");

                    Element tid = doc.createElement("id");
                    tid.setTextContent(t.getId());
                    taskEl.appendChild(tid);

                    Element tdesc = doc.createElement("description");
                    tdesc.setTextContent(t.getDescription());
                    taskEl.appendChild(tdesc);

                    Element date = doc.createElement("expectedFinishDate");
                    date.setTextContent(t.getExpectedFinishDate().toString());
                    taskEl.appendChild(date);

                    Element priority = doc.createElement("priority");
                    priority.setTextContent(t.getPriority().name());
                    taskEl.appendChild(priority);

                    Element status = doc.createElement("status");
                    status.setTextContent(t.getStatus().name());
                    taskEl.appendChild(status);

                    Element responsibleId = doc.createElement("responsibleId");
                    responsibleId.setTextContent(t.getResponsible() != null ? t.getResponsible().getId() : "");
                    taskEl.appendChild(responsibleId);

                    tasksElement.appendChild(taskEl);
                }
                projectEl.appendChild(tasksElement);

                projectsElement.appendChild(projectEl);
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(doc), new StreamResult(file));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
