package utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileManagement {
    public static boolean ensureFileExists(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    try (OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<data>\n  <users/>\n  <projects/>\n</data>\n");
                    }
                }
                return true;
            }
            if (file.length() == 0) {
                try (OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                    fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<data>\n  <users/>\n  <projects/>\n</data>\n");
                }
            } else {
                String head = Files.readString(file.toPath(), StandardCharsets.UTF_8);
                if (!head.contains("<data")) {
                    try (OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<data>\n  <users/>\n  <projects/>\n</data>\n");
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
