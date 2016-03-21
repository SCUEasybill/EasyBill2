package com.scu.easybill.login_db;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.scu.easybill.utils.MyConnector;
import com.scu.easybill.utils.UserInfoAdapter;
import com.scu.easybill.utils.UserInfoDetails;
import com.scu.easybill.utils.Zhengzebiaoda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import xyz.anmai.easybill.R;
import xyz.anmai.easybill.SettingActivity;

import static com.scu.easybill.utils.ConstantsUtil.LOGIN;
import static com.scu.easybill.utils.ConstantsUtil.NOLOGIN;
import static com.scu.easybill.utils.ConstantsUtil.SERVER_ADDRESS;
import static com.scu.easybill.utils.ConstantsUtil.SERVER_PORT;

/**
 * Created by guyu on 2016/3/5.
 */
public class UserInfo extends AppCompatActivity {

    private List<UserInfoDetails> userInfoDetailsList = new ArrayList<UserInfoDetails>();
    /*定义一个动态数组*/
    ArrayList<HashMap<String, Object>> listItem;
    MyConnector mc = null;
    HashMap<String, Object> map;
    ProgressDialog ps;
    String edtInfo_dialog = null;
    UserInfoAdapter userInfoAdapter;
    //外传入数据
    String isLogin = null;
    Users user;
    Looper modifyLooper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_userinfo);
        Toolbar toolbarUser = (Toolbar) findViewById(R.id.toolbar_userinfo);
        setSupportActionBar(toolbarUser);

        toolbarUser.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbarUser.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo.this.finish();
            }
        });
        getAppState();//获取本地用户信息
        Toast.makeText(UserInfo.this, "jieguo" + user.getUser_id() + isLogin, Toast.LENGTH_LONG).show();
        initUserInfo();//初始化界面，填入用户信息
        userInfoAdapter = new UserInfoAdapter(UserInfo.this, R.layout.login_userinfo_details, userInfoDetailsList);
        ListView listView = (ListView) findViewById(R.id.lv_userinfo);
        listView.setAdapter(userInfoAdapter);

//        ListView lv = (ListView) findViewById(R.id.lv_userinfo);//得到ListView对象的引用 /*为ListView设置Adapter来绑定数据*/
//        listItem = new ArrayList<HashMap<String, Object>>();//数组中存放数据
//            map = new HashMap<String, Object>();
//            //添加数据
//            map.put("item_key", "第1行");
//            map.put("item_value", "这是第1行");
//            listItem.add(map);
//            map = new HashMap<String, Object>();
//            map.put("item_key", "第2行");
//            map.put("item_value", "这是第2行");
//            listItem.add(map);
        //简单添加适配器
//        ArrayAdapter adapter =  new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, strs);
//        lv.setAdapter(adapter);
        //自定义添加适配器，baseAdapter
//        MyAdapter mAdapter = new MyAdapter(this);//得到一个MyAdapter对象
//        lv.setAdapter(mAdapter);//为ListView绑定Adapter /*为ListView添加点击事件*/
        //使用simpleAdapter 适配器
//        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItem, R.layout.login_userinfo_details, new String[]{"item_key", "item_value"}, new int[]{R.id.tv_userInfo_key, R.id.tv_userInfo_value});
//        lv.setAdapter(simpleAdapter);

        //添加长按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "长按" + "parent" + parent + "position" + position + "id" + id, Toast.LENGTH_LONG).show();
                showModifyDialog(position);
//                initUserInfo();
                return true;
            }
        });
        Button btnExit = (Button) findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }


    //得到本地存储的用户信息
    public void getAppState() {
        user = new Users();
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        user.setUser_id(sp.getString("userId", "请上网"));
        user.setUser_name(sp.getString("userName", "请上网"));
        user.setUser_email(sp.getString("userEmail", "请上网"));
        user.setUser_phone(sp.getString("userPhone", "请上网"));
        user.setUser_total_money(sp.getInt("userTotalMoney", 0));
        isLogin = sp.getString("isLogin", NOLOGIN);
    }

    //初始化用户信息表
    private void initUserInfo() {
        UserInfoDetails name, phone, email;
        if (!userInfoDetailsList.isEmpty()) {
            userInfoDetailsList.clear();//首先去除原来的
        }
        if (isLogin.equals(LOGIN)) {
            name = new UserInfoDetails("昵称:", user.getUser_name());
            phone = new UserInfoDetails("手机号:", user.getUser_phone());
            email = new UserInfoDetails("邮箱:", user.getUser_email());
        } else {
            name = new UserInfoDetails("昵称:", "guyu");
            phone = new UserInfoDetails("手机号:", "13678109397");
            email = new UserInfoDetails("邮箱:", "972042723@qq.com");
        }
        userInfoDetailsList.add(name);
        userInfoDetailsList.add(phone);
        userInfoDetailsList.add(email);

        UserInfoDetails total_money = new UserInfoDetails("总金额:", Integer.toString(user.getUser_total_money()));//需要重新再加入一个listView
        userInfoDetailsList.add(total_money);
    }

    public void showModifyDialog(final int position) {
        String title = null;
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.login_userinfo_modify_dialog, (ViewGroup) findViewById(R.id.layout_dialog));
        final EditText editText = (EditText) layout.findViewById(R.id.edt_dialog);
        switch (position) {
            case 0:
                title = "请输入昵称:";
                break;
            case 1:
                title = "请输入新手机号:";
                break;
            case 2:
                title = "请输入新邮箱号:";
            default:

        }
        new AlertDialog.Builder(UserInfo.this)
                .setView(layout)
                .setMessage(title)
                .setPositiveButton(
                        "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //开启线程修改信息
                                edtInfo_dialog = editText.getEditableText().toString().trim();
                                modifyInfo(edtInfo_dialog, position);
                                initUserInfo();//更新用户信息
                                Toast.makeText(getApplicationContext(), "可以进行修改信息" + user.getUser_id() + edtInfo_dialog, Toast.LENGTH_LONG).show();
                            }
                        })
                .setNegativeButton(
                        "取消",
                        null)
                .show();
    }

    /**
     * 在线程中处理后台信息，传入参数:用户ID，输入内容，邮箱或者手机（position）
     */
    private void modifyInfo(final String edtInfo_dialog, final int position) {
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        switch (position) {
            case 0://昵称
                editor.putString("userName", edtInfo_dialog);
                user.setUser_name(edtInfo_dialog);
                break;
            case 1:
                if (Zhengzebiaoda.isMobilNO(edtInfo_dialog)) {
                    editor.putString("userPhone", edtInfo_dialog);
                    user.setUser_phone(edtInfo_dialog);
                } else {
                    Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_LONG).show();
                }
                break;
            case 2:
                if (Zhengzebiaoda.isEmailNO(edtInfo_dialog)) {
                    editor.putString("userEmail", edtInfo_dialog);
                    user.setUser_email(edtInfo_dialog);
                } else {
                    Toast.makeText(getApplicationContext(), "请输入正确的邮箱号", Toast.LENGTH_LONG).show();
                }
                break;
            default:
        }
        editor.commit();//提交更改

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                modifyLooper = Looper.myLooper();
                String msg = null;
                Message msg_handle = new Message();
                if (mc == null) {
                    mc = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
                }
                try {
                    switch (position) {
                        case 0://昵称
                            msg = "<#MODIFYUSERINFO#>" + "nickName" + "|" + user.getUser_id() + "|" + edtInfo_dialog;
                            msg_handle.what = 0;
                            break;
                        case 1://手机号
                            msg = "<#MODIFYUSERINFO#>" + "phone" + "|" + user.getUser_id() + "|" + edtInfo_dialog;
                            msg_handle.what = 1;
                            break;
                        case 2://邮箱号
                            msg = "<#MODIFYUSERINFO#>" + "email" + "|" + user.getUser_id() + "|" + edtInfo_dialog;
                            msg_handle.what = 2;
                            break;
                        default:
                    }
                    mc.dout.writeUTF(msg);//向服务器发送修改的内容
                    String receiveMsg = mc.din.readUTF();//读取返回的消息
                    if (receiveMsg.startsWith("<#MODIFYUSERINFOSUCCESS#>")) {
                        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                        //更新用户信息 ,这部分可以进行再一步的短信或者手机验证
                        Bundle bundle = new Bundle();
                        bundle.putString("dialogInfo", edtInfo_dialog);
                        msg_handle.setData(bundle);
                        userHandler.sendMessage(msg_handle);
                    } else {
                        Toast.makeText(getApplicationContext(), "修改失败，请稍后重试...", Toast.LENGTH_SHORT).show();
                    }
                    Looper.loop();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //在handle中处理UI
    public Handler userHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = new Bundle();
            String updateInfo = bundle.getString("dialogInfo");
            SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            switch (msg.what) {
                case 1://昵称
                    editor.putString("userName", updateInfo);
                    user.setUser_name(updateInfo);
                    break;
                case 2://手机号
                    editor.putString("userPhone", updateInfo);
                    user.setUser_phone(updateInfo);
                    break;
                case 3://邮箱号
                    editor.putString("userEmail", updateInfo);
                    user.setUser_email(updateInfo);
                    break;
                default:
            }
            editor.commit();
        }

    };

    private void logout() {
        //删除本地的用户信息信息
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
//        editor.clear();
        editor.putString("isLogin", NOLOGIN);
        editor.commit();//移除后要提交
        String s = sp.getString("isLogin", LOGIN);
        // 传送未登录状态字给主页面
        SettingActivity.settingActivity.finish();//
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (modifyLooper != null) {
            modifyLooper.quit();
        }
        modifyLooper = null;
        if (mc != null) {
            mc.sayBye();
        }
    }
}
