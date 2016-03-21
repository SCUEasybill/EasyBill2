package com.scu.easybill.login_db;//package com.scu.login_db;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import java.io.IOException;
//
//import xyz.anmai.easybill.R;
//
//import static com.scu.login_db.ConstantsUtil.FINDPASSWORD;
//import static com.scu.login_db.ConstantsUtil.LOGIN;
//import static com.scu.login_db.ConstantsUtil.NOLOGIN;
//import static com.scu.login_db.ConstantsUtil.REGISTER;
//import static com.scu.login_db.ConstantsUtil.SERVER_ADDRESS;
//import static com.scu.login_db.ConstantsUtil.SERVER_PORT;
//
///**
// * Created by guyu on 2016/3/1.
// * 说明：找回密码分为三种情况：1、未登录，已注册。2、未注册。3、已经登录，要求重设密码
// * 总的来说，分为已登录和未登录状态，用islogin标识
// */
//public class SetNewPsw extends AppCompatActivity {
//    //传入参数
//    String action = null;//标识是注册还是找回密码
//    String ph_emNum = null;//用户手机号或者邮箱号
//    String isLogin = null;
//
//    EditText edtOldPsw, edtNewPsw1, edtNewPsw2, edtName;
//    String oldPsw, newPsw1, newPsw2, nickName;
//    MyConnector mc = null;
//    int action_isLogin = -1; //状态标识
//
//    String TAG = "setnewpsw";
//
//    //传出去的参数
//    public static void activityStart(Context context) {
//        Intent intent = new Intent(context, SetNewPsw.class);
//        context.startActivity(intent);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {//加入自定义标题
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login_forget_findpsw);
//        Intent intent = getIntent();
//        isLogin = intent.getStringExtra("islogin");
//        action = intent.getStringExtra("action");//获取事件状态
//        ph_emNum = intent.getStringExtra("ph_emNum");
//        Toast.makeText(SetNewPsw.this, "action==" + action + ph_emNum, Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "action==" + action + ph_emNum);
//        //添加titlebar[开始]
//        Toolbar setPswTitlebar = (Toolbar) findViewById(R.id.register_titlebar);
//        if (action.equals(REGISTER)) {//未注册
//            action_isLogin = 0;
//            setTitle("用户注册");
////            LinearLayout linearLayout_oldPsw = (LinearLayout) findViewById(R.id.layout_oldPsw);
//            findViewById(R.id.layout_oldPsw).setVisibility(View.GONE);
//            Toast.makeText(SetNewPsw.this, "不显示旧密码", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "注册");
//        } else if (action.equals(FINDPASSWORD) && isLogin.equals(LOGIN)) {
//            action_isLogin = 11;
//            setTitle("找回密码");
//            findViewById(R.id.layout_name).setVisibility(View.GONE);
//            Toast.makeText(SetNewPsw.this, "不显示昵称框", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "已经登录，找回密码");
//        } else if (action.equals(FINDPASSWORD) && isLogin.equals(NOLOGIN)) {
//            action_isLogin = 10;
//            setTitle("找回密码");//不显示昵称和旧密码
//            findViewById(R.id.layout_oldPsw).setVisibility(View.GONE);
//            findViewById(R.id.layout_name).setVisibility(View.GONE);
//            Log.d(TAG, "未登录，找回密码");
//        }
////        setPswTitlebar.setSubtitle("修改密码");
//        setSupportActionBar(setPswTitlebar);
//        //返回上一层
//        setPswTitlebar.setNavigationIcon(android.R.drawable.ic_menu_revert);
//        setPswTitlebar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        setPswTitlebar.setOnMenuItemClickListener(onMenuitemClick);
//        //添加titlebar[结束]
//        init();
//    }
//
//    private Toolbar.OnMenuItemClickListener onMenuitemClick = new Toolbar.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            String msg = "";
//            switch (item.getItemId()) {
//                case R.id.menu_findpsw_finish:
//                    nickName = edtName.getText().toString().trim();
//                    oldPsw = edtOldPsw.getText().toString().trim();
//                    newPsw1 = edtNewPsw1.getText().toString().trim();
//                    newPsw2 = edtNewPsw2.getText().toString().trim();
//                    if (newPsw1.equals("")) {//当已经登陆并且未填写旧密码
//                        Toast.makeText(SetNewPsw.this, "请填写新密码！", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                    if (newPsw2.equals("")) {
//                        Toast.makeText(SetNewPsw.this, "请确认密码！", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                    if (!newPsw1.equals(newPsw2)) {
//                        Toast.makeText(SetNewPsw.this, "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                    if (oldPsw.equals(newPsw1)) {
//                        Toast.makeText(SetNewPsw.this, "新密码不能和旧密码相同！", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                    if ((action_isLogin == 11) && oldPsw.equals("")) {
//                        Toast.makeText(SetNewPsw.this, "请输入旧密码", Toast.LENGTH_SHORT).show();
//                        break;
//                    }
//                    ModifyPsw modifyPsw = new ModifyPsw();
//                    if ((action_isLogin == 11)) {
//                        modifyPsw.execute(new String[]{ph_emNum, newPsw1, oldPsw});
////                        ModifyPsw(ph_emNum, newPsw1, oldPsw);
//                    } else if ((action_isLogin == 0) || (action_isLogin == 10)) {
//                        modifyPsw.execute(new String[]{ph_emNum, newPsw1, nickName});
////                        ModifyPsw(ph_emNum, newPsw1, nickName);
//                    }
//                    break;
//                default:
//            }
//            return true;
//        }
//    };
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
//        getMenuInflater().inflate(R.menu.menu_findpsw_titlebar, menu);
//        return true;
//    }
//
//    private void init() {
//        edtName = (EditText) findViewById(R.id.edt_name);
//        edtOldPsw = (EditText) findViewById(R.id.edt_oldPsw);
//        edtNewPsw1 = (EditText) findViewById(R.id.edt_newPsw1);
//        edtNewPsw2 = (EditText) findViewById(R.id.edt_newPsw2);
//
//    }
//
//    /**
//     * 处理修改密码
//     * 参数：ph_emNum,newPsw1, oldPsw
//     */
//
//    public void ModifyPsw(final String s1, final String s2, final String s3) {
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                //执行后台操作
//                String msg = null;
//                if (mc == null) {
//                    mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
//                }
//                try {
//                    switch (action_isLogin) {
//                        case 0:
//                            if (Zhengzebiaoda.isMobilNO(s1)) {//判断是手机号
//                                msg = "<#REGISTER#>" + "phone" + "|" + s1 + "|" + s2 + "|" + s3;//传送手机号或者邮箱号，新密码，昵称
//                            } else if (Zhengzebiaoda.isEmailNO(s1)) {//判断是邮箱号
//                                msg = "<#REGISTER#>" + "email" + "|" + s1 + "|" + s2 + "|" + s3;//传送手机号或者邮箱号，新密码，昵称
//                            }
//                            break;
//                        case 11:
//                            msg = "<#FINDPASSWORD#>" + s1 + "|" + s2 + "|" + s3;//传送用户ID userId，新密码，旧密码
//                            break;
//                        case 10:
//                            msg = "<#FINDPASSWORD#>" + s1 + "|" + s2 + "|" + s2;//传送手机号或者邮箱号，新密码
//                            break;
//                        default:
//                            msg = "";
//                    }
//                    mc.dout.writeUTF(msg);//向服务器发送消息
//                    System.out.print("开始接收消息");
//                    String recvMsg = mc.din.readUTF();//接收来自服务器的消息
//                    if (recvMsg.startsWith("<#FINDPASSWORDSUCCESS#>")) {
//                        Toast.makeText(SetNewPsw.this, "修改密码成功", Toast.LENGTH_SHORT).show();
//                        Message msg_handle = new Message();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("resvMsg", recvMsg);
//                        msg_handle.setData(bundle);
//                        msg_handle.what = 1;
//                        handler_setpsw.sendMessage(msg_handle);
//                        Looper.loop();
//                    } else if (recvMsg.startsWith("<#REGISTERSUCCESS#>")) {
//                        Message msg_handle = new Message();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("resvMsg", recvMsg);
//                        msg_handle.setData(bundle);
//                        msg_handle.what = 0;
//                        handler_setpsw.sendMessage(msg_handle);
//                        Looper.loop();
//                    } else {
//                        Toast.makeText(SetNewPsw.this, "修改密码失败", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (IOException e) {//数据操作失败
//                    e.printStackTrace();
//                }
//                Looper.myLooper().quit();
//            }
//        }.start();
//    }
//
//    //handle 处理
//    android.os.Handler handler_setpsw = new android.os.Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Intent intent;
//            Bundle bundle = msg.getData();
//            String userId = bundle.getString("resvMsg");
//            switch (msg.what) {
//                case 0:
//                    //转到主界面
//                    Toast.makeText(SetNewPsw.this, "修改密码成功", Toast.LENGTH_SHORT).show();
//                    intent = new Intent(SetNewPsw.this, Login.class);
//                    intent.putExtra("islogin", NOLOGIN);
//                    startActivity(intent);
//                    finish();
//                case 1:
//                    //转到主界面
//                    Toast.makeText(SetNewPsw.this, "修改密码成功", Toast.LENGTH_SHORT).show();
//                    intent = new Intent(SetNewPsw.this, Login.class);
//                    intent.putExtra("islogin", NOLOGIN);
//                    startActivity(intent);
//                    finish();
//                default:
//            }
//        }
//    };
//
//    @Override
//    public void finish() {
//        super.finish();
//    }
//
//    class ModifyPsw extends AsyncTask<String, Integer, String> {
//        ProgressDialog pd;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pd = ProgressDialog.show(SetNewPsw.this, "重置密码", "请稍后...", true, true);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            //执行后台操作
//            String result = null;
//            String msg = null;
//            if (mc == null) {
//                mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
//            }
//            try {
//                switch (action_isLogin) {
//                    case 0:
//                        if (Zhengzebiaoda.isMobilNO(params[0])) {//判断是手机号
//                            msg = "<#REGISTER#>" + "phone" + "|" + params[0] + "|" + params[1] + "|" + params[2];//传送手机号或者邮箱号，新密码，昵称
//                        } else if (Zhengzebiaoda.isEmailNO(params[0])) {//判断是邮箱号
//                            msg = "<#REGISTER#>" + "email" + "|" + params[0] + "|" + params[1] + "|" + params[2];//传送手机号或者邮箱号，新密码，昵称
//                        }
//                        break;
//                    case 11:
//                        msg = "<#FINDPASSWORD#>" + params[0] + "|" + params[1] + "|" + params[2];//传送用户ID userId，新密码，旧密码
//                        break;
//                    case 10:
//                        msg = "<#FINDPASSWORD#>" + params[0] + "|" + params[1] + "|" + params[1];//传送手机号或者邮箱号，新密码
//                        break;
//                    default:
//                        msg = "";
//                }
//                mc.dout.writeUTF(msg);//向服务器发送消息
//                System.out.print("开始接收消息");
//                String recvMsg = mc.din.readUTF();//接收来自服务器的消息
//                result = recvMsg;
//                System.out.print("recvMsg==" + result);
//            } catch (IOException e) {//数据操作失败
//                e.printStackTrace();
//                result = "fail";
//            }
//            return result;
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            //对UI进行操作
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            pd.dismiss();
//            System.out.println("result==" + result);
//            if (result.startsWith("<#FINDPASSWORDSUCCESS#>")) {
//                Toast.makeText(SetNewPsw.this, "修改密码成功", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(SetNewPsw.this, Login.class);
//                intent.putExtra("islogin", NOLOGIN);
//                startActivity(intent);
//                finish();
//            } else if (result.startsWith("<#REGISTERSUCCESS#>")) {
//                Toast.makeText(SetNewPsw.this, "注册成功", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(SetNewPsw.this, Login.class);
//                intent.putExtra("islogin", NOLOGIN);
//                startActivity(intent);
//                finish();
//            } else {
//                Toast.makeText(SetNewPsw.this, "修改密码失败", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mc != null) {
//            mc.sayBye();
//        }
//        super.onDestroy();
//    }
//}
