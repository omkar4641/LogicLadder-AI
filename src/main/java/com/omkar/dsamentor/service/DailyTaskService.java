package com.omkar.dsamentor.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import com.omkar.dsamentor.model.DSAProblem;
import com.omkar.dsamentor.model.User;
import com.omkar.dsamentor.repository.DsaProblemRepository;
import com.omkar.dsamentor.repository.UserProblemProgressRepository;
import com.omkar.dsamentor.repository.UserRepository;

public class DailyTaskService {
    public static List<DSAProblem> dailyTasks(User user) throws SQLException {
        String user_DsaLevel = user.getDsaLevel();
        List<DSAProblem> problemList = DsaProblemRepository.getEligibleProblems(user_DsaLevel);

        String username = user.getUsername();

        // This hashset is fieled with the problem id of questions thous the user have
        // already solved
        HashSet<Integer> prob_Ids = UserProblemProgressRepository.getProblemIdForCheckUnsolvedProblemsByUser(username);

        // ArrayList for getting unsolved problem list from problemList.
        List<DSAProblem> unsolvedProblems = new ArrayList<>();



        for (int i = 0; i < problemList.size(); i++) {
            DSAProblem problem = problemList.get(i);
            if (!prob_Ids.contains(problem.getProblemID())) {
                unsolvedProblems.add(problem);
            }
        }




        if (!problemList.isEmpty() && unsolvedProblems.isEmpty()) {
            // MASTER has no higher level
            if (user.getDsaLevel().equalsIgnoreCase("MASTER")) {
                System.out.println();
                System.out.println("==============================================================");
                System.out.println("                  || CONGRATULATIONS ||");
                System.out.println("       YOU HAVE COMPLETED ALL REQUIRED DSA QUESTIONS");
                System.out.println("==============================================================");
                return new ArrayList<>();
            }

            UserRepository.promoteUser(user);

            user.setDsaLevel(UserRepository.fetchDsaLevel(user));

            return dailyTasks(user);
        }



        int user_daily_studyMin = user.getDailyStudyMin();

        // finalized list to display the user,his/her daily task.
        List<DSAProblem> FinalQuestionList = new ArrayList<>();

        for (int i = 0; i < unsolvedProblems.size(); i++) {
            DSAProblem problem = unsolvedProblems.get(i);
            if (problem.getEstTime() < user_daily_studyMin + 1) {
                FinalQuestionList.add(problem);
                user_daily_studyMin -= problem.getEstTime();
            }
        }

        return FinalQuestionList;
    }

    public static void updateProblemId(Scanner sc,List<DSAProblem> dailyTasks,User user) throws SQLException
    {
        // problem id input from user to update user_problem_progress table
        System.out.println();
        System.out.print("Enter the problem IDs(separated by commas) you solved today:");
        String input = sc.nextLine().trim();

        // Handle blank input BEFORE converting IDs
        if (input.isBlank()) {
            System.out.println();
            System.out.println("==============================================================");
            System.out.println("       YOU DIDN'T SOLVE ANY QUESTIONS TODAY");
            System.out.println("       Continue your practice tomorrow!");
            System.out.println("==============================================================");
            return;
        }

        HashSet<Integer> solvedProblemIds = new HashSet<>();

        String[] ids = input.split(",");

        for (String id : ids) {
        try {
            solvedProblemIds.add(Integer.parseInt(id.trim()));
        } catch (NumberFormatException e) {
        // Ignore invalid input and continue with the next ID
        }
    }

        if (solvedProblemIds.isEmpty()) {
            System.out.println();
            System.out.println("==============================================================");
            System.out.println("       YOU DIDN'T SOLVE ANY QUESTIONS TODAY");
            System.out.println("       Continue your practice tomorrow!");
            System.out.println("==============================================================");
            return;
        }

        for (DSAProblem problem : dailyTasks) {
            if (solvedProblemIds.contains(problem.getProblemID())) {
                UserProblemProgressRepository.addProbIdAlongWithUserName(user.getUsername(), problem.getProblemID());
            }
        }
        System.out.println();
        System.out.println("PRGORESS UPDATED SUCCESFULLY");
        System.out.println();
    }
}
