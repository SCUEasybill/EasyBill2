package scu.function;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import xyz.anmai.easybill.R;

public class Calendar extends DialogFragment implements View.OnClickListener{
    private CalendarView calendar;
    private ImageButton calendarLeft;
    private TextView calendarCenter;
    private ImageButton calendarRight;
    private SimpleDateFormat format;
    OnClickerListener listener;
    private Date date;
    public interface OnClickerListener{
        public void onclicker(String date);
    }
    public void setClickerListener(OnClickerListener listener){
        this.listener=listener;
    }
    public void onClick(View v) {

    }
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.calendar, null);
        view.setOnClickListener(this);
        format = new SimpleDateFormat("yyyy年MM月dd日");
        //获取日历控件对象
        calendar = (CalendarView) view.findViewById(R.id.calendar);
        calendar.setSelectMore(false); //单选
        calendarLeft = (ImageButton) view.findViewById(R.id.calendarLeft);
        calendarCenter = (TextView) view.findViewById(R.id.calendarCenter);
        calendarRight = (ImageButton) view.findViewById(R.id.calendarRight);
        try {
            //设置日历日期
            Date date = format.parse("2015-01-01");
            calendar.setCalendarData(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //获取日历中年月 ya[0]为年，ya[1]为月（格式大家可以自行在日历控件中改）
        String[] ya = calendar.getYearAndmonth().split("-");
        calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
        calendarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击上一月 同样返回年月
                String leftYearAndmonth = calendar.clickLeftMonth();
                String[] ya = leftYearAndmonth.split("-");
                calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
            }
        });
        calendarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击下一月
                String rightYearAndmonth = calendar.clickRightMonth();
                String[] ya = rightYearAndmonth.split("-");
                calendarCenter.setText(ya[0] + "年" + ya[1] + "月");
            }
        });
        //设置控件监听，可以监听到点击的每一天（大家也可以在控件中根据需求设定）
        calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {
            @Override
            public void OnItemClick(Date selectedStartDate,
                                    Date selectedEndDate, Date downDate) {

                listener.onclicker(format.format(selectedStartDate));
                if (calendar.isSelectMore()) {
                    date = downDate;
                    dismiss();
                    Toast.makeText(calendar.getContext(), format.format(selectedStartDate) + "到" + format.format(selectedEndDate), Toast.LENGTH_SHORT).show();
                } else {
                    date = downDate;
                    dismiss();
                    Toast.makeText(calendar.getContext(), format.format(downDate), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
}
