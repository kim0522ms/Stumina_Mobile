package com.example.kms.stumina;

import android.graphics.Bitmap;

public class UserInfo {
    private String user_idx;
    private String user_name;
    private String user_area;
    private String user_sex;
    private String user_jobno;
    private String user_phone;
    private String user_belong;

    private Bitmap user_photo;

    public String getUser_area() {
        return user_area;
    }

    public void setUser_area(String user_area) {
        this.user_area = user_area;
    }

    public String getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_jobno() {
        return user_jobno;
    }

    public void setUser_jobno(String user_jobno) {
        this.user_jobno = user_jobno;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_belong() {
        return user_belong;
    }

    public void setUser_belong(String user_belong) {
        this.user_belong = user_belong;
    }

    public String getUser_idx() {
        return user_idx;
    }

    public void setUser_idx(String user_idx) {
        this.user_idx = user_idx;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public Bitmap getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(Bitmap user_photo) {
        this.user_photo = user_photo;
    }
}
