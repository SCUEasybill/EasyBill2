package com.scu.easybill.login_db;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.scu.easybill.utils.Zhengzebiaoda;
import com.scu.easybill.utils.getNetworkState;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import xyz.anmai.easybill.R;

import static com.scu.easybill.utils.ConstantsUtil.EMAIL;
import static com.scu.easybill.utils.ConstantsUtil.FINDPASSWORD;
import static com.scu.easybill.utils.ConstantsUtil.PHONE;
import static com.scu.easybill.utils.ConstantsUtil.REGISTER;


/**
 * Created by guyu on 2016/3/1.
 */
public class FindPasswordPh_Em extends AppCompatActivity {

    //传入的参数 action 表示传入的是注册还是修改密码，action_type表示是手机还是邮箱
    String action = null;
    String isLogin = null;
    //需要用正则表达式判断是手机号还是邮箱号，然后执行不同的发送程序。同时判断是否满足手机或者邮箱的格式
    String action_type = null;
    EditText edtPhoneNum, edtYanzhengma;
    Button btnNext;
    TextView tvGetYanzhengma;
    ProgressDialog ps;
    String ph_emNum, yanzhengma;
    private boolean ready;
    int yanzhengVerify = 0;
    boolean isMobile, isEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_forget_ph_em);
        Intent intent = getIntent();
        action = intent.getStringExtra("action");
        isLogin = intent.getStringExtra("islogin");
        Toast.makeText(getApplicationContext(), "action" + action + isLogin, Toast.LENGTH_LONG).show();

        edtPhoneNum = (EditText) findViewById(R.id.edt_phoneNum);
        edtYanzhengma = (EditText) findViewById(R.id.edt_yanzhengma);
        tvGetYanzhengma = (TextView) findViewById(R.id.tv_getYanzhengma);
        btnNext = (Button) findViewById(R.id.btn_next);
        //获取验证码
        tvGetYanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ph_emNum = edtPhoneNum.getText().toString();
                Toast.makeText(getApplicationContext(), "ph_em" + ph_emNum, Toast.LENGTH_LONG).show();
                if (!ph_emNum.equals("")) {
                    Toast.makeText(getApplicationContext(), "ph_em不为空", Toast.LENGTH_LONG).show();
                    isMobile = Zhengzebiaoda.isMobilNO(ph_emNum);
                    isEmail = Zhengzebiaoda.isEmailNO(ph_emNum);
                    Toast.makeText(getApplicationContext(), "ph" + isMobile + ",em" + isEmail, Toast.LENGTH_LONG).show();
                    boolean netState = getNetworkState.isNetworkConnected(getApplicationContext());
                    if (netState) {
                        if (isMobile) {
                            Toast.makeText(getApplicationContext(), "正确的手机号", Toast.LENGTH_LONG).show();
                            action_type = PHONE;
                            //初始化手机发送验证码SDK
                            SMSSDK.initSDK(FindPasswordPh_Em.this, "fd884658fad9", "a3487e5c9dbe8544cb9d863cd5636f6f");
                            EventHandler eh = new EventHandler() {
                                @Override
                                public void afterEvent(int event, int result, Object data) {
                                    Message msg = new Message();
                                    msg.arg1 = event;
                                    msg.arg2 = result;
                                    msg.obj = data;
                                    handler.sendMessage(msg);
                                }
                            };
                            SMSSDK.registerEventHandler(eh);
                            ready = true;
                            SMSSDK.getVerificationCode("86", edtPhoneNum.getText().toString());
                            Toast.makeText(getApplicationContext(), "手机验证码获取成功", Toast.LENGTH_LONG).show();
                        } else if (isEmail) {
                            Toast.makeText(getApplicationContext(), "正确的邮箱号", Toast.LENGTH_LONG).show();
                            action_type = EMAIL;
                            //开启新的线程
                            sendEmail(ph_emNum);
                            Toast.makeText(getApplicationContext(), "邮箱验证码获取成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "请输入正确的邮箱号或手机号", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "没有网络", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(FindPasswordPh_Em.this, "电话或者邮箱不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //下一步
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yanzhengma = edtYanzhengma.getText().toString().trim();
                Toast.makeText(getApplicationContext(), "yanzhengma" + yanzhengma, Toast.LENGTH_LONG).show();
                if (!TextUtils.isEmpty(yanzhengma)) {
                    if (action_type.equals(PHONE)) {
                        SMSSDK.submitVerificationCode("86", edtPhoneNum.getText().toString(), edtYanzhengma.getText().toString());
                        Toast.makeText(getApplicationContext(), "手机验证码提交成功", Toast.LENGTH_LONG).show();
                    }
                    if (action_type.equals(EMAIL)) {
                        Toast.makeText(getApplicationContext(), "邮箱验证码提交成功yanzhengVerify" + yanzhengVerify, Toast.LENGTH_LONG).show();
                        if (Integer.parseInt(yanzhengma) == yanzhengVerify) {//得到的验证码和发送的验证码相同
                            Intent intent = new Intent(FindPasswordPh_Em.this, SetNewPsw.class);
                            intent.putExtra("islogin", isLogin);
                            intent.putExtra("ph_emNum", ph_emNum);
                            if (action.equals(REGISTER)) {
                                intent.putExtra("action", REGISTER);
                            } else if (action.equals(FINDPASSWORD)) {
                                intent.putExtra("action", FINDPASSWORD);
                            }
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "邮箱验证码错误", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "验证码不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void init() {
        edtPhoneNum = (EditText) findViewById(R.id.edt_phoneNum);
        edtYanzhengma = (EditText) findViewById(R.id.edt_yanzhengma);
        tvGetYanzhengma = (TextView) findViewById(R.id.tv_getYanzhengma);
        btnNext = (Button) findViewById(R.id.btn_next);
    }

    /**
     * 处理手机信息
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，返回MainActivity,然后提示新好友
                Toast.makeText(FindPasswordPh_Em.this, "成功成功成功", Toast.LENGTH_LONG).show();
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                    //手机验证成功，跳转到密码界面
                    Intent intent = new Intent(FindPasswordPh_Em.this, SetNewPsw.class);
                    intent.putExtra("ph_emNum", ph_emNum);
                    if (action.equals(REGISTER)) {
                        intent.putExtra("action", REGISTER);
                    } else if (action.equals(FINDPASSWORD)) {
                        intent.putExtra("action", FINDPASSWORD);
                    }
                    intent.putExtra("islogin", isLogin);
                    startActivity(intent);
                    finish();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }
            } else {
                ((Throwable) data).printStackTrace();
                Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 发送邮件
     *
     * @param toAddress
     */
    public void sendEmail(final String toAddress) {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                yanzhengVerify = (int) (Math.random() * 1000000);
                //这个类主要是设置邮件
                MultiMailsender.MultiMailSenderInfo mailInfo = new MultiMailsender.MultiMailSenderInfo();
                mailInfo.setMailServerHost("smtp.126.com");
                mailInfo.setMailServerPort("25");
                mailInfo.setValidate(true);
                mailInfo.setUserName("gu972042723@126.com");
                mailInfo.setPassword("gu19941120.");//您的邮箱密码
                mailInfo.setFromAddress("gu972042723@126.com");
//                mailInfo.setToAddress("972042723@qq.com");
                mailInfo.setToAddress(toAddress);
                mailInfo.setSubject("找回密码");
                mailInfo.setContent("验证码是：" + yanzhengVerify);
                //这个类主要来发送邮件
                MultiMailsender sms = new MultiMailsender();
                boolean re = sms.sendTextMail(mailInfo);//发送文体格式
                if (re) {
                    Toast.makeText(getApplicationContext(), "验证码已经发送", Toast.LENGTH_SHORT).show();
                }
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putInt("yanzhengma", yanzhengVerify);
                bundle.putString("email", ph_emNum);
                msg.setData(bundle);
                msg.what = 0;
                sendEmHandler.sendMessage(msg);
            }
        }.start();
    }

    Handler sendEmHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(getApplicationContext(), "handleMessage===验证码已经发送", Toast.LENGTH_SHORT).show();
                default:

            }

        }
    };

    protected void onDestroy() {
        if (ready) {
            // 销毁回调监听接口
            SMSSDK.unregisterAllEventHandler();
            Toast.makeText(getApplicationContext(), "销毁回调监听接口", Toast.LENGTH_SHORT).show();
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
