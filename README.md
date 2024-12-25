JAVA E-BOOK READER

Overview

This is a Java Swing-based eBook Reader application with an admin and user interface. It allows admins to manage books (add, delete, and view books with cover images) and users to view and read books by selecting their cover images. The application uses MySQL for database management and Apache PDFBox for rendering PDFs.

Features:

-Admin Panel:

    Add Books: Upload books with title, PDF file path, and cover image path.
    Delete Books: Remove selected books from the database.
    View Books: Displays a list of all added books in a table.
    Logout: Redirects back to the login screen.

-User Panel:

    Book Covers: Displays book covers in a grid layout.
    Read Books: Open a book for reading by clicking on its cover image.
    Logout: Redirects back to the login screen.

Technologies Used:

    Java Swing: For building the graphical user interface.
    MySQL: For database management.
    Apache PDFBox: For reading and rendering PDF files.
    IntelliJ IDEA Ultimate: IDE for development.

Setup Instructions:

-Prerequisites:

    Java Development Kit (JDK) 8 or higher.
    IntelliJ IDEA Ultimate (or any Java-compatible IDE).
    MySQL installed (use built-in MySQL for IntelliJ IDEA Ultimate).
    Apache PDFBox library. Download it from Apache PDFBox.

-Database Setup:

Open your MySQL database and execute the following SQL to create the database and table:


    CREATE DATABASE ebook_reader;
    USE ebook_reader;

    CREATE TABLE books (
        id INT AUTO_INCREMENT PRIMARY KEY,
        title VARCHAR(255) NOT NULL,
        file_path VARCHAR(255) NOT NULL,
        cover_path VARCHAR(255) NOT NULL
    );
    
Update the DatabaseConnection class with your database connection credentials:

    public class DatabaseConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/ebook_reader";
        private static final String USERNAME = "your_username";
        private static final String PASSWORD = "your_password";

        public static Connection getConnection() throws Exception {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
          }
    }
    
How to Run:

    Clone or download the project files to your system.
    Open the project in IntelliJ IDEA Ultimate.
    Add the Apache PDFBox library:
    Go to File > Project Structure > Libraries.
    Add the PDFBox JAR file (e.g., pdfbox-2.x.x.jar).
    Run the following classes:
    LoginScreen.java: The entry point of the application.
    AdminPanel.java: Access the admin functionalities.
    UserPanel.java: Access the user functionalities.

Usage:

-Admin Panel:

    Login with admin credentials.
    Use the Add Book section to upload a book's title, PDF file, and cover image.
    View all books in the table. Select a book and click Delete Selected Book to remove it.
    Click Logout to return to the login screen.

-User Panel:

    Login with user credentials.
    Browse the grid of book covers.
    Click on a book cover to open the book in the PDF viewer.
    Click Logout to return to the login screen.

Folder Structure:

    src/
    ├── com.example.ebookreader.database/
    │   └── DatabaseConnection.java      # MySQL database connection class
    ├── com.example.ebookreader.ui/
    │   ├── LoginScreen.java             # Login interface
    │   ├── AdminPanel.java              # Admin panel for managing books
    │   ├── UserPanel.java               # User panel for reading books
    │   └── PDFViewer.java               # PDF reader implementation
    └── resources/
         └── (Book PDFs and Cover Images)

Sample Screenshots:

-Admin Panel:

    Add books with title, PDF path, and cover image path.
    View and delete books in a table format.

-User Panel:

    Display books as cover images.
    Read books by clicking on their covers.
    
Future Improvements:

    Add authentication for admin and user accounts.
    Enable book search functionality in the User Panel.
    Allow users to bookmark pages in the PDF viewer.

Demo link>>
https://youtu.be/uCvg2nZWP6g?si=eUjsCOcxpXAx92jg
