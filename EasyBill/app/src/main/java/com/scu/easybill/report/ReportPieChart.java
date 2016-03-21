package com.scu.easybill.report;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import xyz.anmai.easybill.R;

/**
 * Created by guyu on 2016/3/13.
 * 饼状图
 */
public class ReportPieChart extends Fragment{
    Button btnFragmentPie;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){
        View view;
        view = inflater.inflate(R.layout.report_pie_chart,container,false);
        btnFragmentPie = (Button) view.findViewById(R.id.btn_report_pie);
        btnFragmentPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "fragment_line", Toast.LENGTH_LONG).show();
            }
        });
        return  view;
    }
}
