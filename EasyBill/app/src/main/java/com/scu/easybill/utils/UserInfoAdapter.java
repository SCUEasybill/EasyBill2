package com.scu.easybill.utils;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import xyz.anmai.easybill.R;



public class UserInfoAdapter extends ArrayAdapter<UserInfoDetails> {
    private int resourceId;

    public UserInfoAdapter(Context context, int textViewResourceId, List<UserInfoDetails> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserInfoDetails userInfoDetails = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.key = (TextView) view.findViewById(R.id.tv_userInfo_key);
            viewHolder.value = (TextView) view.findViewById(R.id.tv_userInfo_value);
            view.setTag(viewHolder);//将ViewHolder存储到View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();//重新获取ViewHolder
        }
        viewHolder.key.setText(userInfoDetails.getKey());
        viewHolder.value.setText(userInfoDetails.getValue());
        return view;
    }

    class ViewHolder {
        TextView key;
        TextView value;
    }


}