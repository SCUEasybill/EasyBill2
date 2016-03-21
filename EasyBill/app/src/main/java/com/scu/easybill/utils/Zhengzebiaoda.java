package com.scu.easybill.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by nevermind on 2016/3/8.
 */
public class Zhengzebiaoda {
    //验证是否为手机号
    public static boolean isMobilNO(String mobiles){
        String regex = "^[1]+[3,8]+\\d{9}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    //验证是否为邮箱号
    public static boolean isEmailNO(String emails){
        String regex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(emails);
        return m.matches();
    }
    public static void  main(String[] args){
        //验证
    }
}
