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
 * 折线图
 */
public class ReportLineChart extends Fragment {
//    public String initContent() {
//        return "这是折线图界面";
//    }
    Button fragmentLineBtn;
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){
        View view = inflater.inflate(R.layout.report_line_chart,container,false);
        fragmentLineBtn = (Button) view.findViewById(R.id.btn_report_line);
        fragmentLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "fragment_line", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
