package com.omkar.dsamentor.ui;

import java.sql.SQLException;
import java.util.Scanner;
import com.omkar.dsamentor.model.User;
import com.omkar.dsamentor.repository.UserRepository;
import com.omkar.dsamentor.util.PasswordUtil;

public class CreateAC {

    public static void createAcount(Scanner sc) {
        
        try {
            System.out.println("================================");
            System.out.println("       CREATE YOUR ACOUNT       ");
            System.out.println("===== Start Your DSA Journy ====");
            System.out.println("================================");
            System.out.println();

            System.out.print("Enter your name:");
            String name = sc.nextLine();
            System.out.println();

            while (name.isBlank()) {
                System.out.println("===!! Name is not entered !!===");
                System.out.print("Enter your name again:");
                name = sc.nextLine();
            }
            System.out.println();

            System.out.print("Enter your email:");
            String email = sc.nextLine();
            while (email.isBlank()) {
                System.out.println("===!! Email is not entered !!===");
                System.out.print("Enter your email again:");
                email = sc.nextLine();
            }
            System.out.println();

            System.out.print("Enter User_Name:");
            String username = sc.nextLine();
            while (username.isBlank()) {
                System.out.println("===!! User_Name is not entered !!===");
                System.out.print("Enter your user again:");
                username = sc.nextLine();
            }

            while (UserRepository.FindPassHash(username) != null) {
                System.out.println("========!! User_Name already Exists !!========");
                System.out.print("Enter Unique User_Name again:");
                username = sc.nextLine();
            }
            System.out.println();

            System.out.print("Enter password:");
            String pass = sc.nextLine();
            while (pass.isBlank()) {
                System.out.println("===!! Password is not entered !!===");
                System.out.print("Enter password again:");
                pass = sc.nextLine();
            }

            // password hashing through argon2id algorithm
            String passHash = PasswordUtil.hashPassword(pass);

            User user = new User(name, email, username, passHash);

            try {
                UserRepository.insertUser(user);
                System.out.println();
                System.out.println("===!!   ACCOUNT CREATED SUCCESFULLY   !!===");
            } catch (SQLException e) {
                System.out.println("Something went wrong during account creation");
                System.out.println(e.getMessage());
                return;
            }
            System.out.println();
            System.out.println("================================================");
            System.out.println("1. Complete Onboarding");
            System.out.println("2. EXIT");
            System.out.println("================================================");
            System.out.print("Select Option:");
            int Choose=sc.nextInt();
            while(Choose<1 || Choose>2)
            {
                System.out.println("Choose either 1 or 2 option");
                Choose=sc.nextInt();
            }
            sc.nextLine(); // Consume leftover newline

            switch(Choose)
            {
                case 1:
                    Onboarding.onBoarding(user,sc);
                    break;

                case 2:
                    InitialMenu.initialMenu(sc);
                    break;
            }

        } catch (SQLException e) {
        System.out.println("Something went wrong during account creation");
        System.out.println(e.getMessage());
        return;
    }
    }
}
