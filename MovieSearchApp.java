import java.sql.*;
import java.util.*;

public class MovieSearchApp {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/moviedb";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Welcome to the Movie Search Application!");
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nMenu:");
                System.out.println("1. Search by Title");
                System.out.println("2. Filter by Genre");
                System.out.println("3. Filter by Year");
                System.out.println("4. Filter by Rating");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter movie title: ");
                        String title = scanner.nextLine();
                        searchByTitle(connection, title);
                        break;
                    case 2:
                        System.out.print("Enter genre: ");
                        String genre = scanner.nextLine();
                        filterByGenre(connection, genre);
                        break;
                    case 3:
                        System.out.print("Enter year of release: ");
                        int year = scanner.nextInt();
                        filterByYear(connection, year);
                        break;
                    case 4:
                        System.out.print("Enter minimum rating: ");
                        double rating = scanner.nextDouble();
                        filterByRating(connection, rating);
                        break;
                    case 5:
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection error.");
        }
    }

    private static void searchByTitle(Connection connection, String title) {
        String query = "SELECT * FROM movies WHERE title LIKE ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + title + "%");
            displayResults(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void filterByGenre(Connection connection, String genre) {
        String query = "SELECT * FROM movies WHERE genre = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, genre);
            displayResults(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void filterByYear(Connection connection, int year) {
        String query = "SELECT * FROM movies WHERE release_year = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, year);
            displayResults(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void filterByRating(Connection connection, double rating) {
        String query = "SELECT * FROM movies WHERE rating >= ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, rating);
            displayResults(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayResults(ResultSet resultSet) throws SQLException {
        boolean hasResults = false;
        while (resultSet.next()) {
            hasResults = true;
            System.out.println("\nTitle: " + resultSet.getString("title"));
            System.out.println("Genre: " + resultSet.getString("genre"));
            System.out.println("Release Year: " + resultSet.getInt("release_year"));
            System.out.println("Rating: " + resultSet.getDouble("rating"));
            System.out.println("Director: " + resultSet.getString("director"));
            System.out.println("Cast: " + resultSet.getString("cast"));
            System.out.println("Description: " + resultSet.getString("description"));
        }
        if (!hasResults) {
            System.out.println("No results found.");
        }
    }
}
