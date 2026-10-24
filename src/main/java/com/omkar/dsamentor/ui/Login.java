package com.omkar.dsamentor.ui;

import java.sql.SQLException;
import java.util.Scanner;

import com.omkar.dsamentor.model.User;
import com.omkar.dsamentor.repository.UserRepository;
import com.omkar.dsamentor.util.PasswordUtil;

public class Login {
    
    public static void loginIn(Scanner sc)
    {
        try{
        System.out.println("=======================================");
        System.out.println("       CONTINUE YOUR DSA JOURNY        ");
        System.out.println("========== Login Your Acount ==========");
        System.out.println("========================================");
        System.out.println();

        System.out.print("Enter your User_Name:");
        String username=sc.nextLine();
        System.out.println();


        System.out.print("Enter your password:");
        String pass=sc.nextLine();
        System.out.println();


        String storedHash=UserRepository.FindPassHash(username);

        //verifying whether User_Name exists in DB or not.
        while(storedHash==null)
        {
            System.out.println("========================================");
            System.out.println("            USER NOT FOUND !!           ");
            System.out.println("===== Login With Valid Credentials =====");
            System.out.println("========================================");
            System.out.println();

            System.out.print("Enter your User_Name:");
            username=sc.nextLine();
            System.out.println();


            System.out.print("Enter your password:");
            pass=sc.nextLine();
            System.out.println();

            storedHash=UserRepository.FindPassHash(username);
        }

        
        while(!PasswordUtil.verifyPassword(pass, storedHash)){
            System.out.println("===!! WRONG PASSWORD !!===");
            System.out.print("Enter Valid Password:");
            pass=sc.nextLine();
            System.out.println();
        }

            System.out.println();
            System.out.println();
            System.out.println("===ACCOUNT LOGIN SUCCESSFUL===");
            //====================================================================
            User rebuiltuser=UserRepository.retrievUserObject(username);

            if (rebuiltuser == null) {
            System.out.println("Unable to retrieve user details.");
            return;
            }



            if(!rebuiltuser.isOnBoardingCompleted())
            {
                System.out.println();
                System.out.println("===========================================");
                System.out.println("        YOU ARE NEW TO OUR PLATFORM        ");
                System.out.println("====Please Complete OnBoarding Processs====");
                System.out.println("===========================================");
                System.out.println();
                System.out.print("Press Enter For Completing OnBoarding Process");
                sc.nextLine();//When the user presses Enter, the program continues to the next statement.

                //we are recreating the user from "username" that the user used while login the acount
                Onboarding.onBoarding(rebuiltuser,sc);
            }
            else{
                MainMenu.mainMenu(rebuiltuser,sc);
            }

    }catch(SQLException e)
        {
        System.out.println("Something went wrong");
        System.out.println(e.getMessage());
        }
    }
}
