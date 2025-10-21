package Models;

import domain_layer.Project;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class ProjectTableModel extends AbstractTableModel {

    private final String[] columnNames = {"ID", "Descripción", "Encargado"};
    private List<Project> projects;

    public ProjectTableModel(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public int getRowCount() {
        return projects.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Project p = projects.get(rowIndex);
        switch (columnIndex) {
            case 0: return p.getId();
            case 1: return p.getDescription();
            case 2: return p.getManager() != null ? p.getManager().getName() : "";
            default: return "";
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Project getProjectAt(int rowIndex) {
        return projects.get(rowIndex);
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
        fireTableDataChanged();
    }
}
