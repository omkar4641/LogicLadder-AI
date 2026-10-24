package com.omkar.dsamentor.ui;

import java.sql.SQLException;

import com.omkar.dsamentor.model.ProgressReport;
import com.omkar.dsamentor.model.User;
import com.omkar.dsamentor.service.ProgressService;

public class ProgressUi {

        private static final String RESET = "\u001B[0m";
        private static final String CYAN = "\u001B[96m";
        private static final String BLUE = "\u001B[94m";
        private static final String GRAY = "\u001B[90m";
        private static final String WHITE = "\u001B[97m";
        private static final String BOLD = "\u001B[1m";

        private static final int BAR_WIDTH = 30;

        // Hardcoded totals for Layer 2
        private static final int TOTAL_EASY = 63;
        private static final int TOTAL_MEDIUM = 113;
        private static final int TOTAL_HARD = 23;

        public static void displayProgress(User user) throws SQLException {

                //object of ProgressReport of perticular user
                ProgressReport report = ProgressService.userProgress(user);

                System.out.println();
                System.out.println();
                System.out.println(CYAN + BOLD + "=".repeat(48) + RESET);
                System.out.printf("%31s%n", "MY DSA PROGRESS");
                System.out.println(CYAN + BOLD + "=".repeat(48) + RESET);

                // ==========================================
                // LAYER 1: CURRENT DSA LEVEL PROGRESS
                // ==========================================

                String level = report.getCurrentDsaLevel();

                int total = report.getTotalEligiableQuestionCount();
                int solved = report.getTotalSolvedQuestionCount();
                int percentage = report.getCompletionPercentage();

                int remaining = total - solved;

                System.out.println();
                System.out.println(CYAN + BOLD
                                + "LAYER 1: CURRENT LEVEL PROGRESS" + RESET);

                System.out.println(WHITE + BOLD
                                + "Current Level: " + level + RESET);

                System.out.println(GRAY
                                + "Your progress at the current DSA level."
                                + RESET);

                System.out.println();

                System.out.printf("%-27s %d / %d%n",
                                "Questions Solved:", solved, total);

                System.out.printf("%-27s %d%n",
                                "Questions Remaining:", remaining);

                System.out.printf("%-27s %d%%%n",
                                "Level Completion:", percentage);

                System.out.println();

                System.out.println(WHITE + BOLD
                                + "LEVEL COMPLETION" + RESET);

                if (total == 0) {
                        System.out.println(GRAY
                                        + "No eligible questions available for this level."
                                        + RESET);
                } else {
                        printProgressBar(percentage);
                }

                System.out.println();
                System.out.println();
                System.out.println(CYAN + "-".repeat(48) + RESET);

                // ==========================================
                // LAYER 2: DIFFICULTY-WISE PROGRESS
                // ==========================================

                System.out.println();
                System.out.println();
                System.out.println(CYAN + BOLD
                                + "LAYER 2: DIFFICULTY-WISE PROGRESS" + RESET);

                System.out.println(GRAY
                                + "Progress across the entire active question bank."
                                + RESET);

                System.out.println();

                int easySolved = report.getEasyQuestionCount();
                int mediumSolved = report.getMediumQuestionCount();
                int hardSolved = report.getHardQuestionCount();

                printDifficultyProgress(
                                "EASY", easySolved, TOTAL_EASY);

                printDifficultyProgress(
                                "MEDIUM", mediumSolved, TOTAL_MEDIUM);

                printDifficultyProgress(
                                "HARD", hardSolved, TOTAL_HARD);

                System.out.println();
                System.out.println(CYAN + "=".repeat(48) + RESET);
                System.out.println();
                System.out.println();
        }

        // Displays one difficulty's statistics and progress bar
        private static void printDifficultyProgress(
                        String difficulty, int solved, int total) {

                int percentage = 0;

                if (total > 0) {
                        percentage = (int) (solved * 100.0 / total);
                }

                percentage = Math.max(0, Math.min(100, percentage));

                System.out.println(WHITE + BOLD
                                + difficulty + " PROGRESS" + RESET);

                System.out.printf("%-20s %d / %d (%d%%)%n",
                                "Questions Solved:", solved, total, percentage);

                printProgressBar(percentage);

                System.out.println();
        }

        // Draws a blue progress bar in the console
        private static void printProgressBar(int percentage) {

                percentage = Math.max(0, Math.min(100, percentage));

                int filledBlocks = percentage * BAR_WIDTH / 100;
                int emptyBlocks = BAR_WIDTH - filledBlocks;

                System.out.print(BLUE);
                System.out.print("█".repeat(filledBlocks));

                System.out.print(GRAY);
                System.out.print("░".repeat(emptyBlocks));

                System.out.println(RESET + " " + percentage + "%");
        }
}