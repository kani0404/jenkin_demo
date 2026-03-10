import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {
    public List<File> scanFolder(File folder) {
        List<File> files = new ArrayList<>();
        scanRecursive(folder, files);
        return files;
    }

    private void scanRecursive(File dir, List<File> files) {
        File[] list = dir.listFiles();
        if (list != null) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    scanRecursive(file, files);
                } else {
                    files.add(file);
                }
            }
        }
    }
}