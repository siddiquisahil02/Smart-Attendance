package com.boiz.experiment;

public class UserProfile
{

    private int UserPresent;
    private int UserTotal;
    private int UserGoal;

    public UserProfile()
    {

    }

    public UserProfile(int userPresent, int userTotal, int userGoal) {
        UserPresent = userPresent;
        UserTotal = userTotal;
        UserGoal = userGoal;
    }

    public int getUserPresent() {
        return UserPresent;
    }

    public void setUserPresent(int userPresent) {
        UserPresent = userPresent;
    }

    public int getUserTotal() {
        return UserTotal;
    }

    public void setUserTotal(int userTotal) {
        UserTotal = userTotal;
    }

    public int getUserGoal() {
        return UserGoal;
    }

    public void setUserGoal(int userGoal) {
        UserGoal = userGoal;
    }
}

