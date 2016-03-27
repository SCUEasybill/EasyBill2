package com.scu.easybill.login_db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import xyz.anmai.easybill.R;

/**
 * Created by guyu on 2016/3/14.
 */
public class SelectPicPopupWindow extends PopupWindow {
    private Button btnTakePhoto, btnPickPhoto, btnCancle;
    private View mMenuView;
    public SelectPicPopupWindow(Context context, View.OnClickListener itemsOnClick){
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.login_dialog_pic,null);

        btnTakePhoto = (Button) mMenuView.findViewById(R.id.btn_takePhoto);
        btnPickPhoto = (Button) mMenuView.findViewById(R.id.btn_pickPhoto);
        btnCancle = (Button) mMenuView.findViewById(R.id.btn_cancel);
        //设置监听
        btnTakePhoto.setOnClickListener(itemsOnClick);
        btnPickPhoto.setOnClickListener(itemsOnClick);
        btnCancle.setOnClickListener(itemsOnClick);

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);//设置窗体可点击
        this.setAnimationStyle(R.style.PopupAnimation);//弹出对话框的动画
//        ColorDrawable dw = new ColorDrawable(0x80000000);//实例化一个ColorDrawable，半透明
//        this.setBackgroundDrawable(dw);//设置弹出的窗体背景
        //获取监听，如果触屏位置在框体外面，销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if(event.getAction() == MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
