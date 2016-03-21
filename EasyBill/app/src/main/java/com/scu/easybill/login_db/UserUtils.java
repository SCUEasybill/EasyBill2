package com.scu.easybill.login_db;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

import static com.scu.easybill.utils.ConstantsUtil.LOGIN;
import static com.scu.easybill.utils.ConstantsUtil.NOLOGIN;

/**
 * Created by guyu on 2016/3/19.
 */
public class UserUtils extends AppCompatActivity {

    //在本地得到头部图片
    public static Drawable updatePortrait() {
        Drawable drawableHeader = null;
        Bitmap photo = null;
        String filePath = Environment.getExternalStorageDirectory() + "/JiaXT/Portrait" + "/temphead.jpg";
        File file = new File(filePath);
        if (file.exists()) {
            photo = BitmapFactory.decodeFile(filePath);//对本地文件进行解码
            drawableHeader = new BitmapDrawable(null, photo);
        } else {

        }
        return drawableHeader;
    }

    //获取登录状态
    public final String getLoginState() {
        String loginState = null;
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        loginState = sp.getString("isLogin", NOLOGIN);
        return loginState;
    }

    //设置登录状态
    public void setLoginState() {
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("isLogin", LOGIN);
        editor.commit();
    }
    //在本地存储用户信息
    private void userStore(String s, String s1, String s2, String s3, int i) {
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userId", s);
        editor.putString("userName", s1);
        editor.putString("userPhone", s2);
        editor.putString("userEmail", s3);
        editor.putInt("userTotalMoney", i);
        editor.putString("isLogin",LOGIN);
        editor.commit();
    }
}
