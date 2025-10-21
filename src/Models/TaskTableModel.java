package Models;

import domain_layer.Task;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TaskTableModel extends AbstractTableModel {

    private final String[] columnNames = {"ID", "Descripción", "Fecha Fin", "Prioridad", "Estado", "Responsable"};
    private List<Task> tasks;

    public TaskTableModel(List<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Task t = tasks.get(rowIndex);
        switch (columnIndex) {
            case 0: return t.getId();
            case 1: return t.getDescription();
            case 2: return t.getExpectedFinishDate();
            case 3: return t.getPriority();
            case 4: return t.getStatus();
            case 5: return t.getResponsible() != null ? t.getResponsible().getName() : "";
            default: return "";
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Task getTaskAt(int rowIndex) {
        return tasks.get(rowIndex);
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        fireTableDataChanged();
    }
}
