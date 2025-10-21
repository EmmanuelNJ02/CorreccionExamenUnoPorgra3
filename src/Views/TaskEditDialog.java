package Views;

import domain_layer.Priority;
import domain_layer.Status;
import domain_layer.Task;
import domain_layer.User;

import javax.swing.*;
import java.awt.*;

public class TaskEditDialog extends JDialog {
    private JPanel mainPanel;
    private JLabel lblId;
    private JLabel lblDescription;
    private JLabel lblDate;
    private JComboBox<Priority> cmbPriority;
    private JComboBox<Status> cmbStatus;
    private JLabel lblResponsible;
    private JButton btnSave;
    private JButton btnCancel;

    private Task task;

    public TaskEditDialog(Frame owner, java.util.List<User> users) {
        super(owner, "Editar Tarea", true);
        setContentPane(mainPanel);
        setSize(400, 300);
        setLocationRelativeTo(owner);

        // Poblar combos (solo priority y status)
        cmbPriority.setModel(new DefaultComboBoxModel<>(Priority.values()));
        cmbStatus.setModel(new DefaultComboBoxModel<>(Status.values()));

        // Eventos
        btnCancel.addActionListener(e -> {
            task = null;
            dispose();
        });

        btnSave.addActionListener(e -> {
            // Solo actualizar prioridad y estado
            if (task != null) {
                Priority priority = (Priority) cmbPriority.getSelectedItem();
                Status status = (Status) cmbStatus.getSelectedItem();
                task.setPriority(priority);
                task.setStatus(status);
            }
            dispose();
        });
    }

    public void setTask(Task t) {
        if (t != null) {
            this.task = t;

            if (t.getPriority() != null) cmbPriority.setSelectedItem(t.getPriority());
            if (t.getStatus() != null) cmbStatus.setSelectedItem(t.getStatus());
        }
    }

    public Task getTask() {
        return task;
    }
}
