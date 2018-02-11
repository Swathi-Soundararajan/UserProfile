package com.example.user.userprofile;


public class UserList {

    private String Uname;
    private String skills;
    private String dp;

    public UserList(String uname, String skills, String dp) {
        Uname = uname;
        this.skills = skills;
        this.dp = dp;
    }

    public String getUname() {
        return Uname;
    }

    public String getSkills() {
        return skills;
    }

    public String getDp() {
        return dp;
    }
}
