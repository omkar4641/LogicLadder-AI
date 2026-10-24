//the classes prsesnt in model package are used to create objects as per our requirements as here we are creating the object of user with the fields retrived from Mysql dB and we can using that object at onboarding
package com.omkar.dsamentor.model;
import java.time.LocalDateTime;



public class User {
    private String name;
    private String email;
    public String username;
    private String passHash;
    private String dsaLevel;
    private String userType;

    private boolean onBoardingCompleted;
    private int dailyStudyMin;

    private LocalDateTime lastActiveAt;

    //constructor for creating new user for storing credientials
    public User(String name,String email,String username,String passhash)
    {
        this.name=name;
        this.email=email;
        this.username=username;
        this.passHash=passhash;
        this.onBoardingCompleted=false;
        this.dailyStudyMin=0;
        this.lastActiveAt=null;
        this.dsaLevel=null;
        this.userType=null;
    }

    //constructor for building the user based on info retrived from DB
    public User(String name,String email,String username,String dsaLevel,String userType,boolean onBoardingCompleted,int dailyStudyMin,LocalDateTime lastActiveAt)
    {
        this.name=name;
        this.email=email;
        this.username=username;
        this.dsaLevel=dsaLevel;
        this.userType=userType;
        this.onBoardingCompleted=onBoardingCompleted;
        this.dailyStudyMin=dailyStudyMin;
        this.lastActiveAt=lastActiveAt;
    }

    
    //getters
    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getUsername()
    {
        return username;
    }

    public boolean isOnBoardingCompleted()
    {
        return onBoardingCompleted;
    }

    public int getDailyStudyMin()
    {
        return dailyStudyMin;
    }

    public LocalDateTime getLastActiveAt()
    {
        return lastActiveAt;
    }

    public String getPassHash()
    {
        return passHash;
    }

    public String getDsaLevel()
    {
        return dsaLevel;
    }

    public String getUserType()
    {
        return userType;
    }


    //setters
    public void setDailyStudyMin(int min)
    {
        this.dailyStudyMin=min;
    }

    public void setOnboardingCompleted(boolean completed)
    {
        this.onBoardingCompleted=completed;
    }

    public void setDsaLevel(String dsaLevel)
    {
        this.dsaLevel = dsaLevel;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

}
