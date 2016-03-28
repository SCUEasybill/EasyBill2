package xyz.anmai.easybill;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.scu.easybill.login_db.Login;
import com.scu.easybill.login_db.UserInfo;
import com.scu.easybill.login_db.UserUtils;
import com.scu.easybill.login_db.Users;

import java.util.ArrayList;
import java.util.List;
import custom.view.BaseActivity;
import util.DBManager;

import static com.scu.easybill.utils.ConstantsUtil.LOGIN;
import static com.scu.easybill.utils.ConstantsUtil.NOLOGIN;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction = null;
    private List<Fragment> fragments = new ArrayList<>();
    private MainFragment mainFragment = null;
    private DetailsFragment detailsFragment = null;
    private ReportFragment reportFragment = null;
    Users user;
    String isLogin = null;
    ImageView imageView_header; // 侧滑头像
    public static Context context;
    private DBManager dbManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//加载主布局
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this.getApplicationContext();
        getAppState();//初始化APP，得到登录状态
        init();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//nav-header-main,侧滑头像
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        imageView_header = (ImageView) headerView.findViewById(R.id.imv_header_main);
        //如果还没有登录过，显示默认头像，登录过，则更新头像
        imageView_header.setImageResource(R.drawable.header);
        Drawable drawable = UserUtils.updatePortrait();//        更新头像信息
        if (drawable != null) {
            imageView_header.setImageDrawable(drawable);
        }
        imageView_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "头像点击", Toast.LENGTH_LONG).show();
                if (isLogin.equals(NOLOGIN)) {//未登陆，跳转到登陆界面
                    Intent intent = new Intent(MainActivity.this, Login.class);
                    startActivity(intent);
                }
                if (isLogin.equals(LOGIN)) {//已经登陆，跳转到个人信息界面
//                    user = new Users("1", "guyu", "13678109397", null, 0, null);
                    Intent intent = new Intent(getApplicationContext(), UserInfo.class);
//                    intent.putExtra("user", user);
//                    intent.putExtra("islogin", isLogin);
                    startActivity(intent);
                }
            }
        });
        dbManager = new DBManager(this);
        if (dbManager.db != null) {
            Log.e(TAG, "连接数据库成功！");
            Cursor c = null;
            try {
                c = dbManager.db.rawQuery("select * from user", null);
                if (c.moveToNext()) {
                    Log.e(TAG, "当前登陆用户id：" + c.getInt(c.getColumnIndex("id")));
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            } finally {
                if (c != null) {
                    c.close();
                }
            }
        }else {
            Log.e(TAG, "连接数据库失败");
        }

        mainFragment = new MainFragment();
        detailsFragment = new DetailsFragment();
        reportFragment = new ReportFragment();
        fragments.add(detailsFragment);
        fragments.add(mainFragment);
        fragments.add(reportFragment);

        fragmentManager = getFragmentManager();
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_tab);
        final RadioButton mainRadioButton = (RadioButton) findViewById(R.id.radiobutton_main);
        final RadioButton detailsRadioButton = (RadioButton) findViewById(R.id.radiobutton_details);
        final RadioButton reportRadioButton = (RadioButton) findViewById(R.id.radiobutton_report);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.e("Main","checkedid="+checkedId);
                switch (checkedId) {
                    case R.id.radiobutton_main:
                        mainRadioButton.setChecked(true);
                        switchFragment(1);
                        break;
                    case R.id.radiobutton_details:
                        detailsRadioButton.setChecked(true);
                        switchFragment(0);
                        break;
                    case R.id.radiobutton_report:
                        reportRadioButton.setChecked(true);
                        switchFragment(2);
                        break;
                }
            }
        });
        if (!mainRadioButton.isChecked()) {
            mainRadioButton.setChecked(true);
        }
        transaction = fragmentManager.beginTransaction();
        if (transaction.isEmpty()) {
            for (Fragment fragment : fragments) {
                transaction.add(R.id.main_content, fragment);
            }
        }
        transaction.show(mainFragment).hide(detailsFragment).hide(reportFragment);
        transaction.commitAllowingStateLoss();
    }
    //获取界面中的组件
    private void init() {

    }

    //初始化activity，从本地文件中取出数据
    private void getAppState() {
        user = new Users();
        SharedPreferences sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        isLogin = sp.getString("isLogin", NOLOGIN);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Drawable drawable = UserUtils.updatePortrait();//        更新头像信息
        if (drawable != null) {
            imageView_header.setImageDrawable(drawable);
        }
        getAppState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.e(TAG, "item=" + item);
        if (id == R.id.nav_account) {
            AccountActivity.activityStart(this);
        } else if (id == R.id.nav_category) {
            CategoryActivity.activityStart(this);
        } else if (id == R.id.nav_more) {
            MoreActivity.activityStart(this);
        } else if (id == R.id.nav_setting) {
            SettingActivity.activityStart(this);
        } else if (id == R.id.nav_share) {
            Log.e(TAG, "感谢您点击分享！");
        } else if (id == R.id.nav_send) {
            Log.e(TAG, "感谢您点击发送！");
        }
        //关闭菜单
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * 更改Fragment对象
     * @param index
     */
    private void switchFragment(int index) {
        transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        for (int i = 0; i < fragments.size(); i++) {
            if (index == i) {
                transaction.show(fragments.get(index));
            } else {
                transaction.hide(fragments.get(i));
            }
        }
        transaction.commit();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //销毁活动时关闭数据库连接
        if (dbManager.db != null) {
            dbManager.close();
            Log.e(TAG, "数据库连接已关闭！");
        }
    }

    @Override
    public void onStart(){
       super.onStart();
    }
}
