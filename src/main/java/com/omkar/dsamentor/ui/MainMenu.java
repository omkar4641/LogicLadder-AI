package com.omkar.dsamentor.ui;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;
import com.omkar.dsamentor.model.User;

public class MainMenu {
    public static void mainMenu(User user, Scanner sc) throws SQLException {
        int choice;
        char z;

        do {
            System.out.println();
            System.out.println("===============================================");
            System.out.println("             WELCOME TO MAIN_MENU              ");
            System.out.println("================== " + user.getUsername() + " ===================");
            System.out.println("================================================");
            System.out.println();
            while (true) {
                try {
                    System.out.println("1. View My progress");
                    System.out.println("2. Today's DSA Tasks");
                    System.out.println("3. AI CONTEST");
                    System.out.println("4. Set Daily Study Hours For DSA");
                    System.out.println("5. Logout");

                    System.out.println();
                    System.out.print("Enter Your Choice:");
                    choice = sc.nextInt();
                    System.out.println();
                    while (choice < 1 || choice > 5) {
                        System.out.println("===============!! WRONG INPUT !!===================");
                        System.out.print("Enter Choice from 1 to 8:");
                        choice = sc.nextInt();
                        System.out.println();
                    }
                    sc.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.print("enter integer choice from 1 to 8:");
                    System.out.println();
                    sc.nextLine();
                }
            }

            switch (choice) {
                case 1:
                    ProgressUi.displayProgress(user);
                    ;
                    break;

                case 2:
                    DailyTaskUi.displayDailyTasks(user, sc);
                    break;

                case 3:
                    AiContestUi.displayContest(user, sc);
                    break;

                case 4:
                    WorkingTime.updateDailyStudyTime(user, sc);
                    break;

                case 5:
                    System.out.println("====!! LOGGED OUT SUCCESFULLY !!====");
                    return;
            }
            System.out.println();
            System.out.println();
            System.out.print("PRESS 'Y' FOR RE_DIRECTING TO MENU:");
            z = sc.next().charAt(0);
            z = sc.next().charAt(0);

            while (Character.toLowerCase(z) != 'y') {
                System.out.print("PRESS 'Y' FOR REDIRECTING TO MENU: ");
                z = sc.next().charAt(0);
            }

            System.out.println();
            System.out.println();

        } while (z == 'Y' || z == 'y');
    }
}
