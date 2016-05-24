package com.myresources.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myresources.R;

/**
 * 作者：李靖靖 on 2016/5/24 16:00
 * 简介：自定义dialog对话框  工具类
 */
public class DialogOfSetting extends Dialog {
    private TextView mTitle;//标题
    private TextView mMessage;//消息
    public Button mCancle;//取消按钮
    public Button mSure;//确定按钮
    private boolean isShowScop;
    public Button mConfirm;
    private LinearLayout btnlayout;
    //	private TagSettingListener listener;
    private boolean isShowBtn;

    private LinearLayout scopLayout;
    private EditText editScop;



    public TextView getmTitle() {
        return mTitle;
    }

    public void setmTitle(String title) {
        mTitle.setText(title);
    }

    public TextView getmMessage() {
        return mMessage;
    }

    public void setmMessage(String message) {
        mMessage.setText(message);
    }



    public EditText getEditScop() {
        return editScop;
    }

    public void setEditScop(EditText editScop) {
        this.editScop = editScop;
    }

    public void setShowScop(boolean isShow){
        if(isShow){
            scopLayout.setVisibility(View.VISIBLE);
            mMessage.setVisibility(View.GONE);
        }else{
            scopLayout.setVisibility(View.GONE);
            mMessage.setVisibility(View.VISIBLE);
        }
    }

    public void setShowBtn(boolean isShow){
        if(isShow){
            btnlayout.setVisibility(View.VISIBLE);
            mConfirm.setVisibility(View.GONE);
//			mMessage.setPadding(0, 30, 0,0);
        }else{
            btnlayout.setVisibility(View.GONE);
            mConfirm.setVisibility(View.VISIBLE);
        }

    }

    public DialogOfSetting(Context context) {
        super(context, R.style.dialog2);
        View view = LayoutInflater.from(context).inflate(R.layout.my_dialog_setting, null);
        setContentView(view);
		mTitle = (TextView)view.findViewById(R.id.tv_title);
        mMessage = (TextView)view.findViewById(R.id.tv_message);
        mCancle = (Button)view.findViewById(R.id.cancle_btn);
        mSure = (Button)view.findViewById(R.id.sure_btn);
        mConfirm= (Button) view.findViewById(R.id.confirm_btn);
        btnlayout = (LinearLayout)findViewById(R.id.btn);
        scopLayout = (LinearLayout)findViewById(R.id.scop);
        editScop = (EditText)findViewById(R.id.scop_edit);
//        mCancle.getBackground().setAlpha(0);
//        mSure.getBackground().setAlpha(0);
    }

}
