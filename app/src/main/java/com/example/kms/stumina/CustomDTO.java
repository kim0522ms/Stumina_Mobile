package com.example.kms.stumina;

import android.graphics.Bitmap;

public class CustomDTO {
    private int resId;
    private String title;
    private String content;
    private Bitmap image;
    private String std_no;

    public String getStd_no() {
        return std_no;
    }

    public void setStd_no(String std_no) {
        this.std_no = std_no;
    }
    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
