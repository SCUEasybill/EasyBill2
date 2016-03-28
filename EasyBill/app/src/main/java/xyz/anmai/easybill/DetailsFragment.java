package xyz.anmai.easybill;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import scu.function.Calendar;
import scu.function.CalendarView;

/**
 * Created by anquan on 2016/3/1.
 */
public class DetailsFragment extends Fragment{
    private Button graph_left,graph_right;
    private LinearLayout layout_one,layout_two;
    private TextView start_time,end_time;

    private CalendarView calendar;
    private ImageButton calendarLeft;
    private TextView calendarCenter;
    private ImageButton calendarRight;
    private SimpleDateFormat format;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_detail, null);
        start_time=(TextView)view.findViewById(R.id.start_time);
        end_time=(TextView)view.findViewById(R.id.end_time);
        graph_left=(Button)view.findViewById(R.id.graph_left);
        graph_right=(Button)view.findViewById(R.id.graph_right);
        layout_one=(LinearLayout)view.findViewById(R.id.graph_one);
        layout_two=(LinearLayout)view.findViewById(R.id.graph_two);
        Date now = new Date();
        java.util.Calendar cal=java.util.Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String str = format.format(now);
        start_time.setText(str+" 到 ");
        end_time.setText(str);
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar dialog = new Calendar();
                dialog.setClickerListener(new Calendar.OnClickerListener() {
                    @Override
                    public void onclicker(String date) {
                        start_time.setText(date+" 到 ");
                    }
                });
                dialog.show(getFragmentManager(), "");
            }
        });
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar dialog = new Calendar();
                dialog.setClickerListener(new Calendar.OnClickerListener() {
                    @Override
                    public void onclicker(String date) {
                        end_time.setText(date);
                    }
                });
                dialog.show(getFragmentManager(), "");

            }
        });
        //设置两个按钮所显示的界面
        graph_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_one.setVisibility(View.GONE);
                layout_two.setVisibility(View.INVISIBLE);
            }
        });
        graph_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_two.setVisibility(View.GONE);
                layout_one.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

}