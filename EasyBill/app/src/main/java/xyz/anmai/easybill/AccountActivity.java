package xyz.anmai.easybill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import custom.view.BaseActivity;
import custom.view.ListViewCompat;
import custom.view.SlideView;

public class AccountActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener, SlideView.OnSlideListener {

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, AccountActivity.class);
        context.startActivity(intent);
    }

    private static final String TAG = "AccountActivity";
    private ListViewCompat mListView;
    private List<AccountItem> mAccountItems = new ArrayList<AccountItem>();
    private SlideAdapter mSlideAdapter;
    private SlideView mLastSlideViewWithStatusOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountActivity.this.finish();
            }
        });

        initView();
    }

    private void initView() {
        mListView = (ListViewCompat) findViewById(R.id.account_listview);

        for (int i = 0; i <5; i++){
            AccountItem item = new AccountItem();
            if (i%3 == 0) {
                item.iconRes = R.drawable.visa;
                item.account_name = "VISA";
                item.balance = "1001元";
                Log.e(TAG,"visa");
            } else {
                item.iconRes = R.drawable.alipay;
                item.account_name = "支付宝";
                item.balance = "699元";
                Log.e(TAG,"alipay");
            }
            mAccountItems.add(item);
        }
        mSlideAdapter = new SlideAdapter();
        mListView.setAdapter(mSlideAdapter);
        mListView.setOnItemClickListener(this);
    }

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
            ViewHolder viewHolder;
            SlideView slideView = (SlideView)convertView;
            if (slideView == null){
                View itemView = mInflater.inflate(R.layout.account_list_item, null);
                slideView = new SlideView(AccountActivity.this);
                slideView.setContentView(itemView);
                viewHolder = new ViewHolder(slideView);
                slideView.setOnSlideListener(AccountActivity.this);
                slideView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder)slideView.getTag();
            }
            AccountItem item = mAccountItems.get(position);
            item.slideView = slideView;
            item.slideView.shrink();

            viewHolder.icon.setImageResource(item.iconRes);
            viewHolder.account_name.setText(item.account_name);
            viewHolder.balance.setText(item.balance);
            viewHolder.btn_modify.setOnClickListener(AccountActivity.this);
            viewHolder.btn_delete.setOnClickListener(AccountActivity.this);

            return slideView;
        }
    }

    public class AccountItem {
        public int iconRes;
        public String account_name;
        public String balance;
        public SlideView slideView;
    }

    private static class ViewHolder {
        public ImageView icon;
        public TextView account_name;
        public TextView balance;
        public TextView btn_modify;
        public TextView btn_delete;

        ViewHolder(View view){
            icon = (ImageView)view.findViewById(R.id.account_icon);
            account_name = (TextView)view.findViewById(R.id.account_name);
            balance = (TextView)view.findViewById(R.id.account_balance);
            btn_modify = (TextView)view.findViewById(R.id.holder).findViewById(R.id.modify_account_button);
            btn_delete = (TextView)view.findViewById(R.id.holder).findViewById(R.id.delete_account_button);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.e(TAG, "onItemClick position = " + position);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete_account_button) {
            int position  = mListView.getPositionForView(v);
            if (position != ListView.INVALID_POSITION){
                mAccountItems.remove(position);
                mSlideAdapter.notifyDataSetChanged();
            }
            Log.e(TAG, "onClick view=" + v + " and delete it.");
        }
        else if (v.getId() == R.id.modify_account_button) {
            int position  = mListView.getPositionForView(v);
            if (position != ListView.INVALID_POSITION){
                Toast.makeText(this, "您点击了修改！", Toast.LENGTH_SHORT).show();
            }
            Log.e(TAG, "onClick view=" + v + " and modify it.");
        }
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view){
            mLastSlideViewWithStatusOn.shrink();
        }
        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }
}
