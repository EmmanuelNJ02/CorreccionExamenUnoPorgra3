import Controllers.ProjectController;
import data_access_layer.IFileStore;
import data_access_layer.XmlFileStore;
import service_layer.ProjectService;
import Views.MainView;
import utilities.FileManagement;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Ruta del archivo XML
        String filePath = "data.xml";

        // Asegurar que el archivo existe
        FileManagement.ensureFileExists(filePath);

        // Crear FileStore y Service
        IFileStore fileStore = new XmlFileStore(filePath);
        ProjectService service = new ProjectService(fileStore);

        // Crear controlador principal
        ProjectController controller = new ProjectController(service);

        // Lanzar GUI en el hilo de Swing
        SwingUtilities.invokeLater(() -> {
            MainView view = new MainView(controller);
            view.setVisible(true);
        });
    }
}
