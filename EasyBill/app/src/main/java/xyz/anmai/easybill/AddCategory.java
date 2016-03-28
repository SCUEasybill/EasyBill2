package xyz.anmai.easybill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import util.DBManager;
import util.DataCollection;
import util.MyAdapter;

public class AddCategory extends AppCompatActivity {
    //启动这个活动
    public static void activityStart(Context context) {
        Intent intent = new Intent(context, AddCategory.class);
        context.startActivity(intent);
    }

    private final static String TAG = "AddCategory";

    //对话框对象
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    //属性spinner
    private Spinner attriSpinner = null;
    //图标spinner
    private Spinner icoSpinner = null;
    //属性spinner中item的text
    private String attribute = null;
    //图标spinner中item的图标编号
    private int icon = 0;
    //label的名称
    private String labelName = null;
    //label名称的editText对象
    private EditText name = null;
    //图标编号存储表
    HashMap<Integer, String> hashMap = null;
    //数据库管理对象
    private DBManager dbManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        dbManager = new DBManager(this);
        if (dbManager.db == null) {
            Log.e(TAG, "连接数据库失败！！！");
        } else {
            Log.e(TAG, "连接数据库成功！");
        }
        //获得控件
        attriSpinner = (Spinner) findViewById(R.id.cate_spinner_add_attribute);
        icoSpinner = (Spinner) findViewById(R.id.cate_spinner_add_ico);
        name = (EditText) findViewById(R.id.cate_add_name);
        Button commit = (Button) findViewById(R.id.add_label_commit_button);

        //初始化数据
        initDatas();

        //提交按钮点击事件
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得editText中的值
                labelName = name.getText().toString();
                Log.e(TAG, "attribute=" + attribute + ";ico=" + icon + ";labelName=" + labelName);
                //判断是否有未填项目
                if (attribute != null && icon != 0 && !labelName.isEmpty()) {
                    //0为支出，1为收入
                    final int io = attribute.equals("支出") ? 0 : 1;
                    //从存储表中取出图标名字
                    final String mIcon = hashMap.get(icon);
                    //下面创建对话框
                    alert = null;
                    builder = new AlertDialog.Builder(AddCategory.this);
                    alert = builder.setIcon(R.mipmap.ic_launcher)
                            .setTitle("注意")
                            .setMessage("是否确认添加？！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override//点击确定按钮时开始添加
                                public void onClick(DialogInterface dialog, int which) {
                                    if (dbManager.db != null) {
                                        try {
                                            Log.e(TAG, "准备添加数据：【null, '666666', " + io + ", 0, " + mIcon + ", " + labelName + "】");
                                            dbManager.db.execSQL("insert into label values(?,?,?,?,?,?)", new Object[]{null, "666666", io, "0", mIcon, labelName});
                                            Log.e(TAG, "添加到数据库成功！");
                                            Toast.makeText(AddCategory.this, "添加成功！", Toast.LENGTH_SHORT).show();
                                            AddCategory.this.finish();//添加成功关闭活动
                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                            Log.e(TAG, "更新数据库失败！！！");
                                        }
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(AddCategory.this, "取消添加", Toast.LENGTH_SHORT).show();
                                    AddCategory.this.finish();//取消时关闭活动
                                }
                            }).create();             //创建AlertDialog对象
                    alert.show();
                } else {//信息不完整提示继续填写
                    alert = null;
                    builder = new AlertDialog.Builder(AddCategory.this);
                    alert = builder.setIcon(R.mipmap.ic_launcher)
                            .setTitle("对不起")
                            .setMessage("请将信息填写完整！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //nothing
                                }
                            }).create();
                    alert.show();
                }
            }
        });
    }

    private void initDatas() {
        //属性spinner的选项
        ArrayList<String> attriData = new ArrayList<>();
        attriData.add("支出");
        attriData.add("收入");

        //图标spinner的选项
        ArrayList<Integer> icoData = new ArrayList<>();
        hashMap = new DataCollection().getCategory();
        for (int i = 1; i <= 15; i++) {
            icoData.add(getResources().getIdentifier(hashMap.get(i), "drawable", getPackageName()));
        }

        //调用可复用adapter初始化属性spinner的适配器
        MyAdapter attriAdapter = new MyAdapter<String>(attriData, R.layout.item_spin_cate_attr) {
            @Override
            public void bindView(ViewHolder holder, String obj) {
                holder.setText(R.id.attri_text, obj);
            }
        };
        //调用可复用adapter初始化图标spinner的适配器
        MyAdapter icoAdapter = new MyAdapter<Integer>(icoData, R.layout.item_spin_cate_ico) {
            @Override
            public void bindView(ViewHolder holder, Integer obj) {
                holder.setImageResource(R.id.img_ico, obj);
            }
        };

        //设置适配器
        attriSpinner.setAdapter(attriAdapter);
        icoSpinner.setAdapter(icoAdapter);

        //重写item点击事件
        attriSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                attribute = ((TextView) findViewById(R.id.attri_text)).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        icoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                icon = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁活动时关闭数据库连接
        if (dbManager.db != null) {
            dbManager.close();
            Log.e(TAG, "数据库连接已关闭！");
        }
    }
}
