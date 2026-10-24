package com.omkar.dsamentor.ui;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.omkar.dsamentor.model.User;
import com.omkar.dsamentor.repository.UserRepository;

public class WorkingTime {
    public static int dailyWorkingTime(Scanner sc) {
        System.out.println();
        while (true) {
            try {
                System.out
                        .println("=============================== !! INSTRUCTION !!=================================");
                System.out
                        .println("Set the study capacity in minutes and don't add any 'min' unit at end like 120min ");
                System.out
                        .println("==================================================================================");
                System.out.println();
                System.out.print("Set Your Daily Study Capacity:");
                int minu = sc.nextInt();
                while (minu < 20 || minu > 360) {
                    System.out.println("Don't study DSA Beyond of 6 hours || Set atleast 21mins at Start");
                    System.out.print("Set Your Daily Study Capacity:");
                    minu = sc.nextInt();
                }

                return minu;
            } catch (InputMismatchException e) {
                System.out.println("Enter Study Capacity in Minutes");
                sc.nextLine();
            }
        }

    }

    public static void updateDailyStudyTime(User user, Scanner sc) {
        try {
            int minutes = dailyWorkingTime(sc);

            UserRepository.updateDailyStudyMin(user.getUsername(),minutes);

            // Update the current logged-in User object.
            user.setDailyStudyMin(minutes);

            System.out.println(
                    "Your daily DSA study time has been updated to "
                            + minutes + " minutes successfully!");

        } catch (SQLException e) {
            System.out.println(
                    "Unable to save study time: " + e.getMessage());
        }
    }

}
