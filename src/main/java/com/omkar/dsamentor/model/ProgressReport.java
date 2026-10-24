package com.omkar.dsamentor.model;

public class ProgressReport {
    private int easy;
    private int medium;
    private int hard;
    private String currentDsaLevel;
    private int TotalEligiableQuestionCount;
    private int TotalSolvedQuestionCount;
    private int completionPercentage;

    public ProgressReport(int easy,int medium,int hard,String currentDsaLevel,int TotalEligQuestions,int TotalSolvedQues,int compPercentage)
    {
        this.easy=easy;
        this.medium=medium;
        this.hard=hard;
        this.currentDsaLevel=currentDsaLevel;
        this.TotalEligiableQuestionCount=TotalEligQuestions;
        this.TotalSolvedQuestionCount=TotalSolvedQues;
        this.completionPercentage=compPercentage;
    }

    public String getCurrentDsaLevel()
    {
        return currentDsaLevel;
    }

    public int getTotalEligiableQuestionCount()
    {
        return TotalEligiableQuestionCount;
    }

    public int getTotalSolvedQuestionCount()
    {
        return TotalSolvedQuestionCount;
    }

    public int getCompletionPercentage()
    {
        return completionPercentage;
    }

    public int getEasyQuestionCount()
    {
        return easy;
    }

    public int getMediumQuestionCount()
    {
        return medium;
    }

    public int getHardQuestionCount()
    {
        return hard;
    }
}
