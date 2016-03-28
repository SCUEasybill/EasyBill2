package xyz.anmai.easybill;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TreeMap;

import custom.view.BaseActivity;
import custom.view.GalleryAdapter;
import util.DBManager;

public class CategoryActivity extends BaseActivity {

    /**
     * 启动activity
     * @param context 启动新活动之前的活动上下文
     */
    public static void activityStart(Context context) {
        Intent intent = new Intent(context, CategoryActivity.class);
        context.startActivity(intent);
    }

    private final static String TAG = "CategoryActivity";

    //recyclerView对象
    private RecyclerView mRecyclerView;
    //自定义recyclerView的适配器
    private GalleryAdapter mAdapter;
    //初始化数据的treeMap
    private TreeMap<Integer, String> mDatas;
    //数据库管理对象
    private DBManager dbManager = null;
    //对话框对象
    private AlertDialog alert = null;
    //创建对话框对象
    private AlertDialog.Builder builder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //设置toolbar返回图标点击事件-关闭活动
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryActivity.this.finish();
            }
        });

        //初始化数据库管理对象，检验连接是否成功
        dbManager = new DBManager(this);
        if (dbManager.db == null) {
            Log.e(TAG, "连接数据库失败！！！");
        } else {
            Log.e(TAG, "连接数据库成功！");
        }
        //初始化数据
        initDatas();
        //得到RecyclerView控件
        mRecyclerView = (RecyclerView) findViewById(R.id.category_recyclerview_horizontal);
        //为RecyclerView设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //为RecyclerView设置适配器
        mAdapter = new GalleryAdapter(this, mDatas);
        //为适配器注册点击事件
        mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
            @Override
            //点击事件响应
            public void onItemClick(View view, int position) {
//                Toast.makeText(CategoryActivity.this, position + "", Toast.LENGTH_SHORT).show();
                if (dbManager.db != null) {
                    Cursor c = null;
                    try {
                        //开始数据库事务
                        dbManager.db.beginTransaction();
                        //查询宫格中的当前布局情况
                        c = dbManager.db.rawQuery("select * from label where where_in_main <> ? order by where_in_main", new String[]{"0"});
//                        Log.e(TAG, "c.getCount()=" + c.getCount());
                        //如果宫格中不足12个，则可以添加到宫格中
                        if (c.getCount() < 12) {
                            for (int i = 1; i <= 12; i++) {//遍历看从那个宫格开始是空的
                                if (dbManager.db.rawQuery("select * from label where where_in_main = ?", new String[]{String.valueOf(i)}).moveToNext()) {
                                } else {
//                                    ImageView imageView = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
                                    //gallery中正在点击的textview对象
                                    TextView textView = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
//                                    Log.e(TAG, "textView.gettext()=" + textView.getText().toString());
                                    //将添加到的位置更新写入数据库中
                                    dbManager.db.execSQL("update label set where_in_main = ? where name = ?", new String[]{String.valueOf(i), textView.getText().toString()});
                                    //宫格中的i位置处的textview
                                    TextView tv = (TextView) findViewById(getResources().getIdentifier("cate_" + i, "id", getPackageName()));
                                    //设置宫格处显示的name
                                    tv.setText(textView.getText().toString());
                                    //下面是设置图标
                                    c = dbManager.db.rawQuery("select * from label where name = ?", new String[]{textView.getText().toString()});
                                    Drawable topDrawable = null;
                                    if (c.moveToNext()) {
                                        topDrawable = getResources().getDrawable(getResources().getIdentifier(c.getString(c.getColumnIndex("ico")), "drawable", getPackageName()));
                                    }
                                    assert topDrawable != null;
                                    topDrawable.setBounds(0, 0, 125, 125);//单位是dp
                                    tv.setCompoundDrawables(null, topDrawable, null, null);
                                    //从mDatas中去除已经加到宫格中的label
                                    mDatas.remove(getResources().getIdentifier(c.getString(c.getColumnIndex("ico")), "drawable", getPackageName()));
                                    mAdapter.notifyDataSetChanged();
                                    //设置事务成功
                                    dbManager.db.setTransactionSuccessful();
                                    break;//跳出循环
                                }
                            }
                        }
                    } catch (Exception e) {
                        //打印错误
                        Log.e(TAG, e.toString());
                    } finally {
                        //结束事务
                        dbManager.db.endTransaction();
                        if (c != null) {//如果不为空，关闭cursor
                            c.close();
                        }
                    }
                }
            }

            @Override
            //长按事件响应
            public void onItemLongClick(final View view, final int position) {
                alert = null;
                builder = new AlertDialog.Builder(CategoryActivity.this);
                alert = builder.setIcon(R.mipmap.ic_launcher)//设置对话框图标
                        .setTitle("删除")//对话框标题
                        .setMessage("确认要删除吗？")//对话框内容
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确认按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {//确认按钮点击操作
                                if (dbManager.db != null) {
                                    Cursor c = null;
                                    try {
                                        dbManager.db.beginTransaction();
                                        //获取当前点击的textview
                                        TextView textView = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
                                        //下面是从mdatas中取出该label
                                        c = dbManager.db.rawQuery("select * from label where name = ?", new String[]{textView.getText().toString()});
                                        if (c.moveToNext()) {
                                            mDatas.remove(getResources().getIdentifier(c.getString(c.getColumnIndex("ico")), "drawable", getPackageName()));
                                        }
                                        //从数据库中删除该label
                                        dbManager.db.execSQL("delete from label where name = ?", new Object[]{textView.getText().toString()});
                                        mAdapter.notifyDataSetChanged();
                                        dbManager.db.setTransactionSuccessful();
                                        Log.e(TAG, "从数据库删除成功！");
                                        Toast.makeText(CategoryActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        Log.e(TAG, e.toString());
                                        Log.e(TAG, "从数据库删除失败！！！");
                                    } finally {
                                        dbManager.db.endTransaction();
                                        if (c != null) {
                                            c.close();
                                        }
                                    }
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {//设置取消按钮
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(CategoryActivity.this, "取消删除", Toast.LENGTH_SHORT).show();
                            }
                        }).create();             //创建AlertDialog对象
                alert.show();
            }
        });
        //recyclerView设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 初始化数据
     */
    private void initDatas() {
        mDatas = new TreeMap<>();
        if (dbManager.db != null) {
            Cursor c = null;
            try {
                //从数据库中查询出在宫格中的内容即where_in_main!=0
                c = dbManager.db.rawQuery("select * from label where where_in_main <> ? order by where_in_main", new String[]{"0"});
                if (c.getCount() > 12) {//宫格最多12个
                    throw new Exception("数据表label中的数据超出限制");
                } else {
//                    TableLayout layout = (TableLayout) findViewById(R.id.category_tablelayout);
                    for (int i; c.moveToNext(); ) {
                        //获得在宫格中的位置，记为i
                        i = c.getInt(c.getColumnIndex("where_in_main"));
                        //根据位置获得对应位置的textView
                        TextView tv = (TextView) findViewById(getResources().getIdentifier("cate_" + i, "id", getPackageName()));
                        //设置text的文字内容即为label的name
                        tv.setText(c.getString(c.getColumnIndex("name")));
                        //下面设置textview的drawableTop图标内容
                        Drawable topDrawable = getResources().getDrawable(getResources().getIdentifier(c.getString(c.getColumnIndex("ico")), "drawable", getPackageName()));
                        assert topDrawable != null;
                        topDrawable.setBounds(0, 0, 125, 125);
                        tv.setCompoundDrawables(null, topDrawable, null, null);
                    }
                }
                //找出不在宫格中的即未添加的label保存到mDatas
                c = dbManager.db.rawQuery("select * from label where where_in_main = ?", new String[]{"0"});
                while (c.moveToNext()) {
                    mDatas.put(getResources().getIdentifier(c.getString(c.getColumnIndex("ico")), "drawable", getPackageName()), c.getString(c.getColumnIndex("name")));
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }
    }

    //响应宫格中textView的点击事件
    public void onClickTV(View v) {
        //获得被点击的textView
        TextView tv = (TextView) findViewById(v.getId());
        //获得标签的name
        String name = tv.getText().toString();
        //确保被点击处的textview有内容
        if (dbManager.db != null && !name.equals("空") && !name.equals("1111")) {
            Cursor c = null;
            try {
                dbManager.db.beginTransaction();
                //更改数据库，让当前label变为未添加，即where_in_main设置为0
                dbManager.db.execSQL("update label set where_in_main = ? where name = ?", new String[]{"0", name});
                //下面是将当前这个label加入到mDatas中
                c = dbManager.db.rawQuery("select * from label where name = ?", new String[]{name});
                if (c.moveToNext()) {
                    mDatas.put(getResources().getIdentifier(c.getString(c.getColumnIndex("ico")), "drawable", getPackageName()), name);
                }
                //宫格中显示空
                tv.setText("空");
                tv.setCompoundDrawables(null, null, null, null);
                mAdapter.notifyDataSetChanged();
                dbManager.db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            } finally {
                dbManager.db.endTransaction();
                if (c != null) {
                    c.close();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 设置菜单，那个添加图标按钮
        getMenuInflater().inflate(R.menu.cate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //点击添加按钮启动新活动
        if (id == R.id.cate_action_add) {
            AddCategory.activityStart(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //重新设置mDatas的值，实现刷新效果
        if (dbManager.db != null) {
            Cursor c = null;
            try {
                c = dbManager.db.rawQuery("select * from label where where_in_main = ?", new String[]{"0"});
                while (c.moveToNext()) {
                    mDatas.put(getResources().getIdentifier(c.getString(c.getColumnIndex("ico")), "drawable", getPackageName()), c.getString(c.getColumnIndex("name")));
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }
        mAdapter.notifyDataSetChanged();
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
