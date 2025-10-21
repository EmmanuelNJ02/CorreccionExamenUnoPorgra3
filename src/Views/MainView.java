package Views;

import Controllers.ProjectController;
import Models.ProjectTableModel;
import Models.TaskTableModel;
import domain_layer.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainView extends JFrame {
    private JPanel mainPanel;
    private JTable tblProjects;
    private JTable tblTasks;
    private JButton btnAddProject;
    private JButton btnAddTask;
    private JButton btnExit;

    private final ProjectController controller;
    private ProjectTableModel projectTableModel;
    private TaskTableModel taskTableModel;

    public MainView(ProjectController controller) {
        this.controller = controller;
        setTitle("Gestión de Proyectos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);

        // Cargar proyectos iniciales
        projectTableModel = new ProjectTableModel(this.controller.getProjects());
        tblProjects.setModel(projectTableModel);

        // Inicialmente, no hay tareas
        taskTableModel = new TaskTableModel(java.util.Collections.emptyList());
        tblTasks.setModel(taskTableModel);
        btnAddTask.setToolTipText("Seleccione un proyecto para habilitar");
        btnAddProject.setToolTipText("Crear un nuevo proyecto");

        // Eventos
        tblProjects.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblProjects.getSelectedRow();
                if (row >= 0) {
                    Project p = projectTableModel.getProjectAt(row);
                    taskTableModel.setTasks(p.getTasks());
                    btnAddTask.setEnabled(true);
                }
            }
        });

        btnAddProject.addActionListener(e -> {
            ProjectForm form = new ProjectForm(MainView.this, this.controller.getUsers());
            // Autogenerar ID y bloquear edición del ID
            String newId = this.controller.nextProjectId();
            form.setAutoId(newId);
            form.setVisible(true);
            Project created = form.getProject();
            if (created != null) {
                this.controller.addProject(created);
                projectTableModel.setProjects(this.controller.getProjects());
            }
        });

        btnAddTask.addActionListener(e -> {
            int row = tblProjects.getSelectedRow();
            if (row >= 0) {
                Project p = projectTableModel.getProjectAt(row);
                // Usar TaskCreateDialog para crear nuevas tareas
                TaskCreateDialog dlg = new TaskCreateDialog(MainView.this, this.controller.getUsers());
                Task temp = new Task();
                temp.setId(this.controller.nextTaskId());
                temp.setStatus(Status.ABIERTA);
                temp.setPriority(Priority.MEDIA);
                dlg.setTask(temp);
                dlg.setVisible(true);
                Task created = dlg.getTask();
                if (created != null) {
                    this.controller.addTask(p.getId(), created);
                    // refresh
                    projectTableModel.setProjects(this.controller.getProjects());
                    // reselect project and update tasks table
                    tblProjects.setRowSelectionInterval(row, row);
                    taskTableModel.setTasks(projectTableModel.getProjectAt(row).getTasks());
                }
            } else {
                JOptionPane.showMessageDialog(MainView.this,
                        "Selecciona un proyecto primero.");
            }
        });

        // Doble click en tarea para editar prioridad/estado
        tblTasks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int prow = tblProjects.getSelectedRow();
                    int trow = tblTasks.getSelectedRow();
                    if (prow >= 0 && trow >= 0) {
                        Project p = projectTableModel.getProjectAt(prow);
                        Task selectedTask = taskTableModel.getTaskAt(trow);
                        TaskEditDialog dlg = new TaskEditDialog(MainView.this, MainView.this.controller.getUsers());
                        dlg.setTask(selectedTask);
                        dlg.setVisible(true);
                        Task edited = dlg.getTask();
                        if (edited != null) {
                            MainView.this.controller.updateTask(p.getId(), edited);
                            // refresh
                            projectTableModel.setProjects(MainView.this.controller.getProjects());
                            tblProjects.setRowSelectionInterval(prow, prow);
                            taskTableModel.setTasks(projectTableModel.getProjectAt(prow).getTasks());
                        }
                    }
                }
            }
        });

        btnExit.addActionListener(e -> System.exit(0));

        // Estado inicial sin proyecto seleccionado
        btnAddTask.setEnabled(false);
    }
}
