package com.scu.easybill.utils;

/**
 * Created by guyu on 2016/3/19.
 */
public class UserInfoDetails {
    private String key;
    private String value;

    public UserInfoDetails(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}