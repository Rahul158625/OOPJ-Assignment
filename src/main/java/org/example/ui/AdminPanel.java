package org.example.ui;

import org.example.database.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminPanel extends JFrame {
    private JTable booksTable;
    private DefaultTableModel tableModel;

    public AdminPanel() {
        setTitle("Admin Panel - Manage Books");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel (Header with Logout)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 152, 219));
        JLabel titleLabel = new JLabel("Admin Panel - Manage Books");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(Color.RED);
        logoutButton.setForeground(Color.WHITE);

        logoutButton.addActionListener(e -> {
            new LoginScreen().setVisible(true);
            dispose();
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        // Center Panel (Books Table)
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "File Path", "Cover Path"}, 0);
        booksTable = new JTable(tableModel);
        booksTable.setRowHeight(25);
        booksTable.setSelectionBackground(new Color(46, 204, 113));
        booksTable.setSelectionForeground(Color.WHITE);
        JScrollPane tableScrollPane = new JScrollPane(booksTable);

        loadBooks(); // Load books into the table

        // Bottom Panel (Form and Buttons)
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        JLabel titleLabelForm = new JLabel("Book Title:");
        JTextField titleField = new JTextField();
        JLabel filePathLabel = new JLabel("PDF Path:");
        JTextField filePathField = new JTextField();
        JLabel coverPathLabel = new JLabel("Cover Image Path:");
        JTextField coverPathField = new JTextField();

        JButton browsePdfButton = new JButton("Browse PDF");
        JButton browseCoverButton = new JButton("Browse Cover");
        JButton addBookButton = new JButton("Add Book");
        JButton deleteBookButton = new JButton("Delete Selected Book");

        formPanel.add(titleLabelForm);
        formPanel.add(titleField);
        formPanel.add(filePathLabel);
        formPanel.add(filePathField);
        formPanel.add(browsePdfButton);
        formPanel.add(browseCoverButton);
        formPanel.add(coverPathLabel);
        formPanel.add(coverPathField);
        formPanel.add(addBookButton);
        formPanel.add(deleteBookButton);

        // Button Listeners
        browsePdfButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        browseCoverButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                coverPathField.setText(selectedFile.getAbsolutePath());
            }
        });

        addBookButton.addActionListener(e -> {
            String title = titleField.getText();
            String filePath = filePathField.getText();
            String coverPath = coverPathField.getText();

            if (!title.isEmpty() && !filePath.isEmpty() && !coverPath.isEmpty()) {
                addBookToDatabase(title, filePath, coverPath);
                loadBooks(); // Refresh table
                titleField.setText("");
                filePathField.setText("");
                coverPathField.setText("");
                JOptionPane.showMessageDialog(this, "Book added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteBookButton.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookId = (int) tableModel.getValueAt(selectedRow, 0); // Get ID from table
                deleteBookFromDatabase(bookId);
                loadBooks(); // Refresh table
                JOptionPane.showMessageDialog(this, "Book deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book to delete!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Add components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);
    }

    private void loadBooks() {
        tableModel.setRowCount(0); // Clear existing rows
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM books";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String filePath = rs.getString("file_path");
                String coverPath = rs.getString("cover_path");
                tableModel.addRow(new Object[]{id, title, filePath, coverPath});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBookToDatabase(String title, String filePath, String coverPath) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO books (title, file_path, cover_path) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, filePath);
            stmt.setString(3, coverPath);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteBookFromDatabase(int bookId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminPanel().setVisible(true));
}
}