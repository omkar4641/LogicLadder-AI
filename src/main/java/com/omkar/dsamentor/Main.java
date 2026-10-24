package com.omkar.dsamentor;


import java.sql.SQLException;
import java.util.Scanner;


import com.omkar.dsamentor.ui.CreateAC;
import com.omkar.dsamentor.ui.InitialMenu;
import com.omkar.dsamentor.ui.Login;

public class Main {
    public static void main(String[] args) throws SQLException{
        Scanner sc = new Scanner(System.in);
        //below method will bring the user responce in choice variable,through which we will navigate in respective pages with switch
        int choice = InitialMenu.initialMenu(sc);

        // Navigation from initial to respective pages as per the user choice
        switch (choice) {
            case 1:
                CreateAC.createAcount(sc);
                break;

            case 2:
                Login.loginIn(sc);
                break;

            case 3:
                System.out.println("Thank you for using DSA Mentor!");
                sc.close();
                return;
        }
    }
}
