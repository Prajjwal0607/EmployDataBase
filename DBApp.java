import java.sql.*;
import java.util.Scanner;

public class DBApp {
    // Change the below according to your DB
    private static final String URL = "jdbc:mysql://localhost:3306/testdb"; // MySQL
    // private static final String URL = "jdbc:postgresql://localhost:5432/testdb"; // PostgreSQL
    private static final String USER = "root";     // Change this to your DB username
    private static final String PASSWORD = "1234";     // Change this to your DB password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("✅ Connected to the database!");

            while (true) {
                System.out.println("\n=== MENU ===");
                System.out.println("1. Add User");
                System.out.println("2. View Users");
                System.out.println("3. Update User");
                System.out.println("4. Delete User");
                System.out.println("5. Exit");
                System.out.print("Choose option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1: addUser(conn, scanner); break;
                    case 2: viewUsers(conn); break;
                    case 3: updateUser(conn, scanner); break;
                    case 4: deleteUser(conn, scanner); break;
                    case 5: System.out.println("Exiting..."); return;
                    default: System.out.println("Invalid option.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addUser(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();

        String sql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, age);
            stmt.executeUpdate();
            System.out.println("✅ User added.");
        }
    }

    public static void viewUsers(Connection conn) throws SQLException {
        String sql = "SELECT * FROM users";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\n--- User List ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Email: %s | Age: %d\n",
                        rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getInt("age"));
            }
        }
    }

    public static void updateUser(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter user ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("New name: ");
        String name = scanner.nextLine();
        System.out.print("New email: ");
        String email = scanner.nextLine();
        System.out.print("New age: ");
        int age = scanner.nextInt();

        String sql = "UPDATE users SET name = ?, email = ?, age = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, age);
            stmt.setInt(4, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("✅ User updated.");
            else System.out.println("❌ User not found.");
        }
    }

    public static void deleteUser(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter user ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) System.out.println("🗑️ User deleted.");
            else System.out.println("❌ User not found.");
        }
    }
}
