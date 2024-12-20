package org.example.ui;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PDFViewer extends JFrame {
    private PDDocument document;
    private PDFRenderer renderer;
    private int currentPage = 0;
    private double zoomFactor = 1.0; // Zoom factor (default 100%)
    private JLabel pdfLabel;
    private JLabel pageNumberLabel;

    public PDFViewer(String filePath) {
        setTitle("PDF Viewer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel for navigation and PDF display
        JPanel mainPanel = new JPanel(new BorderLayout());
        pdfLabel = new JLabel("", SwingConstants.CENTER);
        JScrollPane scrollPane = new JScrollPane(pdfLabel);

        // Page number label
        pageNumberLabel = new JLabel("Page 1 / 1", SwingConstants.CENTER);
        pageNumberLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pageNumberLabel.setForeground(Color.BLACK);

        // Navigation controls panel
        JPanel navigationPanel = new JPanel(new FlowLayout());
        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        JButton zoomInButton = new JButton("Zoom In");
        JButton zoomOutButton = new JButton("Zoom Out");

        prevButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                displayPage(currentPage);
            } else {
                JOptionPane.showMessageDialog(this, "Already on the first page!");
            }
        });

        nextButton.addActionListener(e -> {
            if (currentPage < document.getNumberOfPages() - 1) {
                currentPage++;
                displayPage(currentPage);
            } else {
                JOptionPane.showMessageDialog(this, "Already on the last page!");
            }
        });

        zoomInButton.addActionListener(e -> {
            zoomFactor += 0.1;
            displayPage(currentPage); // Re-render the current page with zoom
        });

        zoomOutButton.addActionListener(e -> {
            if (zoomFactor > 0.2) { // Prevent zoom out too much
                zoomFactor -= 0.1;
                displayPage(currentPage); // Re-render the current page with zoom
            } else {
                JOptionPane.showMessageDialog(this, "Minimum zoom reached!");
            }
        });

        navigationPanel.add(prevButton);
        navigationPanel.add(nextButton);
        navigationPanel.add(zoomInButton);
        navigationPanel.add(zoomOutButton);

        // Add components to main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(pageNumberLabel, BorderLayout.NORTH);
        mainPanel.add(navigationPanel, BorderLayout.SOUTH);

        add(mainPanel);

        try {
            // Load the PDF document
            File pdfFile = new File(filePath);
            if (!pdfFile.exists()) {
                throw new IOException("File not found: " + filePath);
            }

            document = PDDocument.load(pdfFile);
            renderer = new PDFRenderer(document);

            // Render the first page
            displayPage(currentPage);

        } catch (IOException e) {
            e.printStackTrace();
            showError("Unable to open PDF. Check the file path or ensure the file is a valid PDF.");
        }
    }

    private void displayPage(int pageIndex) {
        try {
            // Render page with zoom factor (adjust DPI)
            BufferedImage image = renderer.renderImageWithDPI(pageIndex, (float) (50 * zoomFactor));
            ImageIcon icon = new ImageIcon(image);
            pdfLabel.setIcon(icon);
            pdfLabel.revalidate();

            // Update page number label
            pageNumberLabel.setText("Page " + (currentPage + 1) + " / " + document.getNumberOfPages());

        } catch (IOException e) {
            e.printStackTrace();
            showError("Unable to render page " + pageIndex);
        }
    }

    private void showError(String message) {
        pdfLabel.setText("<html><div style='color:red; text-align:center;'>" + message + "</div></html>");
        pdfLabel.setIcon(null);
    }

    @Override
    public void dispose() {
        super.dispose();
        try {
            if (document != null) {
                document.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
}
}
}