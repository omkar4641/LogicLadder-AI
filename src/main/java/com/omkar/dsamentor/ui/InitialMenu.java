package com.omkar.dsamentor.ui;

import java.util.Scanner;

public class InitialMenu {

    // Professional terminal color palette
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[96m";
    private static final String BLUE = "\u001B[94m";
    private static final String WHITE = "\u001B[97m";
    private static final String GRAY = "\u001B[90m";
    private static final String YELLOW = "\u001B[93m";
    private static final String BOLD = "\u001B[1m";

    private static final int WIDTH = 52;

    public static int initialMenu(Scanner sc) {

        while (true) {

            System.out.println();
            System.out.println();
            System.out.println();

            // Brand banner
            printBorder();

            printBannerLine(
                    "DSA MENTOR",
                    CYAN + BOLD
            );

            printBannerLine(
                    "YOUR PERSONAL DSA LEARNING PLATFORM",
                    BLUE
            );

            printBorder();

            System.out.println();

            System.out.println(
                    WHITE + "  Welcome! Build your DSA skills one problem at a time."
                            + RESET
            );

            System.out.println();

            // Main menu
            printBorder();

            printBannerLine(
                    "MAIN MENU",
                    WHITE + BOLD
            );

            printBorder();

            printMenuOption("1", "Create Account");
            printMenuOption("2", "Login");
            printMenuOption("3", "Exit");

            printBorder();

            System.out.println();

            // User input
            System.out.print(
                    CYAN + BOLD + "  Enter your choice "
                            + RESET
                            + GRAY + "(1-3): " + RESET
            );

            String input = sc.nextLine().trim();

            int choice;

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(
                        YELLOW + "  Please enter a valid number."
                                + RESET
                );
                continue;
            }

            if (choice < 1 || choice > 3) {
                System.out.println();
                System.out.println(
                        YELLOW + "  Select an option between 1 and 3."
                                + RESET
                );
                continue;
            }

            System.out.println();

            if (choice == 1) {
                System.out.println(
                        CYAN + "  Opening account registration..."
                                + RESET
                );
            } else if (choice == 2) {
                System.out.println(
                        BLUE + "  Opening login..."
                                + RESET
                );
            } else {
                System.out.println(
                        GRAY + "  Thank you for using DSA Mentor. Goodbye!"
                                + RESET
                );
            }

            return choice;
        }
    }

    // Draws the banner and menu borders
    private static void printBorder() {
        System.out.println(
                CYAN + "+" + "-".repeat(WIDTH) + "+" + RESET
        );
    }

    // Centers text inside the fixed-width banner
    private static void printBannerLine(
            String text, String color) {

        int leftPadding = (WIDTH - text.length()) / 2;
        int rightPadding = WIDTH - text.length() - leftPadding;

        String centeredText =
                " ".repeat(leftPadding)
                        + text
                        + " ".repeat(rightPadding);

        System.out.println(
                CYAN + "|" + RESET
                        + color + centeredText + RESET
                        + CYAN + "|" + RESET
        );
    }

    // Prints consistently aligned menu options
    private static void printMenuOption(
            String number, String label) {

        String spaces = String.format("%-44s", label);

        System.out.println(
                GRAY + "|" + RESET
                        + "   "
                        + BLUE + "[" + number + "]" + RESET
                        + "  "
                        + WHITE + spaces + RESET
                        + GRAY + "|" + RESET
        );
    }
}