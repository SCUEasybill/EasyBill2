package xyz.anmai.easybill;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.scu.easybill.login_db.Login;
import com.scu.easybill.login_db.SelectPicPopupWindow;
import com.scu.easybill.login_db.UserInfo;
import com.scu.easybill.login_db.UserUtils;
import com.scu.easybill.login_db.Users;
import com.scu.easybill.utils.FileUtil;
import com.scu.easybill.utils.MyConnector;
import com.scu.easybill.utils.getNetworkState;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import custom.view.BaseActivity;
import custom.view.RoundImageView;
import scu.function.ExportExcel;
import scu.function.ImportExecl;

import static com.scu.easybill.utils.ConstantsUtil.NOLOGIN;
import static com.scu.easybill.utils.ConstantsUtil.SERVER_ADDRESS;
import static com.scu.easybill.utils.ConstantsUtil.SERVER_PORT;

public class SettingActivity extends BaseActivity {
    private static Context mContext;
    //头像
    RoundImageView riv_head_portrait;
    //    private CircleImg avatarImg;// 头像图片
    private Button loginBtn;// 页面的登录按钮
    private SelectPicPopupWindow menuWindow; // 自定义的头像编辑弹出框
    // 上传服务器的路径【一般不硬编码到程序中】
    private String imgUrl = "";
    private static final String IMAGE_FILE_NAME = "avatarImage.jpg";// 头像文件名称
    private String urlpath;            // 图片本地路径
    private String resultStr = "";    // 服务端返回结果集
    private static ProgressDialog pd;// 等待进度圈
    private static final int REQUESTCODE_PICK = 0;        // 相册选图标记
    private static final int REQUESTCODE_TAKE = 1;        // 相机拍照标记
    private static final int REQUESTCODE_CUTTING = 2;    // 图片裁切标记
    MyConnector mConnection = null;
    Users user;
    String isLogin = null;
    public static SettingActivity settingActivity;
    boolean netState = false;//是否联网
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        settingActivity = this;
        mContext = this.getApplicationContext();
        getAppState();
        netState = getNetworkState.isNetworkConnected(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final LinearLayout ImportDate = (LinearLayout) findViewById(R.id.ImportDate);
        LinearLayout ExportDate = (LinearLayout) findViewById(R.id.ExportDate);

        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingActivity.this.finish();
            }
        });

        //测试数据
        final String as[] = {"流波1", "刘波2", "老板3", "猎豹4", "了吧5", "老班6", "类别7", "里吧8", "来吧9", "列表10", "聊吧11", "六百12"};
        final ArrayList<String[]> strings = new ArrayList<String[]>();
        for (int i = 0; i < 100; i++) {
            String a[] = new String[12];
            for (int j = 0; j < 12; j++) {
                a[j] = "a" + " " + i + " " + j + " " + (char) ('a' + i);
            }
            strings.add(a);
        }

        ImportDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new ExportExcel()).ExportDate(strings, as, "EasyBill");
                Toast.makeText(ImportDate.getContext(), "导出成功", Toast.LENGTH_LONG);
            }
        });

        ExportDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, ImportExecl.class);
                startActivity(intent);
                Log.e("liubo", "ExportDate successed!!!!");
            }
        });


//头像操作
        riv_head_portrait = (RoundImageView) findViewById(R.id.setting_activity_head_portrait);
        riv_head_portrait.setImageResource(R.drawable.header);
        Drawable drawable = UserUtils.updatePortrait();//        更新头像信息
        if (drawable != null) {
            riv_head_portrait.setImageDrawable(drawable);
        }
        riv_head_portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳出选项框，选择拍照或者从相册选择
                menuWindow = new SelectPicPopupWindow(SettingActivity.this, itemsOnClick);
                menuWindow.showAtLocation(findViewById(R.id.setting_layout),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        //个人信息
        TextView tvUserInfo = (TextView) findViewById(R.id.tv_userInfo);
        tvUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserInfo.class);
                startActivity(intent);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void getAppState() {
        user = new Users();
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        user.setUser_id(sp.getString("userId", "请上网"));
        user.setUser_name(sp.getString("userName", "请上网"));
        user.setUser_email(sp.getString("userEmail", "请上网"));
        user.setUser_phone(sp.getString("userPhone", "请上网"));
        user.setUser_total_money(sp.getInt("userTotalMoney", 0));
        isLogin = sp.getString("isLogin", NOLOGIN);
    }

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                // 拍照
                case R.id.btn_takePhoto:
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //下面这句指定调用相机拍照后的照片存储的路径
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME)));
                    startActivityForResult(takeIntent, REQUESTCODE_TAKE);
                    break;
                // 相册选择图片
                case R.id.btn_pickPhoto:
                    Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                    // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                    pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpg");
                    startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE_PICK:// 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();// 用户点击取消操作
                }
                break;
            case REQUESTCODE_TAKE:// 调用相机拍照
                File temp = new File(Environment.getExternalStorageDirectory() + "/" + IMAGE_FILE_NAME);
                startPhotoZoom(Uri.fromFile(temp));
                break;
            case REQUESTCODE_CUTTING:// 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路显示
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            urlpath = FileUtil.saveFile(SettingActivity.this, "temphead.jpg", photo);//获得图像在本地的位置信息径做
            // 新线程后台上传服务端
            pd = ProgressDialog.show(SettingActivity.this, null, "正在上传图片，请稍候...", true, true);
            uploadImage(urlpath);
            Toast.makeText(mContext, "裁剪后的图片", Toast.LENGTH_LONG).show();
            pd.dismiss();
            riv_head_portrait.setImageDrawable(drawable);//在本地设置图像
        }
    }

    public void uploadImage(final String urlpath) {
        final String BUFF = "--";
        new Thread() {
            @Override
            public void run() {
                if (mConnection == null) {
                    mConnection = new MyConnector(SERVER_ADDRESS, SERVER_PORT);
                }
                try {
                    File file = new File(urlpath);
                    FileInputStream fStream = new FileInputStream(file);
                    String[] fileEnd = file.getName().split("\\.");
                    String sendMsg = "<#UPLOADPORTRAIT#>" + user.getUser_id() + BUFF + fileEnd[fileEnd.length - 1].toString();
                    mConnection.dout.writeUTF(sendMsg);
                    //每次写入102400
                    int bufferSize = 1024 * 70;
                    byte[] buffer = new byte[bufferSize];
                    int length = 0;
                    // 从文件读取数据至缓冲区(值为-1说明已经读完)
                    while ((length = fStream.read(buffer)) != -1) {
                        mConnection.dout.write(buffer, 0, length);
                    }
                    // 一定要加上这句，否则收不到来自服务器端的消息返回
//                    socket.shutdownOutput();
                    //关闭输出流
                    mConnection.dout.flush();
                    fStream.close();
                    String recvMsg = mConnection.din.readUTF();
                    Message msg = new Message();
                    if (recvMsg.startsWith("<#UPDATEPORTRAITSUCCESS#>")) {
                        msg.what = 0;
                    } else {
                        msg.what = -1;
                    }
                    myHandler.sendMessage(msg);// 执行耗时的方法之后发送消给handler
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pd.dismiss();
                    Toast.makeText(mContext, "上传成功！", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();
                    break;
                case -1:
                    break;
                default:

            }

        }
    };
    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    pd.dismiss();
                    try {
                        // 返回数据示例，根据需求和后台数据灵活处理
                        // {"status":"1","statusMessage":"上传成功","imageUrl":"http://120.24.219.49/726287_temphead.jpg"}
                        JSONObject jsonObject = new JSONObject(resultStr);
                        // 服务端以字符串“1”作为操作成功标记
                        if (jsonObject.optString("status").equals("1")) {
                            BitmapFactory.Options option = new BitmapFactory.Options();
                            // 压缩图片:表示缩略图大小为原始图片大小的几分之一，1为原图，3为三分之一
                            option.inSampleSize = 1;
                            // 服务端返回的JsonObject对象中提取到图片的网络URL路径
                            String imageUrl = jsonObject.optString("imageUrl");
                            Toast.makeText(mContext, imageUrl, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, jsonObject.optString("statusMessage"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnection != null) {
            mConnection.sayBye();
        }
        mConnection = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Setting Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://xyz.anmai.easybill/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Setting Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://xyz.anmai.easybill/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
