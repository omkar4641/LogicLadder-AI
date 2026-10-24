package com.omkar.dsamentor.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.omkar.dsamentor.model.User;

public class UserRepository {
    private static final String URL = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/dsamentor");

    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() {
        try {
            if (USER == null || USER.isBlank()) {
                throw new SQLException("DB_USER environment variable is missing.");
            }

            if (PASSWORD == null || PASSWORD.isBlank()) {
                throw new SQLException("DB_PASSWORD environment variable is missing.");
            }

            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("=== Database Connection Failed! ===");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void insertUser(User user) throws SQLException {
        try (Connection conn = getConnection()) {
            if (conn == null) {
                System.out.println("Something went wrong");
                throw new SQLException("Database connection failed");
            }

            String sql = "Insert into users(username, name, email, pass_hash,onboarding_completed, daily_study_min, last_active_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassHash());
            ps.setBoolean(5, user.isOnBoardingCompleted());
            ps.setInt(6, user.getDailyStudyMin());
            ps.setObject(7, user.getLastActiveAt());

            ps.executeUpdate();
            ps.close();
        }
    }

    public static String FindPassHash(String username) throws SQLException {
        String sql = "SELECT pass_hash FROM users WHERE username = ?";

        try (Connection conn = UserRepository.getConnection()) {

            if (conn == null) {
                throw new SQLException("Database connection failed");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("pass_hash");
                    }

                    return null;
                }
            }
        }
    }

    // retriving user details from DB and recreating the user object for further use
    public static User retrievUserObject(String username) throws SQLException {

        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = getConnection()) {
            if (conn == null) {
                throw new SQLException("Database connection failed");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        String userN = rs.getString("username");
                        String dsaLevel = rs.getString("dsa_level");
                        String userType = rs.getString("user_type");
                        boolean onboardingCompleted = rs.getBoolean("onboarding_completed");
                        int dailyStudyMin = rs.getInt("daily_study_min");
                        LocalDateTime lastActiveAt = rs.getObject("last_active_at", LocalDateTime.class);

                        return new User(
                                name, email, userN, dsaLevel, userType,
                                onboardingCompleted, dailyStudyMin, lastActiveAt);
                    }

                    return null;
                }
            }
        }
    }

    public static void UpdateUserOnboarding(User user) throws SQLException {
        try (Connection conn = getConnection()) {
            if (conn == null) {
                System.out.println("Something went wrong");
                throw new SQLException("Database connection failed");
            }

            // we are updating the dsa_level and user_type hence the onboaring_completed
            // becomes true
            String sql = "UPDATE users SET dsa_level = ?, user_type = ?, onboarding_completed = TRUE WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getDsaLevel());
            ps.setString(2, user.getUserType());
            ps.setString(3, user.getUsername());
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated != 1) {
                throw new SQLException("Onboarding could not be saved. User not found.");
            }
            ps.close();
        }
    }

    public static void updateDailyStudyMin(String username, int minutes) throws SQLException {

        if (minutes < 15 || minutes > 360) {
            throw new IllegalArgumentException(
                    "Study time must be between 15 and 360 minutes.");
        }

        String sql = """
                UPDATE users
                SET daily_study_min = ?
                WHERE username = ?
                """;

        try (Connection conn = getConnection()) {

            if (conn == null) {
                throw new SQLException(
                        "Database connection failed.");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, minutes);
                ps.setString(2, username);

                int rowsUpdated = ps.executeUpdate();

                if (rowsUpdated != 1) {
                    throw new SQLException(
                            "Could not update study time. User not found.");
                }
            }
        }
    }

    public static void promoteUser(User user) throws SQLException {
        String dsa_level;
        String currentLevel = user.getDsaLevel();

        if (currentLevel == null) {
            throw new IllegalArgumentException("DSA level cannot be null.");
        } else if (currentLevel.equalsIgnoreCase("NEW") || currentLevel.equalsIgnoreCase("BEGINEER")) {
            dsa_level = "BEGINEER1";
        } else if (currentLevel.equalsIgnoreCase("BEGINEER1")) {
            dsa_level = "INTERMEDIATE1";
        } else if (currentLevel.equalsIgnoreCase("INTERMEDIATE") || currentLevel.equalsIgnoreCase("INTERMEDIATE1")) {
            dsa_level = "INTERMEDIATE2";
        } else if (currentLevel.equalsIgnoreCase("INTERMEDIATE2")) {
            dsa_level = "INTERMEDIATE3";
        } else if (currentLevel.equalsIgnoreCase("INTERMEDIATE3")) {
            dsa_level = "MASTER";
        } else if (currentLevel.equalsIgnoreCase("MASTER")) {
            dsa_level = "MASTER";
        } else {
            throw new IllegalArgumentException(
                    "Unknown DSA level: " + currentLevel);
        }

        String sql = "UPDATE users SET dsa_level=? where username=?";
        try (Connection conn = getConnection()) {
            if (conn == null) {
                throw new SQLException("Database Connectivity Failed");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, dsa_level);
                ps.setString(2, user.getUsername());
                ps.executeUpdate();
            }
        }
    }

    public static String fetchDsaLevel(User user) throws SQLException {
        String sql = "select dsa_level from users where username=?";
        try (Connection conn = getConnection()) {
            if (conn == null) {
                throw new SQLException("Database Connectivity Failed");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, user.getUsername());

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("dsa_level");
                    }

                    throw new SQLException("User not found: " + user.getUsername());
                }
            }
        }
    }

}
