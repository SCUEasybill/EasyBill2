package com.scu.easybill.login_db;//package com.scu.login_db;
//import static com.scu.login_db.ConstantsUtil.SERVER_ADDRESS;
//import static com.scu.login_db.ConstantsUtil.SERVER_PORT;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Looper;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import java.io.IOException;
//
//import xyz.anmai.easybill.R;
//
///**
// * Created by guyu on 2016/1/29.
// */
//public class Register extends AppCompatActivity {
//
//    MyConnector mc = null;
//    EditText edtName, edtOldPsw, edtPsw1, edtPsw2;
//    String name, oldPsw, psw2, psw1;
//    ProgressDialog pd;
//    Toolbar registerTitlebar;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.login_forget_findpsw);
//
//        //添加titlebar[开始]
//        registerTitlebar = (Toolbar) findViewById(R.id.register_titlebar);
//        registerTitlebar.setTitle("注册");
//        registerTitlebar.setSubtitle("填写注册信息");
//        setSupportActionBar(registerTitlebar);
//        //返回上一层
//        registerTitlebar.setNavigationIcon(R.drawable.back);
//        registerTitlebar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Register.this,Login.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//        registerTitlebar.setOnMenuItemClickListener(onMenuitemClick);
//        //添加titlebar[结束]
//        initView();
//    }
//    private android.support.v7.widget.Toolbar.OnMenuItemClickListener onMenuitemClick = new Toolbar.OnMenuItemClickListener(){
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            String msg = "";
//            switch (menuItem.getItemId()) {
//                case R.id.menu_finish:
//                    msg += "Click edit";
//                    edtName = (EditText) findViewById(R.id.edtAccount);
//                    edtOldPsw = (EditText) findViewById(R.id.edt_oldPsw);
//                    edtPsw1 = (EditText) findViewById(R.id.edt_newPsw1);
//                    edtPsw2 = (EditText)findViewById(R.id.edt_newPsw2);
//
//                    name = edtName.getEditableText().toString().trim();
//                    oldPsw = edtOldPsw.getText().toString().trim();
//                    psw1 = edtPsw1.getText().toString().trim();
//                    psw2 = edtPsw2.getText().toString().trim();
//                    //注册
//                    if(name.equals("") || oldPsw.equals("") || psw1.equals("") || psw2.equals("") ){
//                        Toast.makeText(Register.this, "请将注册信息填写完整", Toast.LENGTH_LONG).show();
//                               break;             }
//                    if(!psw1.equals(psw2)){
//                        Toast.makeText(Register.this,"输入密码不一致",Toast.LENGTH_LONG).show();
//                                  break;          }
//                    registe();
//                default:
//            }
//
//            if(!msg.equals("")) {
//                Toast.makeText(Register.this, msg, Toast.LENGTH_SHORT).show();
//            }
//            return true;
//        }
//    };
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
//        getMenuInflater().inflate(R.menu.menu_titlebar, menu);
//        return true;
//    }
//
//    /**
//     * 连接服务器，进行注册
//     */
//
//    private void registe() {
//        new Thread(){
//            @Override
//            public void run() {
//                Looper.prepare();
//                try {
//                    mc = new MyConnector(SERVER_ADDRESS,SERVER_PORT);
//                    String regInfo = "<#REGISTER#>"+account+"|"+pwd+"|"+email+"|"+age;
//                    pd = ProgressDialog.show(Register.this, "请稍后", "正在注册...", true, true);
//                    mc.dout.writeUTF(regInfo);
//                    String result = mc.din.readUTF();
//                    pd.dismiss();
//                    if(result.startsWith("<#REG_SUCCESS#>")){
//                        result = result.substring(15);
//                        Toast.makeText(Register.this, "注册成功！", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(Register.this,Login.class);
//                        intent.putExtra("account",result);
//                        startActivity(intent);
//                        //跳转到登陆界面，登陆账号（我们分配，还是注册时的用户名登陆）
//                        finish();
//                    }
//                    else{		//注册失败
//                        Toast.makeText(Register.this, "注册失败！请重试！", Toast.LENGTH_LONG).show();
//                        Looper.loop();
//                        Looper.myLooper().quit();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();
//    }
//
//    /**
//     * 初始化界面
//     */
//    private void initView() {
//
//
//        edt_account = (EditText) findViewById(R.id.edtAccount);
//        edt_pwd = (EditText) findViewById(R.id.edtPwd);
//        edt_pwd2 = (EditText) findViewById(R.id.edtPwd2);
//        edt_email = (EditText)findViewById(R.id.edtEmail);
//
//    }
//    /**
//     *方法：断开连接
//     */
//    protected void onDestroy() {
//        if(mc != null){
//            mc.sayBye();
//        }
//        super.onDestroy();
//    }
//}
