package xyz.anmai.easybill;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import custom.view.BaseActivity;
import custom.view.ListViewCompat;
import custom.view.SlideView;
import util.DBManager;

public class AccountActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, SlideView.OnSlideListener {
    /**
     * 启动activity接口函数
     */
    public static void activityStart(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }

    private static final String TAG = "AccountActivity";
    //自定义listview对象实例
    private ListViewCompat mListView;
    //存储列表item的内容
    private List<AccountItem> mAccountItems = new ArrayList<>();
    //自定义适配器
    private SlideAdapter mSlideAdapter;
    //自定义视图类
    private SlideView mLastSlideViewWithStatusOn;

    //数据库管理对象
    private DBManager dbManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//设置toolbar
        //toolbar返回图标
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        //返回图标点击事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountActivity.this.finish();
            }
        });

        //添加更多按钮
        Button addButton = (Button) findViewById(R.id.account_add_button);
        addButton.setOnClickListener(this);

        dbManager = new DBManager(this);
        if (dbManager.db == null) {
            Log.e(TAG, "连接数据库失败！！！");
        } else {
            Log.e(TAG, "连接数据库成功！");
        }
        mListView = (ListViewCompat) findViewById(R.id.account_listview);

    }

    private void initView() {
        if (dbManager.db != null) {
            mAccountItems.removeAll(mAccountItems);
            Cursor c = null;
            try {
                c = dbManager.db.rawQuery("select * from account", null);
                while (c.moveToNext()) {
                    AccountItem item = new AccountItem();
                    if (c.getString(c.getColumnIndex("cate")).equals("支付宝")) {
                        item.iconRes = R.drawable.alipay;
                        item.account_name = "支付宝";
                    } else if (c.getString(c.getColumnIndex("cate")).equals("VISA")) {
                        item.iconRes = R.drawable.visa;
                        item.account_name = "VISA";
                    } else {
                        item.iconRes = R.mipmap.ic_launcher;
                        item.account_name = c.getString(c.getColumnIndex("cate"));
                    }
                    item.db_id = c.getInt(c.getColumnIndex("id"));
                    item.balance = c.getString(c.getColumnIndex("money"));
                    mAccountItems.add(item);
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }

        //获得适配器对象
        mSlideAdapter = new SlideAdapter();
        //listview设置适配器
        mListView.setAdapter(mSlideAdapter);
        //listveiw设置item的点击事件
        mListView.setOnItemClickListener(this);
    }

    /**
     * 自定义适配器类，继承自BaseAdapter
     */
    public class SlideAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        SlideAdapter() {
            super();
            mInflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mAccountItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mAccountItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //创建自定义的viewholder
            ViewHolder viewHolder;
            SlideView slideView = (SlideView) convertView;
            if (slideView == null) {
                View itemView = mInflater.inflate(R.layout.account_list_item, null);
                slideView = new SlideView(AccountActivity.this);
                slideView.setContentView(itemView);
                viewHolder = new ViewHolder(slideView);
                slideView.setOnSlideListener(AccountActivity.this);
                slideView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) slideView.getTag();
            }
            AccountItem item = mAccountItems.get(position);
            item.slideView = slideView;
            item.slideView.shrink();
            viewHolder.icon.setImageResource(item.iconRes);//设置图标
            viewHolder.account_name.setText(item.account_name);//设置名称
            viewHolder.balance.setText(item.balance);//设置金额
            viewHolder.btn_modify.setOnClickListener(AccountActivity.this);//修改按钮点击事件
            viewHolder.btn_delete.setOnClickListener(AccountActivity.this);//删除按钮点击事件
            return slideView;
        }
    }

    public class AccountItem {
        public int db_id;
        public int iconRes;
        public String account_name;
        public String balance;
        public SlideView slideView;
    }

    /**
     * 自定义viewHolder
     */
    private static class ViewHolder {
        public ImageView icon;
        public TextView account_name;
        public TextView balance;
        public TextView btn_modify;
        public TextView btn_delete;

        ViewHolder(View view) {
            icon = (ImageView) view.findViewById(R.id.account_icon);
            account_name = (TextView) view.findViewById(R.id.account_name);
            balance = (TextView) view.findViewById(R.id.account_balance);
            btn_modify = (TextView) view.findViewById(R.id.holder).findViewById(R.id.modify_account_button);
            btn_delete = (TextView) view.findViewById(R.id.holder).findViewById(R.id.delete_account_button);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e(TAG, "onItemClick position = " + position);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete_account_button) {
            final int position = mListView.getPositionForView(v);
            if (position != ListView.INVALID_POSITION) {
                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                alert = builder.setIcon(R.mipmap.ic_launcher)
                        .setTitle("警告！")
                        .setMessage("确定要删除吗？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(), "删除操作已取消", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                dbManager.db.execSQL("delete from account where id =" + mAccountItems.get(position).db_id);
                                mAccountItems.remove(position);
                                mSlideAdapter.notifyDataSetChanged();
                            }
                        }).create();             //创建AlertDialog对象
                alert.show();
            }
//            Log.e(TAG, "onClick view=" + v + " and delete it.");
        } else if (v.getId() == R.id.modify_account_button) {
            int position = mListView.getPositionForView(v);
            if (position != ListView.INVALID_POSITION) {
                ChangeAccount.activityStart(this, mAccountItems.get(position));
            }
//            Log.e(TAG, "onClick view=" + v + " and modify it.");
        } else if (v.getId() == R.id.account_add_button) {
            ChangeAccount.activityStart(this);
        }
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }
        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //初始化listview数据
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //停止活动时关闭数据库连接
        if (dbManager.db != null) {
            dbManager.close();
            Log.e(TAG, "数据库连接已关闭！");
        }
    }
}
