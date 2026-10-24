package com.omkar.dsamentor.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class UserProblemProgressRepository {
    public static void addProbIdAlongWithUserName(String username,int problemId) throws SQLException
    {
        String sql="INSERT INTO user_problem_progress(username,problem_id,solved_at) VALUES(?,?,CURRENT_TIMESTAMP)";
        try(Connection conn=UserRepository.getConnection())
        {
            if (conn == null) {
                throw new SQLException("Database Connectivity Failed");
            }

            try(PreparedStatement ps=conn.prepareStatement(sql)) {
                ps.setString(1,username);
                ps.setInt(2,problemId);
                ps.executeUpdate();
            }
        }
    }

    public static HashSet<Integer> getProblemIdForCheckUnsolvedProblemsByUser(String username) throws SQLException {
        String sql = "SELECT problem_id FROM user_problem_progress WHERE username = ?";
        HashSet<Integer> probIDs = new HashSet<>();
        try (Connection conn = UserRepository.getConnection()) {
            if (conn == null) {
                throw new SQLException("Database Connectivity Failed");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int probID = rs.getInt("problem_id");
                        probIDs.add(probID);
                    }
                }
            }
        }

        return probIDs;
    }
}
