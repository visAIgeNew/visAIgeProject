package com.example.capstone1;

public class logList {
    private int id;
    private String date;
    private String img;
    private boolean success;
    private String user_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }


    public logList() {}

    public logList(int id, String date, String img, boolean success, String user_id) {
        this.id = id;
        this.date = date;
        this.img = img;
        this.success = success;
        this.user_id = user_id;
    }

    public logList(String date, String img, boolean success, String user_id) {
        this.date = date;
        this.img = img;
        this.success = success;
        this.user_id = user_id;
    }
}
