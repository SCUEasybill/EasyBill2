package com.scu.easybill.login_db;

import java.io.Serializable;

/**
 * Created by nevermind on 2016/3/7.
 * 让一个类去实现 Serializable 这个接口就可以了
 */
public class Users  implements Serializable{
    private String user_id = null;
    private String user_name = null;
    private String user_phone = null;
    private String user_portrait = null;
    private String user_email = null;
    private int user_total_money = 0;

    public Users(){
        super();
    }
    public Users (String user_id,String user_name, String user_phone, String user_email, int user_total_money,String user_portrait){
        this.user_id = user_id;
        this.user_email = user_email;
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.user_total_money = user_total_money;
        this.user_portrait = user_portrait;
    }
    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public void setUser_portrait(String user_portrait) {
        this.user_portrait = user_portrait;
    }

    public void setUser_total_money(int user_total_money) {
        this.user_total_money = user_total_money;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_portrait() {
        return user_portrait;
    }

    public int getUser_total_money() {
        return user_total_money;
    }

}
