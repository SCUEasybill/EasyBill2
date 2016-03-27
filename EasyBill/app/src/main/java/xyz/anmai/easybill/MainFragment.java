package xyz.anmai.easybill;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.scu.easybill.iflytek.speech.util.JsonParser;
import com.scu.easybill.login_db.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Created by anquan on 2016/3/1.
 */
public class MainFragment extends Fragment {
    private PieChart mChart;
    private static String TAG = MainFragment.class.getSimpleName();
    View view;
    Button btnVoice;
    Dialog pd;
    DialogFragment dialogFragment;

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private EditText mResultText;
    private Toast mToast;
    private SharedPreferences mSharedPreferences;
    private Context mContext;
    int ret = 0; // 函数调用返回值


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, null);
        mContext = getActivity().getApplicationContext();
        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(MainActivity.context, mInitListener);
        mSharedPreferences = getActivity().getSharedPreferences("com.scu.easybill.mainfragment", Context.MODE_PRIVATE);
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(getActivity().getApplicationContext(), mInitListener);
        //点击语音按钮
        btnVoice = (Button) view.findViewById(R.id.voice);
        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "click voice", Toast.LENGTH_LONG).show();
                boolean isShowDialog = mSharedPreferences.getBoolean(
                        getString(R.string.pref_key_iat_show), true);
//                if (!isShowDialog) {
//                    //显示听写对话框===error:对话框显示失败
//                    mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//                    mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
//                    mIatDialog.setListener(mRecognizerDialogListener);
//                    mIatDialog.show();
//                    showTip(getString(R.string.text_begin));
//                } else {
//				 不显示听写对话框
                pd = onCreateDialog(savedInstanceState);
                pd.show();
                ret = mIat.startListening(mRecognizerListener);
                if (ret != ErrorCode.SUCCESS) {
                    showTip("听写失败,错误码：" + ret);
                } else {
                    showTip(getString(R.string.text_begin));
                }
//                }
            }
        });
        //饼状图
        mChart = (PieChart) view.findViewById(R.id.spread_pie_chart);
        PieData mPieData = getPieData(4, 100);
        showChart(mChart, mPieData);
        return view;
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };
    /**
     * 语音开始
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }
    };

    private String printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        showTip(resultBuffer.toString());
        return resultBuffer.toString();

    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        //会有两次执行函数，第一次islast为false，第二次为true
        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            String voice = printResult(results);
            if (isLast) {
                // TODO 最后的结果
                pd.dismiss();
                Intent intent = new Intent(getActivity(), Login.class);
                intent.putExtra("voice", voice);
                startActivity(intent);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        mIat.cancel();
        mIat.destroy();
    }

    @Override
    public void onResume() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(mContext);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    public void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(mContext);
        super.onPause();
    }

    /**
     * 语音结束
     */
    public Dialog onCreateDialog(final Bundle saveInstancestate) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("speak");
        dialog.setMessage("please speak!");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        return dialog;
    }

    /**
     * author anmai
     *
     * @param count 分成几部分
     * @param range
     */
    private PieData getPieData(int count, float range) {

        ArrayList<String> xValues = new ArrayList<String>();  //xVals用来表示每个饼块上的内容

        for (int i = 0; i < count; i++) {
            xValues.add("Quarterly" + (i + 1));  //饼块上显示成Quarterly1, Quarterly2, Quarterly3, Quarterly4
        }

        ArrayList<Entry> yValues = new ArrayList<Entry>();  //yVals用来表示封装每个饼块的实际数据

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
        float quarterly1 = 14;
        float quarterly2 = 14;
        float quarterly3 = 34;
        float quarterly4 = 38;

        yValues.add(new Entry(quarterly1, 0));
        yValues.add(new Entry(quarterly2, 1));
        yValues.add(new Entry(quarterly3, 2));
        yValues.add(new Entry(quarterly4, 3));

        //y轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "Quarterly Revenue 2014"/*显示在比例图上*/);
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        float px = 5 * (metrics.densityDpi / 160f);
        float px = 3 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(xValues, pieDataSet);

        return pieData;
    }

    /**
     * author anmai
     *
     * @param pieChart
     * @param pieData
     */
    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(55f);  //半径
        pieChart.setTransparentCircleRadius(60f); // 半透明圈
        //pieChart.setHoleRadius(0)  //实心圆

//        pieChart.setDescription("测试饼状图");
        pieChart.setDescription(null);

        // mChart.setDrawYValues(true);
        pieChart.setDrawCenterText(true);  //饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        // draws the corresponding description value into the slice
        // mChart.setDrawXValues(true);

        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true); // 可以手动旋转

        // display percentage values
        pieChart.setUsePercentValues(true);  //显示成百分比
        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
//      mChart.setOnChartValueSelectedListener(this);
        // mChart.setTouchEnabled(false);

//      mChart.setOnAnimationListener(this);

        pieChart.setCenterText("Quarterly Revenue");  //饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
//      pieChart.highlightValues(null);
//      pieChart.invalidate();

        Legend mLegend = pieChart.getLegend();  //设置比例图
        mLegend.setEnabled(false);
//        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
//        mLegend.setXEntrySpace(7f);
//        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }

}