import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class MainGUI extends JFrame {
    private JButton selectFolderButton;
    private JButton scanButton;
    private JTextArea resultArea;
    private File selectedFolder;

    public MainGUI() {
        setTitle("Duplicate File Detector");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("Duplicate File Detector", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        // Center panel
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout());
        selectFolderButton = new JButton("Select Folder");
        scanButton = new JButton("Scan Files");
        scanButton.setEnabled(false);
        centerPanel.add(selectFolderButton);
        centerPanel.add(scanButton);
        add(centerPanel, BorderLayout.CENTER);

        // Result area
        resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.SOUTH);

        // Event listeners
        selectFolderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(MainGUI.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    selectedFolder = chooser.getSelectedFile();
                    scanButton.setEnabled(true);
                }
            }
        });

        scanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedFolder != null) {
                    FileScanner scanner = new FileScanner();
                    List<File> files = scanner.scanFolder(selectedFolder);
                    DuplicateFinder finder = new DuplicateFinder();
                    Map<String, List<File>> duplicates = finder.findDuplicates(files);
                    displayResults(duplicates);
                }
            }
        });

        pack();
        setVisible(true);
    }

    private void displayResults(Map<String, List<File>> duplicates) {
        resultArea.setText("");
        if (duplicates.isEmpty()) {
            resultArea.append("No duplicate files found.\n");
        } else {
            for (Map.Entry<String, List<File>> entry : duplicates.entrySet()) {
                resultArea.append("Duplicate group:\n");
                for (File file : entry.getValue()) {
                    resultArea.append("  " + file.getAbsolutePath() + "\n");
                }
                resultArea.append("\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI());
    }
}