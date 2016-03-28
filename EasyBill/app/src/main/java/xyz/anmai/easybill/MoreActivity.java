package xyz.anmai.easybill;

import java.util.ArrayList;

import custom.view.BaseActivity;
import custom.view.Group;
import custom.view.People;
import custom.view.PinnedHeaderExpandableListView;
import custom.view.StickyLayout;
import entity.Bugget;
import entity.Debt;
import entity.Dream;
import entity.Project;
import util.DBManager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends BaseActivity implements
        ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupClickListener,
        PinnedHeaderExpandableListView.OnHeaderUpdateListener,
        StickyLayout.OnGiveUpTouchEventListener {

    private static final String TAG = "MoreActivity";
    //自定义的expandableListView对象
    private PinnedHeaderExpandableListView expandableListView;
    //自定义根布局
    private StickyLayout stickyLayout;
    //父item数据集
    private ArrayList<Group> groupList;
    //子item数据集
    private ArrayList<ArrayList<Object>> childList;
    //自定义适配器
    private MyexpandableListAdapter adapter;
    //数据库管理工具
    private DBManager dbManager = null;

    /**
     * 启动该活动
     *
     * @param context 启动该活动之前的活动的上下文
     */
    public static void activityStart(Context context) {
        Intent intent = new Intent(context, MoreActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar左边返回图标按钮点击事件-销毁活动
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreActivity.this.finish();
            }
        });

        //初始化数据库管理对象，检验连接是否成功
        dbManager = new DBManager(this);
        if (dbManager.db == null) {
            Log.e(TAG, "连接数据库失败！！！");
        } else {
            Log.e(TAG, "连接数据库成功！");
        }

        //获取控件
        expandableListView = (PinnedHeaderExpandableListView) findViewById(R.id.more_activity_expandablelist);
        stickyLayout = (StickyLayout) findViewById(R.id.more_activity_sticky_layout);
        //初始化数据
        initData();
        //创建并设置适配器
        adapter = new MyexpandableListAdapter(this);
        expandableListView.setAdapter(adapter);

        // 展开所有group
        for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
            expandableListView.expandGroup(i);
        }

        //注册各种监听事件
        expandableListView.setOnHeaderUpdateListener(this);
        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupClickListener(this);
        stickyLayout.setOnGiveUpTouchEventListener(this);

    }

    /***
     * InitData
     */
    void initData() {
        //父item默认固定
        groupList = new ArrayList<Group>();
        Group group = null;
        group = new Group();
        group.setTitle("预算资金");
        groupList.add(group);
        group = new Group();
        group.setTitle("债务管理");
        groupList.add(group);
        group = new Group();
        group.setTitle("梦想清单");
        groupList.add(group);
        group = new Group();
        group.setTitle("项目资金");
        groupList.add(group);


        childList = new ArrayList<>();
        for (int i = 0; i < groupList.size(); i++) {
            ArrayList<Object> childTemp;
            if (i == 0) {
                childTemp = new ArrayList<>();
                if (dbManager.db != null) {
                    Cursor c = null;
                    try {
                        c = dbManager.db.rawQuery("select * from bugget where user_id = ?", new String[]{"666666"});
                        while (c.moveToNext()) {
                            Bugget bugget = new Bugget();
                            bugget.set_id(c.getInt(c.getColumnIndex("id")));
                            bugget.setmBegin(c.getString(c.getColumnIndex("begin")));
                            bugget.setmEnd(c.getString(c.getColumnIndex("end")));
                            bugget.setmNum(c.getDouble(c.getColumnIndex("number")));
                            bugget.setmUsed(c.getDouble(c.getColumnIndex("used")));
                            bugget.setmOver(c.getInt(c.getColumnIndex("over")) == 1);
                            childTemp.add(bugget);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    } finally {
                        if (c != null) {
                            c.close();
                        }
                    }
                }
            } else if (i == 1) {
                childTemp = new ArrayList<>();
                if (dbManager.db != null) {
                    Cursor c = null;
                    try {
                        c = dbManager.db.rawQuery("select * from debt where user_id = ?", new String[]{"666666"});
//                        Log.e(TAG, String.valueOf(c.getCount()));
                        while (c.moveToNext()) {
                            Debt debt = new Debt();
//                            Log.e(TAG, c.getString(c.getColumnIndex("begin")));
                            debt.set_id(c.getInt(c.getColumnIndex("id")));
                            debt.setmIo(c.getInt(c.getColumnIndex("io")));
                            debt.setmNum(c.getDouble(c.getColumnIndex("num")));
                            debt.setmWho(c.getString(c.getColumnIndex("who")));
                            debt.setmBegin(c.getString(c.getColumnIndex("begin")));
                            debt.setmEnd(c.getString(c.getColumnIndex("end")));
                            debt.setmAtualEnd(c.getString(c.getColumnIndex("actual_end")));
                            debt.setmOver(c.getInt(c.getColumnIndex("over")));
                            debt.setmRemak(c.getString(c.getColumnIndex("remark")));
                            childTemp.add(debt);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    } finally {
                        if (c != null) {
                            c.close();
                        }
                    }
                }
            } else if (i == 2) {
                childTemp = new ArrayList<>();
                if (dbManager.db != null) {
                    Cursor c = null;
                    try {
                        c = dbManager.db.rawQuery("select * from dream where user_id = ?", new String[]{"666666"});
//                        Log.e(TAG, String.valueOf(c.getCount()));
                        while (c.moveToNext()) {
                            Dream dream = new Dream();
//                            Log.e(TAG, c.getString(c.getColumnIndex("begin")));
                            dream.set_id(c.getInt(c.getColumnIndex("id")));
                            dream.setmName(c.getString(c.getColumnIndex("name")));
                            dream.setmContent(c.getString(c.getColumnIndex("content")));
                            dream.setmNumber(c.getDouble(c.getColumnIndex("number")));
                            dream.setmHaveNum(c.getDouble(c.getColumnIndex("have_money")));
                            dream.setmBegin(c.getString(c.getColumnIndex("begin")));
                            dream.setmEnd(c.getString(c.getColumnIndex("end")));
                            dream.setmDone(c.getInt(c.getColumnIndex("done")) == 1);
                            childTemp.add(dream);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    } finally {
                        if (c != null) {
                            c.close();
                        }
                    }
                }
            } else {
                childTemp = new ArrayList<>();
                if (dbManager.db != null) {
                    Cursor c = null;
                    try {
                        c = dbManager.db.rawQuery("select * from project where user_id = ?", new String[]{"666666"});
//                        Log.e(TAG, String.valueOf(c.getCount()));
                        while (c.moveToNext()) {
                            Project project = new Project();
//                            Log.e(TAG, c.getString(c.getColumnIndex("begin")));
                            project.set_id(c.getInt(c.getColumnIndex("id")));
                            project.setmName(c.getString(c.getColumnIndex("name")));
                            project.setmNumber(c.getDouble(c.getColumnIndex("number")));
                            project.setmLeaving(c.getDouble(c.getColumnIndex("leaving")));
                            project.setmBegin(c.getString(c.getColumnIndex("begin")));
                            project.setmEnd(c.getString(c.getColumnIndex("end")));
                            project.setmDone(c.getInt(c.getColumnIndex("done")) == 1);
                            project.setmRemark(c.getString(c.getColumnIndex("remark")));
                            childTemp.add(project);
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
            childList.add(childTemp);
        }
    }

    /***
     * 数据源
     */
    class MyexpandableListAdapter extends BaseExpandableListAdapter {
        private Context context;
        private LayoutInflater inflater;

        public MyexpandableListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        // 返回父列表个数
        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        // 返回子列表个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return childList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {

            return groupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder groupHolder = null;
            if (convertView == null) {
                groupHolder = new GroupHolder();
                convertView = inflater.inflate(R.layout.more_elv_group, null);
                groupHolder.textView = (TextView) convertView
                        .findViewById(R.id.more_group_textview);
                groupHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.more_group_imageview);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }

            groupHolder.textView.setText(((Group) getGroup(groupPosition))
                    .getTitle());
            if (isExpanded)// ture则表示已展开，false表示关闭
                groupHolder.imageView.setImageResource(R.drawable.expanded);
            else
                groupHolder.imageView.setImageResource(R.drawable.collapse);
            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder childHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.more_elv_child, null);
                childHolder = new ChildHolder(convertView);
                convertView.setTag(childHolder);
            } else {
                childHolder = (ChildHolder) convertView.getTag();
            }
            if (groupPosition == 0) {
                childHolder.text1.setText(((Bugget) getChild(groupPosition,
                        childPosition)).getmEnd());
                childHolder.text2.setText(String.valueOf(((Bugget) getChild(groupPosition,
                        childPosition)).getmNum()));
                childHolder.text3.setText(String.valueOf(((Bugget) getChild(groupPosition,
                        childPosition)).getmUsed()));
                childHolder.text4.setText(((Bugget) getChild(groupPosition,
                        childPosition)).ismOver() ? "超出" : "有余");
            } else if (groupPosition == 1) {
                childHolder.text1.setText((((Debt) getChild(groupPosition,
                        childPosition)).getmIo()) == 0 ? "借出" : "借入");
                childHolder.text2.setText(String.valueOf(((Debt) getChild(groupPosition,
                        childPosition)).getmNum()));
                childHolder.text3.setText(((Debt) getChild(groupPosition,
                        childPosition)).getmWho());
                childHolder.text4.setText((((Debt) getChild(groupPosition,
                        childPosition)).getmOver()) == 0 ? "未结" : "完成");
            } else if (groupPosition == 2) {
                childHolder.text1.setText(((Dream) getChild(groupPosition,
                        childPosition)).getmName());
                childHolder.text2.setText(((Dream) getChild(
                        groupPosition, childPosition)).getmEnd());
                childHolder.text3.setText(String.valueOf(((Dream) getChild(groupPosition,
                        childPosition)).getmNumber() - ((Dream) getChild(groupPosition,
                        childPosition)).getmHaveNum()));
                childHolder.text4.setText(((Dream) getChild(groupPosition,
                        childPosition)).ismDone() ? "已足够" : "尚不足");
            } else if (groupPosition == 3) {
                childHolder.text1.setText(((Project) getChild(groupPosition,
                        childPosition)).getmName());
                childHolder.text2.setText(((Project) getChild(
                        groupPosition, childPosition)).getmEnd());
                childHolder.text3.setText(String.valueOf(((Project) getChild(groupPosition,
                        childPosition)).getmLeaving()));
                childHolder.text4.setText(((Project) getChild(groupPosition,
                        childPosition)).ismDone() ? "已完结" : "进行中");
            } else {
                Log.e(TAG, "bad childitem!");
            }
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            Log.e(TAG, "isChildSelectable=" + groupPosition + "->" + childPosition);
            return true;
        }
    }

    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v,
                                int groupPosition, final long id) {
        Log.e(TAG, "onGroupClick=" + groupPosition);
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {

        Log.e(TAG, "onChildClick=" + groupPosition + "->" + childPosition);

        return false;
    }

    class GroupHolder {
        TextView textView;
        ImageView imageView;
    }

    class ChildHolder {
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;

        public ChildHolder(View view) {
            text1 = (TextView) view.findViewById(R.id.more_child_1);
            text2 = (TextView) view.findViewById(R.id.more_child_2);
            text3 = (TextView) view.findViewById(R.id.more_child_3);
            text4 = (TextView) view.findViewById(R.id.more_child_4);
        }
    }

    @Override
    public View getPinnedHeader() {
        View mHeaderView = getLayoutInflater().inflate(R.layout.more_elv_group, null);
        mHeaderView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return mHeaderView;
    }

    @Override
    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
        Group firstVisibleGroup = (Group) adapter.getGroup(firstVisibleGroupPos);
        TextView textView = (TextView) headerView.findViewById(R.id.more_group_textview);
        textView.setText(firstVisibleGroup.getTitle());
    }

    @Override
    public boolean giveUpTouchEvent(MotionEvent event) {
        if (expandableListView.getFirstVisiblePosition() == 0) {
            View view = expandableListView.getChildAt(0);
            if (view != null && view.getTop() >= 0) {
                return true;
            }
        }
        return false;
    }

}
