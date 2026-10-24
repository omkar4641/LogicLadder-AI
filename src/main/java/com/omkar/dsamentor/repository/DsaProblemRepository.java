package com.omkar.dsamentor.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.omkar.dsamentor.model.DSAProblem;

public class DsaProblemRepository {

    public static List<DSAProblem> getEligibleProblems(String user_dsaLevel) throws SQLException {

        if (user_dsaLevel == null || user_dsaLevel.isBlank()) {
            throw new IllegalArgumentException("DSA level cannot be empty.");
        }

        List<DSAProblem> problems = new ArrayList<>();

        String sql = """
                SELECT
                    dp.problem_id,
                    dp.title,
                    dp.topic,
                    dp.pattern,
                    dp.learning_stage,
                    dp.difficulty,
                    dp.estimated_minutes,
                    dp.platform,
                    dp.problem_url,
                    dp.resource_url,
                    dp.is_active,
                    dp.problem_style
                FROM dsa_problems AS dp
                CROSS JOIN (
                    SELECT UPPER(TRIM(?)) AS user_level
                ) AS u
                WHERE dp.is_active = 1
                AND (
                    (
                        u.user_level IN ('NEW', 'BEGINEER')
                        AND dp.learning_stage = 0
                        AND dp.topic = 'Logic Building'
                        AND dp.difficulty IN ('EASY')
                        AND dp.problem_style IN ('STRAIGHTFORWARD')
                    )

                    OR

                    (
                        u.user_level = 'BEGINEER1'
                        AND dp.learning_stage BETWEEN 1 AND 4
                        AND dp.difficulty IN ('EASY', 'MEDIUM','HARD')
                        AND dp.problem_style IN ('STRAIGHTFORWARD', 'TRICKY')
                    )

                    OR

                    (
                        u.user_level IN ('INTERMEDIATE', 'INTERMEDIATE1')
                        AND dp.learning_stage BETWEEN 1 AND 8
                        AND dp.topic = 'Array'
                        AND dp.difficulty IN ('EASY', 'MEDIUM', 'HARD')
                        AND dp.problem_style IN ('STRAIGHTFORWARD', 'TRICKY')
                    )

                    OR

                    (
                        u.user_level = 'INTERMEDIATE2'
                        AND dp.learning_stage BETWEEN 1 AND 8
                        AND dp.topic = 'String'
                        AND dp.difficulty IN ('EASY', 'MEDIUM', 'HARD')
                        AND dp.problem_style IN ('STRAIGHTFORWARD', 'TRICKY')
                    )

                    OR

                    (
                        u.user_level = 'INTERMEDIATE3'
                        AND dp.learning_stage BETWEEN 1 AND 8
                        AND dp.topic IN ('Linked List', 'Intervals')
                        AND dp.difficulty IN ('EASY', 'MEDIUM', 'HARD')
                        AND dp.problem_style IN ('STRAIGHTFORWARD', 'TRICKY')
                    )

                    OR

                    (
                        u.user_level = 'MASTER'
                        AND dp.learning_stage BETWEEN 1 AND 12
                        AND dp.topic IN (
                            'Array',
                            'String',
                            'Linked List',
                            'Math',
                            'Intervals',
                            'Heap',
                            'Recursion',
                            'Backtracking',
                            'Tree',
                            'Graph',
                            'Dynamic Programming',
                            'Greedy'
                        )
                        AND (
                            dp.difficulty IN ('MEDIUM', 'HARD')
                            OR (
                                dp.difficulty = 'EASY'
                                AND dp.problem_style = 'TRICKY'
                            )
                        )
                    )
                )
                ORDER BY dp.problem_id
                """;

        try (Connection conn = UserRepository.getConnection()) {

            if (conn == null) {
                throw new SQLException("Database Connectivity Failed");
            }

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, user_dsaLevel);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {

                        int prob_id = rs.getInt("problem_id");
                        String title = rs.getString("title");
                        String Topic = rs.getString("topic");
                        String pattern = rs.getString("pattern");
                        int learning_stage = rs.getInt("learning_stage");
                        String difficulty = rs.getString("difficulty");
                        int est_min = rs.getInt("estimated_minutes");
                        String platform = rs.getString("platform");
                        String prob_url = rs.getString("problem_url");
                        String res_url = rs.getString("resource_url");
                        boolean is_active = rs.getBoolean("is_active");
                        String prob_style = rs.getString("problem_style");

                        DSAProblem problem = new DSAProblem(
                                prob_id,
                                title,
                                Topic,
                                pattern,
                                difficulty,
                                est_min,
                                platform,
                                prob_url,
                                res_url,
                                learning_stage,
                                is_active,
                                prob_style
                        );

                        problems.add(problem);
                    }
                }
            }
        }

        return problems;
    }


    public static String getDifficulty(int problemId) throws SQLException
    {
        String sql="select difficulty from dsa_problems where problem_id=?";
        try(Connection conn=UserRepository.getConnection())
        {
            if(conn==null)
            {
                throw new SQLException("Database Connectivity Failed");
            }

            try(PreparedStatement ps=conn.prepareStatement(sql))
            {
                ps.setInt(1,problemId);

                try(ResultSet rs=ps.executeQuery())
                {
                    if(rs.next())
                    {
                        return rs.getString("difficulty");
                    }
                }
            }
        }
        return null;
    }
}