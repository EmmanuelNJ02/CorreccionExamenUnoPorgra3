package Views;

import domain_layer.Project;
import domain_layer.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProjectForm extends JDialog {
    private JPanel mainPanel;
    private JTextField txtId;
    private JTextField txtDescription;
    private JComboBox<User> cmbManager;
    private JButton btnSave;
    private JButton btnCancel;

    private Project project;

    public ProjectForm(Frame owner, List<User> users) {
        super(owner, "Nuevo Proyecto", true);
        setContentPane(mainPanel);
        setSize(400, 250);
        setLocationRelativeTo(owner);

        // Poblar combo con usuarios (gerentes posibles)
        cmbManager.setModel(new DefaultComboBoxModel<>(users.toArray(new User[0])));

        // Eventos
        btnCancel.addActionListener(e -> {
            project = null;
            dispose();
        });

        btnSave.addActionListener(e -> {
            String id = txtId.getText().trim();
            String description = txtDescription.getText().trim();
            User manager = (User) cmbManager.getSelectedItem();

            if (id.isEmpty() || description.isEmpty() || manager == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe llenar todos los campos",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            project = new Project(id, description, manager);
            dispose();
        });
    }

    public void setProject(Project p) {
        if (p != null) {
            txtId.setText(p.getId());
            txtDescription.setText(p.getDescription());
            cmbManager.setSelectedItem(p.getManager());
            this.project = p;
        }
    }

    public Project getProject() {
        return project;
    }

    // Helper para preasignar ID autogenerado y bloquear edición
    public void setAutoId(String id) {
        if (id != null) {
            txtId.setText(id);
            txtId.setEditable(false);
        }
    }
}

