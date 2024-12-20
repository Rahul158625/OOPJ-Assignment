package org.example.ui;


import org.example.database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UserPanel extends JFrame {
    public UserPanel() {
        setTitle("User Panel - Read Books");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Top Panel (Header with Logout)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(46, 134, 222));
        JLabel titleLabel = new JLabel("Welcome to eBook Reader!");
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

        // Center Panel (Grid of Covers)
        JPanel booksPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        loadBooks(booksPanel);

        // Add components to the frame
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(booksPanel), BorderLayout.CENTER);
    }

    private void loadBooks(JPanel booksPanel) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT title, cover_path, file_path FROM books";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while (rs.next()) {
                String title = rs.getString("title");
                String coverPath = rs.getString("cover_path");
                String filePath = rs.getString("file_path");

                ImageIcon coverIcon = new ImageIcon(new ImageIcon(coverPath).getImage().getScaledInstance(150, 200, Image.SCALE_SMOOTH));
                JButton bookButton = new JButton(title, coverIcon);
                bookButton.setVerticalTextPosition(SwingConstants.BOTTOM);
                bookButton.setHorizontalTextPosition(SwingConstants.CENTER);

                bookButton.addActionListener(e -> new PDFViewer(filePath).setVisible(true));

                booksPanel.add(bookButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UserPanel().setVisible(true));
}
}