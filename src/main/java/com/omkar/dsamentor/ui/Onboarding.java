package com.omkar.dsamentor.ui;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.omkar.dsamentor.model.User;
import com.omkar.dsamentor.repository.UserRepository;

public class Onboarding {

    public static void onBoarding(User user,Scanner sc) {
        int choice;
        while (true) {
            try {
                System.out.println();
                System.out.println();
                System.out.println("====================================================");
                System.out.println("       WELLCOM TO YOUR DSA PREPERATION JOURNY       ");
                System.out.println("====Complete onboarding for knowing your status=====");
                System.out.println("=====================================================");
                System.out.println();
                System.out.println("What's Your Current DSA level");
                System.out.println("1. Not even know about DSA");
                System.out.println("2. Begineer");
                System.out.println("3. Intermediate");
                System.out.println("4. Master,using platform for prcticing DSA more");
                System.out.println();
                System.out.println("Select Option From 1 to 4");

                choice = sc.nextInt();
                while (choice < 1 || choice > 4) {
                    System.out.println("You Entered Wrong Choice");
                    System.out.print("Enter choice from 1 to 4:");
                    choice = sc.nextInt();
                }
                break;
            } catch (InputMismatchException a) {
                System.out.println("OOPS !! YOU ENTERED WRONG CHOICE");
                sc.nextLine();
            }
        }

        String level;
        if (choice == 1) {
            level = "NEW";
        } else if (choice == 2) {
            level = "BEGINEER";
        } else if (choice == 3) {
            level = "INTERMEDIATE";
        } else {
            level = "MASTER";
        }

        System.out.println();
        int choose;
        while (true) {
            try {
                System.out.println("Which Best Describs You:");
                System.out.println("1. Student");
                System.out.println("2. Working Professional/Experienced");
                System.out.println();
                System.out.println("Select either 1 or 2:");
                choose = sc.nextInt();
                while (choose < 1 || choose > 2) {
                    System.out.println("OOPS !! YOU ENTERED WRONG CHOICE");
                    System.out.print("Please Enter Correct Choice(1 or 2):");
                    choose = sc.nextInt();
                }
                sc.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("OOPS !! YOU ENTERED WRONG CHOICE");
                sc.nextLine();
            }
        }


        String desc;
        if (choose == 1) {
            desc = "STUDENT";
        } else {
            desc = "EXPERIENCED";
        }

        user.setDsaLevel(level);
        user.setUserType(desc);

        //status of Onboarding get updated now/Onboarding =true,means completed
        try{
            UserRepository.UpdateUserOnboarding(user);
            System.out.println();
            System.out.println("===!!   ONBOARDING COMPLETED SUCCSEFULLY   !!===");

            System.out.println("Press Enter for Moving onto MainMenu");
            sc.nextLine();
            //we will display the main menu now.
            MainMenu.mainMenu(user,sc);
        }catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }

    }
}
