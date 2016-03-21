package com.scu.easybill.login_db;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scu.easybill.utils.MyConnector;
import com.scu.easybill.utils.getNetworkState;

import xyz.anmai.easybill.MainActivity;
import xyz.anmai.easybill.R;

import static com.scu.easybill.utils.ConstantsUtil.FINDPASSWORD;
import static com.scu.easybill.utils.ConstantsUtil.LOGIN;
import static com.scu.easybill.utils.ConstantsUtil.NOLOGIN;
import static com.scu.easybill.utils.ConstantsUtil.REGISTER;
import static com.scu.easybill.utils.ConstantsUtil.SERVER_ADDRESS;
import static com.scu.easybill.utils.ConstantsUtil.SERVER_PORT;
import static com.scu.easybill.utils.FileUtil.saveFile;

/**
 * Created by guyu on 2016/1/29.
 */
public class Login extends AppCompatActivity {
    MyConnector mc;
    Button btn_login, btn_cancle;
    EditText etUid, etPwd;
    TextView tv_register, tv_findPswPh, tv_registerEm;
    ProgressDialog pd;
    String uid, pwd;
    String isLogin = null;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();//初始化登录界面
        getAppState();
        checkIfRemember();
        //登陆
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUid = (EditText) findViewById(R.id.edt_account);    //获得帐号EditText
                etPwd = (EditText) findViewById(R.id.edt_psw);    //获得密码EditText
                uid = etUid.getEditableText().toString().trim();    //获得输入的帐号
                pwd = etPwd.getEditableText().toString().trim();    //获得输入的密码
                if (uid.equals("") || pwd.equals("")) {        //判断输入是否为空
                    Toast.makeText(Login.this, "请输入帐号或密码!", Toast.LENGTH_SHORT).show();//输出提示消息
                    return;
                }
                boolean netState = getNetworkState.isNetworkConnected(getApplicationContext());//获取网络状态
                if (netState) {
                    pd = ProgressDialog.show(Login.this, "请稍候", "正在连接服务器...", false, true);
                    login();
                } else {
                    Toast.makeText(getApplicationContext(), "没有网络", Toast.LENGTH_LONG).show();
                }

            }
        });

        //手机注册
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, FindPasswordPh_Em.class);
                intent.putExtra("action", REGISTER);
                intent.putExtra("islogin", isLogin);
                startActivity(intent);
            }
        });

        //忘记密码,用手机号找回
        tv_findPswPh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, FindPasswordPh_Em.class);
                intent.putExtra("action", FINDPASSWORD);
                intent.putExtra("islogin", isLogin);
                startActivity(intent);
            }
        });
    }

    private void getAppState() {
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        isLogin = sp.getString("isLogin", NOLOGIN);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_cancle = (Button) findViewById(R.id.btn_cancel);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_findPswPh = (TextView) findViewById(R.id.tv_findPswPh);
        tv_registerEm = (TextView) findViewById(R.id.tv_registerEm);
    }

    /**
     * 登陆
     */
    public void login() {
        new Thread(new Runnable() {
            public void run() {
                Looper.prepare();
                try {
                    if (mc == null) {
                        mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
                    }
                    String msg = "<#LOGIN#>" + uid + "|" + pwd;                    //组织要返回的字符串
                    mc.dout.writeUTF(msg);                                        //发出消息
                    String receivedMsg = mc.din.readUTF();        //读取服务器发来的消息
                    if (receivedMsg.startsWith("<#LOGINSUCCESS#>")) {    //收到的消息为登录成功消息
                        receivedMsg = receivedMsg.substring(16);
                        String[] sa = receivedMsg.split("\\|");
                        CheckBox cb = (CheckBox) findViewById(R.id.cb_remember);        //获得CheckBox对象
                        if (cb.isChecked()) {
                            rememberMe(uid, pwd);
                        }
                        user = new Users(sa[0], sa[1], sa[2], sa[3], 0, null);//user_id, user_name, user_phone, user_email,总金额，头像信息
                        userStore(sa[0], sa[1], sa[2], sa[3], 0);//将用户信息保存到本地，以便脱网使用
                        Toast.makeText(Login.this, "login====" + user.getUser_id() + user.getUser_name(), Toast.LENGTH_LONG).show();
                       //接收图片
                        int size = mc.din.readInt();			//读取图片大小
                        byte [] buf = new byte[size];		//创建字节数组
                        for(int i=0;i<size;i++){
                            buf[i] = mc.din.readByte();
                        }
                        String filePath = "";
                        saveFile(getApplicationContext(),filePath,"temphead.jpg",buf);//把图片存到本地
                        pd.dismiss();
                        Message msghandle = new Message();
                        msghandle.what = 0;
                        handler_login.sendMessage(msghandle);
                        Looper.loop();
                    } else if (receivedMsg.startsWith("<#LOGINFAIL#>")) {                    //收到的消息为登录失败
                        Toast.makeText(Login.this, receivedMsg.substring(14), Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Looper.myLooper().quit();
            }
        }).start();
    }

    //handle 处理登录
    android.os.Handler handler_login = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String userId = bundle.getString("userId");
            switch (msg.what) {
                case 0:
                    //转到主界面,需要取得全部的信息
                    Toast.makeText(getApplicationContext(), "成功登陆" + userId + LOGIN, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                case 1:
                default:
            }
        }
    };

    //方法：将用户的id和密码存入Preferences
    public void rememberMe(String uid, String pwd) {
        SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);    //获得Preferences
        SharedPreferences.Editor editor = sp.edit();            //获得Editor
        editor.putString("uid", uid);                            //将用户名存入Preferences
        editor.putString("pwd", pwd);                            //将密码存入Preferences
        editor.commit();
    }

    //方法：从Preferences中读取用户名和密码
    public void checkIfRemember() {
//        SharedPreferences sp = getPreferences(MODE_PRIVATE);    //获得Preferences
        SharedPreferences sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        String uid = sp.getString("uid", null);
        String pwd = sp.getString("pwd", null);
        if (uid != null && pwd != null) {
            EditText etUid = (EditText) findViewById(R.id.edt_account);
            EditText etPwd = (EditText) findViewById(R.id.edt_psw);
            CheckBox cbRemember = (CheckBox) findViewById(R.id.cb_remember);
            etUid.setText(uid);
            etPwd.setText(pwd);
            cbRemember.setChecked(true);
        }
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
        editor.putString("isLogin", LOGIN);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(Login.this,MainActivity.class);
//        intent.putExtra("islogin",isLogin);
//        startActivity(intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        if (mc != null) {
            mc.sayBye();
        }
        mc = null;
        super.onDestroy();
    }
}
