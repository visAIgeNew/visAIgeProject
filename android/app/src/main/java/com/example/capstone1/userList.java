package com.example.capstone1;

import java.util.HashMap;

public class userList {

    private String id;
    private String password;
    private String name;
    private String otp_key;
    private HashMap<String, String> qr_code = new HashMap<String, String>();
    private String phone_num;
    private String lock_pwd;

    public userList() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
            return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtp_key() {
        return otp_key;
    }

    public void setOtp_key(String otp_key) {
        this.otp_key = otp_key;
    }

    public HashMap<String, String> getQr_code() {
        return qr_code;
    }

    public void setQr_code(HashMap<String, String> qr_code) {
        this.qr_code = qr_code;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getLock_pwd() {
        return lock_pwd;
    }

    public void setLock_pwd(String lock_pwd) {
        this.lock_pwd = lock_pwd;
    }

    public userList(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }
}
