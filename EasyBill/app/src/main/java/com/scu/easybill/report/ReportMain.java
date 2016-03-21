package com.scu.easybill.report;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import xyz.anmai.easybill.R;

/**
 * Created by guyu on 2016/3/13.
 */
public class ReportMain extends AppCompatActivity {
    public static void activityStart(Context context) {
        Intent intent = new Intent(context, ReportMain.class);
        context.startActivity(intent);
    }

    private SegmentControlView SegmentControlView = null;
    //需要切换的fragment
    private ReportLineChart reportLineChart = null;
    private ReportPieChart reportPieChart = null;

    private FragmentManager fragmentManager;
    private List<Fragment> fragments = new ArrayList<Fragment>();//缓存fragments

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_main);
//        reportLineChart = new ReportLineChart();
//        reportPieChart = new ReportPieChart();
//
//        fragments.add(reportLineChart);
//        fragments.add(reportPieChart);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_tab_report);
        final RadioButton radioButton_line = (RadioButton) findViewById(R.id.radiobutton_line);
        final RadioButton radioButton_pie = (RadioButton) findViewById(R.id.radiobutton_pie);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                switch (checkedId){
                    case R.id.radiobutton_line:
                        if(reportLineChart == null){
                            reportLineChart = new ReportLineChart();
                        }
                        radioButton_line.setChecked(true);
                        fragmentTransaction.replace(R.id.frameLayout_report_main,reportLineChart);
                        break;
                    case R.id.radiobutton_pie:
                        if(reportPieChart == null){
                            reportPieChart = new ReportPieChart();
                        }
                        radioButton_pie.setChecked(true);
                        fragmentTransaction.replace(R.id.frameLayout_report_main,reportPieChart);
                        break;
                    default:
                        break;
                }
                fragmentTransaction.commit();
            }
        });

//        FindById();
        // 设置默认的Fragment
        setDefaultFragment();
//        Listener();

    }

//    private void FindById() {
//        SegmentControlView = (SegmentControlView) findViewById(R.id.SegmentControlView);
//    }
//
//    private void Listener() {
//        SegmentControlView.setOnSegmentControlViewClickListener(new com.scu.easybill.report.SegmentControlView.onSegmentControlViewClickListener() {
//
//            @Override
//            public void onSegmentControlViewClick(View view, int position) {
//                FragmentManager fm = getFragmentManager();
//                // 开启Fragment事务
//                FragmentTransaction transaction = fm.beginTransaction();
//
//                switch (position) {
//                    case 0:
//                        if (reportLineChart == null) {
//                            reportLineChart = new ReportLineChart();
//                        }
//                        // 使用当前Fragment的布局替代id_content的控件
//                        transaction.replace(R.id.fragmentlayout, reportLineChart);
//                        // 事务提交
//                        transaction.commit();
//                        break;
//                    case 1:
//                        if (reportPieChart == null) {
//                            reportPieChart = new ReportPieChart();
//                        }
//                        // 使用当前Fragment的布局替代id_content的控件
//                        transaction.replace(R.id.fragmentlayout, reportPieChart);
//                        // 事务提交
//                        transaction.commit();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });
//    }

    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        reportPieChart = new ReportPieChart();
        transaction.replace(R.id.frameLayout_report_main, reportPieChart);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
