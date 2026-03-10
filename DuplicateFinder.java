import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateFinder {
    public Map<String, List<File>> findDuplicates(List<File> files) {
        Map<String, List<File>> hashToFiles = new HashMap<>();
        for (File file : files) {
            try {
                String hash = computeHash(file);
                hashToFiles.computeIfAbsent(hash, k -> new ArrayList<>()).add(file);
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        Map<String, List<File>> duplicates = new HashMap<>();
        for (Map.Entry<String, List<File>> entry : hashToFiles.entrySet()) {
            if (entry.getValue().size() > 1) {
                duplicates.put(entry.getKey(), entry.getValue());
            }
        }
        return duplicates;
    }

    private String computeHash(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }
        byte[] hashBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}