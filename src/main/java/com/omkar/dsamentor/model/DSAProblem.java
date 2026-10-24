package com.omkar.dsamentor.model;

public class DSAProblem {
    private int problemId;
    private String title;
    private String topic;
    private String pattern;
    private String difficulty;
    private int estimatedTime;
    private String platform;
    private String probURL;  //problrm url:question link
    private String resURL;   //resource url:the resource of that question to get help or understand problem
    private int learningStage;
    private boolean isActive;
    private String problemStyle;


    public DSAProblem(int problem_id,String title,String topic,String pattern,String difficulty,int estimatedTime,String platform,String probURL,String resURL,int learningStage,boolean is_active,String problemStyle)
    {
        this.problemId=problem_id;
        this.title=title;
        this.topic=topic;
        this.pattern=pattern;
        this.difficulty=difficulty;
        this.estimatedTime=estimatedTime;
        this.platform=platform;
        this.probURL=probURL;
        this.resURL=resURL;
        this.learningStage=learningStage;
        this.isActive=is_active;
        this.problemStyle=problemStyle;
    }


    public int getProblemID()
    {
        return problemId;
    }

    public String getTitle()
    {
        return title;
    }

    public String getTopic()
    {
        return topic;
    }

    public String getPattern()
    {
        return pattern;
    }

    public String getDifficulty()
    {
        return difficulty;
    }

    public int getEstTime()
    {
        return estimatedTime;
    }

    public String getPlatform()
    {
        return platform;
    }

    public String getProURL()
    {
        return probURL;
    }

    public String getResURL()
    {
        return resURL;
    }

    public int getlearningStage()
    {
        return learningStage;
    }

    public boolean getIsActive()
    {
        return isActive;
    }

    public String getProblemStyle()
    {
        return problemStyle;
    }
}
