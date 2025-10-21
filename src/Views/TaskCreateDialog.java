package Views;

import domain_layer.Priority;
import domain_layer.Status;
import domain_layer.Task;
import domain_layer.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TaskCreateDialog extends JDialog {
    private JPanel mainPanel;
    private JTextField txtId;
    private JTextField txtDescription;
    private JTextField txtDate;
    private JComboBox<Priority> cmbPriority;
    private JComboBox<Status> cmbStatus;
    private JComboBox<User> cmbResponsible;
    private JButton btnSave;
    private JButton btnCancel;

    private Task task; // Resultado al cerrar con guardar

    public TaskCreateDialog(Frame owner, List<User> users) {
        super(owner, "Crear Tarea", true);
        setContentPane(mainPanel);
        setSize(420, 320);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Poblar combos
        cmbPriority.setModel(new DefaultComboBoxModel<>(Priority.values()));
        cmbStatus.setModel(new DefaultComboBoxModel<>(Status.values()));
        cmbResponsible.setModel(new DefaultComboBoxModel<>(users.toArray(new User[0])));

        // Botones
        btnCancel.addActionListener(e -> {
            task = null;
            dispose();
        });

        btnSave.addActionListener(e -> onSave());

        // Si el usuario cierra con la X, no crear tarea
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                task = null;
                dispose();
            }
        });
    }

    private void onSave() {
        try {
            String id = txtId.getText().trim();
            String description = txtDescription.getText().trim();
            String dateStr = txtDate.getText().trim();
            LocalDate date = LocalDate.parse(dateStr);
            Priority priority = (Priority) cmbPriority.getSelectedItem();
            Status status = (Status) cmbStatus.getSelectedItem();
            User responsible = (User) cmbResponsible.getSelectedItem();

            if (description.isEmpty() || responsible == null) {
                JOptionPane.showMessageDialog(this,
                        "Descripción y responsable son obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            task = new Task(id, description, date, priority, status, responsible);
            dispose();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Fecha inválida. Use el formato: YYYY-MM-DD",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Pre-cargar datos (por ejemplo, ID autogenerado y defaults)
    public void setTask(Task t) {
        if (t == null) return;
        // Solo prellenar los campos visuales; NO asignar this.task aquí
        if (t.getId() != null) txtId.setText(t.getId());
        if (t.getDescription() != null) txtDescription.setText(t.getDescription());
        if (t.getExpectedFinishDate() != null) txtDate.setText(t.getExpectedFinishDate().toString());
        if (t.getPriority() != null) cmbPriority.setSelectedItem(t.getPriority());
        if (t.getStatus() != null) cmbStatus.setSelectedItem(t.getStatus());
        if (t.getResponsible() != null) cmbResponsible.setSelectedItem(t.getResponsible());
        if (t.getId() != null && !t.getId().isEmpty()) txtId.setEditable(false);
    }

    public Task getTask() {
        return task;
    }
}
