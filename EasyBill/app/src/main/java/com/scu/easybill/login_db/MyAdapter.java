package com.scu.easybill.login_db;//package com.scu.login_db;
//
///**
// * Created by guyu on 2016/3/5.
// */
// /*添加一个得到数据的方法，方便使用*/
//
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//import xyz.anmai.easybill.R;
//
//
///*      * 新建一个类继承BaseAdapter，实现视图与数据的绑定
// */
//public class MyAdapter extends BaseAdapter {
//    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
//
//    public MyAdapter(Context context) {
//        this.mInflater = LayoutInflater.from(context);
//    }
//    @Override
//    public int getCount() {
//        return getDate().size();//返回数组的长度
//    }
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    /*书中详细解释该方法*/
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        //观察convertView随ListView滚动情况
//        Log.v("MyListViewBase", "getView " + position + " " + convertView);
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.login_userinfo_details, null);
//            holder = new ViewHolder();
//                    /*得到各个控件的对象*/
//            holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
//            holder.text = (TextView) convertView.findViewById(R.id.ItemText);
//            holder.bt = (Button) convertView.findViewById(R.id.ItemButton);
//            convertView.setTag(holder);//绑定ViewHolder对象
//        } else {
//            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象                  }
//            /*设置TextView显示的内容，即我们存放在动态数组中的数据*/
//            holder.title.setText(getDate().get(position).get("ItemTitle").toString());
//            holder.text.setText(getDate().get(position).get("ItemText").toString());
//
//            /*为Button添加点击事件*/
//            holder.bt.setOnClickListener(new android.view.View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.v("MyListViewBase", "你点击了按钮" + position);                                //打印Button的点击信息
//                }
//            });
//        }
//        return convertView;
//    }
//    private ArrayList<HashMap<String, Object>> getDate(){
//
//        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
//    /*为动态数组添加数据*/
//        for(int i=0;i<30;i++)
//        {
//            HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("ItemTitle", "第"+i+"行");
//            map.put("ItemText", "这是第"+i+"行");
//            listItem.add(map);
//        }
//        return listItem;
//    }
//    /*存放控件*/
//    public final class ViewHolder {
//        public TextView title;
//        public TextView text;
//        public Button bt;
//    }
//}

//在layout文件下新建xml文件
//<ImageView android:layout_alignParentRight="true" android:layout_width="wrap_content"
//        android:layout_height="wrap_content" android:id="@+id/ItemImage"/>
//<TextView android:id="@+id/ItemTitle" android:layout_height="wrap_content"
//        android:layout_width="fill_parent" android:textSize="20sp"/>
//<TextView android:id="@+id/ItemText" android:layout_height="wrap_content"
//        android:layout_width="fill_parent" android:layout_below="@+id/ItemTitle"/>
//
//<Button
//android:layout_alignParentRight="true" android:layout_width="wrap_content"
//        android:layout_height="wrap_content" android:id="@+id/ItemButton" />
