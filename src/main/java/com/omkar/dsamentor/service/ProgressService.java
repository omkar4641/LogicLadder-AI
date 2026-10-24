package com.omkar.dsamentor.service;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

import com.omkar.dsamentor.model.DSAProblem;
import com.omkar.dsamentor.model.ProgressReport;
import com.omkar.dsamentor.model.User;
import com.omkar.dsamentor.repository.DsaProblemRepository;
import com.omkar.dsamentor.repository.UserProblemProgressRepository;

public class ProgressService {
    public static ProgressReport userProgress(User user) throws SQLException
    {
        //=================================================for layer 1 where we display the statistics as per user'current dsa level==============
        String currentDsaLevel=user.getDsaLevel();

        List<DSAProblem> eligiableQuestions=DsaProblemRepository.getEligibleProblems(currentDsaLevel);

        //total eligiabl question size for user's current dsa_level example:user dsa level is "BEGINEER1" and he might have 40 questions to solve in this dsa_level
        int TotaleligiableQuestionCount=eligiableQuestions.size();

        // IDs of questions solved by this user
        HashSet<Integer> solvedProblemIdByUser=UserProblemProgressRepository.getProblemIdForCheckUnsolvedProblemsByUser(user.getUsername());

        // Count solved questions belonging to the current level
        int solvedQuestionCountForCurrentDsaLevel = 0;
        for (DSAProblem problem : eligiableQuestions)
        {
            if (solvedProblemIdByUser.contains(problem.getProblemID()))
            {
                solvedQuestionCountForCurrentDsaLevel++;
            }
        }


        // Calculate completion percentage safely
        int completionPercentage = 0;
        if (TotaleligiableQuestionCount > 0)
        {
            completionPercentage = (int) (solvedQuestionCountForCurrentDsaLevel * 100.0 / TotaleligiableQuestionCount);
        }


        //=====================================Second layer where we will display the complition of question as per difficulty of question=====
        //we will reuse the solvedProbleIdByuser hashset at line 25 for getting the level of those problems present in hash set

        int easy=0;
        int medium=0;
        int hard=0;

        for(int problemid:solvedProblemIdByUser)
        {
            String difficulty=DsaProblemRepository.getDifficulty(problemid);
            if(difficulty.equalsIgnoreCase("easy"))
            {
                easy++;
            }
            else if(difficulty.equalsIgnoreCase("medium"))
            {
                medium++;
            }
            else if(difficulty.equalsIgnoreCase("hard"))
            {
                hard++;
            }
            else
            {
                System.out.println("Unknown difficulty in DB at dsa_problem table");
                return null;
            }
        }

        ProgressReport PR=new ProgressReport(easy,medium,hard,currentDsaLevel, TotaleligiableQuestionCount, solvedQuestionCountForCurrentDsaLevel, completionPercentage);
        return PR;
    }
}
