
package com.omkar.dsamentor.ui;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.omkar.dsamentor.model.ContestQuestion;
import com.omkar.dsamentor.model.User;
import com.omkar.dsamentor.service.AiContestService;

public class AiContestUi {

    public static void displayContest(User user, Scanner sc) {

        System.out.println();
        System.out.println("======================================================");
        System.out.println("             AI PERSONALIZED DSA CONTEST");
        System.out.println("======================================================");
        System.out.println("Current DSA Level: " + user.getDsaLevel());
        System.out.println();

        try {
            System.out.println(
                    "Generating your personalized contest...");
            System.out.println();

            List<ContestQuestion> contest =
                    AiContestService.generateContest(user);

            int totalMinutes = 0;

            for (ContestQuestion question : contest) {
                totalMinutes += question.getAllocatedMinutes();
            }

            System.out.println("Contest generated successfully!");
            System.out.println("Questions: " + contest.size());
            System.out.println(
                    "Total Allocated Time: " + totalMinutes + " minutes");

            for (int i = 0; i < contest.size(); i++) {

                ContestQuestion question = contest.get(i);

                System.out.println();
                System.out.println("======================================================");
                System.out.println("QUESTION " + (i + 1));
                System.out.println("======================================================");

                System.out.println(
                        "Title          : " + question.getTitle());

                System.out.println(
                        "Platform       : " + question.getPlatform());

                System.out.println(
                        "Allocated Time : "
                                + question.getAllocatedMinutes()
                                + " minutes");

                if (question.getUrl() != null
                        && !question.getUrl().isBlank()) {

                    System.out.println(
                            "Problem URL    : " + question.getUrl());

                } else {

                    System.out.println(
                            "Problem URL    : Not confidently available.");

                    System.out.println(
                            "Search using the problem title on the listed platform.");
                }
            }

            System.out.println();
            System.out.println("======================================================");
            System.out.println("CONTEST NOTES");
            System.out.println("======================================================");
            System.out.println(
                    "Solve each question independently on its platform.");
            System.out.println("Time allocations are AI-generated estimates.");
            System.out.println(
                    "Solutions are not independently verified.");
            System.out.println(
                    "AI-suggested links have not been live-verified.");

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
            System.out.println("Contest generation was interrupted.");

        } catch (SQLException | IOException | IllegalArgumentException e) {

            System.out.println("Unable to generate the contest.");
            System.out.println("Reason: " + e.getMessage());
        }
    }
}
