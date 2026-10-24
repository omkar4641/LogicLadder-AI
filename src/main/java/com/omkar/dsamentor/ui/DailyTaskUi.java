package com.omkar.dsamentor.ui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.omkar.dsamentor.model.DSAProblem;
import com.omkar.dsamentor.model.User;
import com.omkar.dsamentor.service.AiDsaMentorService;
import com.omkar.dsamentor.service.AiDsaMentorService.AssistanceOption;
import com.omkar.dsamentor.service.DailyTaskService;

public class DailyTaskUi {

    public static void displayDailyTasks(User user, Scanner sc) throws SQLException {
        char z;
        do{
        // Fetch today's tasks from the service layer
        List<DSAProblem> dailyTasks = DailyTaskService.dailyTasks(user);

        int dailyLimit = user.getDailyStudyMin();
        int totalMinutes = 0;

        for (DSAProblem problem : dailyTasks) {
            totalMinutes += problem.getEstTime(); // gives the total estimated time for solving toays task ex:problem1
                                                  // requires 20mins/problem2 requires30mins/problem3 requires40mins
                                                  // than totalMinutes becomes=20+30+40=90minutes
        }

        System.out.println();
        System.out.println("==============================================================");
        System.out.println("                    TODAY'S DSA TASKS");
        System.out.println("==============================================================");

        System.out.println("DSA Level        : " + user.getDsaLevel());
        System.out.println("Daily Study Limit: " + dailyLimit + " minutes");
        System.out.println("Questions Today  : " + dailyTasks.size());
        System.out.println("Planned Study    : " + totalMinutes + " minutes Qustion Solving Time");

        System.out.println("==============================================================");

        // Handle cases where no tasks are available
        if (dailyTasks.isEmpty()) {
            System.out.println();
            System.out.println("No DSA tasks are available for today.");
            System.out.println(
                    "You may have completed all eligible questions, " +
                            "or no questions fit your current selection rules.");
            System.out.println();
            return;
        }

        // Display the questions in tabular form
        printTableBorder();

        System.out.printf(
                "| %-3s | %-5s | %-32s | %-16s | %-5s | %-10s | %4s | %-12s |%n",
                "No.",
                "ID",
                "Title",
                "Topic",
                "Stage",
                "Difficulty",
                "Min",
                "Platform");

        printTableBorder();

        for (int i = 0; i < dailyTasks.size(); i++) {

            DSAProblem problem = dailyTasks.get(i);

            System.out.printf(
                    "| %-3d | %-5d | %-32.32s | %-16.16s | %-5d | %-10.10s | %4d | %-12.12s |%n",
                    i + 1,
                    problem.getProblemID(),
                    displayValue(problem.getTitle()),
                    displayValue(problem.getTopic()),
                    problem.getlearningStage(),
                    displayValue(problem.getDifficulty()),
                    problem.getEstTime(),
                    displayValue(problem.getPlatform()));
        }

        printTableBorder();

        // ==========================================
        // SECTION 1: PROBLEM LINKS
        // ==========================================
        System.out.println();
        System.out.println("============== PROBLEM LINKS ==============");

        for (int i = 0; i < dailyTasks.size(); i++) {

            DSAProblem problem = dailyTasks.get(i);

            System.out.println();
            System.out.println((i + 1) + ". " + problem.getTitle()
                    + " (ID: " + problem.getProblemID() + ")");

            String problemURL = problem.getProURL();

            if (problemURL != null && !problemURL.isBlank()) {
                System.out.println("   Problem URL: " + problemURL);
            } else {
                System.out.println("   Problem URL: Not available");
            }
        }

        // ==========================================
        // SECTION 2: LEARNING RESOURCE LINKS
        // ==========================================
        System.out.println();
        System.out.println("========== LEARNING RESOURCE LINKS ==========");

        for (int i = 0; i < dailyTasks.size(); i++) {

            DSAProblem problem = dailyTasks.get(i);

            System.out.println();
            System.out.println((i + 1) + ". " + problem.getTitle()
                    + " (ID: " + problem.getProblemID() + ")");

            String resourceURL = problem.getResURL();

            if (resourceURL != null && !resourceURL.isBlank()) {
                System.out.println("   Resource URL: " + resourceURL);
            } else {
                System.out.println("   Resource URL: Not available");
            }
        }

        System.out.println();
        System.out.println("=============================================");

        System.out.println();
        System.out.println("==============================================================");
        System.out.println("                 END OF TODAY'S TASKS");
        System.out.println("==============================================================");
        System.out.println();

        System.out.println();
        System.out.println();
        System.out.println("1. Mark Solevd Questions");
        System.out.println("2. AI Assist");
        System.out.println("3. Exit");
        System.out.println();
        int choice;

        while (true) {
            try {
                System.out.print("Enter your choice:");
                choice = sc.nextInt();
                while(choice<1 || choice>3)
                {
                    System.out.print("Enter your valid choice from 1 to 3:");
                    choice = sc.nextInt();
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Enter Choice from 1 to 3");
                sc.nextLine();
            }
        }
        sc.nextLine();
        System.out.println();

        switch (choice) {
            case 1:
                DailyTaskService.updateProblemId(sc, dailyTasks, user);
                break;

            case 2:
                showAiAssistance(user, dailyTasks, sc);
                break;

            case 3:
                return;
        }
        System.out.println("Press y to return to Today's DSA Tasks. ");
        z=sc.next().charAt(0);
    }while(z=='Y' || z=='y');

    }

    // ======================================================================================================================
    // Prints the border of the task table
    private static void printTableBorder() {

        System.out.println(
                "+" + "-".repeat(5) +
                        "+" + "-".repeat(7) +
                        "+" + "-".repeat(34) +
                        "+" + "-".repeat(18) +
                        "+" + "-".repeat(7) +
                        "+" + "-".repeat(12) +
                        "+" + "-".repeat(6) +
                        "+" + "-".repeat(14) +
                        "+");
    }

    // Prevents null or blank values from appearing in the output
    private static String displayValue(String value) {

        if (value == null || value.isBlank()) {
            return "-";
        }

        return value;
    }

    // =================AI dsa question explained and approach explained=========
    private static void showAiAssistance(User user, List<DSAProblem> dailyTasks, Scanner sc) {

        while (true) {

            System.out.println();
            System.out.println("============== AI DSA MENTOR ==============");
            System.out.println("Select a question from today's tasks:");
            System.out.println();

            for (DSAProblem problem : dailyTasks) {
                System.out.printf(
                        "ID: %d | %s | %s%n",
                        problem.getProblemID(),
                        problem.getTitle(),
                        problem.getPlatform());
            }

            System.out.println();
            System.out.println("0. Return to Today's DSA Tasks");
            System.out.print("Enter Problem ID: ");

            String input = sc.nextLine().trim();

            if ("0".equals(input)) {
                return;
            }

            int problemId;

            try {
                problemId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(
                        "Please enter a valid numeric Problem ID.");
                continue;
            }

            DSAProblem selectedProblem = null;

            for (DSAProblem problem : dailyTasks) {
                if (problem.getProblemID() == problemId) {
                    selectedProblem = problem;
                    break;
                }
            }

            if (selectedProblem == null) {
                System.out.println(
                        "That Problem ID is not in today's task list.");
                continue;
            }

            while (true) {

                System.out.println();
                System.out.println("============================================");
                System.out.println("             AI ASSISTANCE");
                System.out.println("============================================");
                System.out.println(
                        "Problem: " + selectedProblem.getTitle());
                System.out.println(
                        "Platform: " + selectedProblem.getPlatform());
                System.out.println(
                        "DSA Level: " + user.getDsaLevel());

                System.out.println();
                System.out.println("1. Explain Question and Dry Run an Example");
                System.out.println("2. Brute Force");
                System.out.println("3. Optimal Approach");
                System.out.println("4. Return to Today's DSA Tasks");
                System.out.print("Choose an option: ");

                String assistanceChoice = sc.nextLine().trim();

                AssistanceOption option;

                switch (assistanceChoice) {

                    case "1":
                        option = AssistanceOption.EXPLAIN_AND_DRY_RUN;
                        break;

                    case "2":
                        option = AssistanceOption.BRUTE_FORCE_APPROACH;
                        break;

                    case "3":
                        option = AssistanceOption.OPTIMAL_APPROACH;
                        break;

                    case "4":
                        return;

                    default:
                        System.out.println("Please choose a valid option.");
                        continue;
                }

                System.out.println();
                System.out.println("Generating AI assistance...");
                System.out.println("Please wait.");
                System.out.println();

                try {
                    String response = AiDsaMentorService.getAssistance(user,selectedProblem,option);

                    System.out.println();
                    System.out.println("============== AI EXPLANATION ==============");
                    System.out.println(response);
                    System.out.println("============================================");

                } catch (InterruptedException e) {

                    Thread.currentThread().interrupt();

                    System.out.println("The AI request was interrupted.");

                } catch (IOException | IllegalArgumentException e) {

                    System.out.println("Unable to generate AI assistance: " + e.getMessage());
                }

                System.out.println();
                return;
            }
        }
    }

}