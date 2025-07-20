package gui;

import logic.FileOrganizer;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileOrganizerGUI extends JFrame {
    private final JTextField pathField = new JTextField(30);
    private File selectedDir;

    public FileOrganizerGUI() {
        setTitle("File Organizer");
        setSize(500, 150);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel top = new JPanel();
        top.add(new JLabel("Folder:"));
        pathField.setEditable(false);
        top.add(pathField);
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(e -> chooseFolder());
        top.add(browseButton);

        JPanel bottom = new JPanel();
        JButton organizeButton = new JButton("Organize");
        organizeButton.addActionListener(e -> organize());
        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> undo());

        bottom.add(organizeButton);
        bottom.add(undoButton);

        add(top, BorderLayout.NORTH);
        add(bottom, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void chooseFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedDir = chooser.getSelectedFile();
            pathField.setText(selectedDir.getAbsolutePath());
        }
    }

    private void organize() {
        if (selectedDir == null) {
            JOptionPane.showMessageDialog(this, "No folder selected.");
            return;
        }
        try {
            new FileOrganizer(selectedDir).organize();
            JOptionPane.showMessageDialog(this, "Files organized.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void undo() {
        if (selectedDir == null) return;
        try {
            new FileOrganizer(selectedDir).undo();
            JOptionPane.showMessageDialog(this, "Undo successful.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Undo failed: " + ex.getMessage());
        }
    }
}