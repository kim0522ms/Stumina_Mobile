package com.example.kms.stumina.Attandance;

public class CustomAttendanceDTO {
    private String user_name;
    private boolean att_value;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public boolean isAtt_value() {
        return att_value;
    }

    public void setAtt_value(boolean att_value) {
        this.att_value = att_value;
    }
}
