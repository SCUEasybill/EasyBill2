package xyz.anmai.easybill;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import custom.view.BaseActivity;
import custom.view.GalleryAdapter;

public class CategoryActivity extends BaseActivity {

    public static void activityStart(Context context) {
        Intent intent = new Intent(context, CategoryActivity.class);
        context.startActivity(intent);
    }

    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private TreeMap<Integer, String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryActivity.this.finish();
            }
        });

        initDatas();//初始化数据
        //得到控件
        mRecyclerView = (RecyclerView) findViewById(R.id.category_recyclerview_horizontal);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        mAdapter = new GalleryAdapter(this, mDatas);
        mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(CategoryActivity.this, position + "", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initDatas() {
        mDatas = new TreeMap<Integer, String>();
        mDatas.put(R.drawable.cate_book, "book");
        mDatas.put(R.drawable.cate_clothes, "clothes");
        mDatas.put(R.drawable.cate_commodity, "commodity");
        mDatas.put(R.drawable.cate_communication, "communication");
        mDatas.put(R.drawable.cate_food, "food");
        mDatas.put(R.drawable.cate_gift, "gift");
        mDatas.put(R.drawable.cate_health, "health");
        mDatas.put(R.drawable.cate_milk, "milk");
        mDatas.put(R.drawable.cate_play, "play");
    }
}
