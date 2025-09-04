package com.weini.maogou.widget.dialog.dj;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.weini.maogou.R;
import com.weini.maogou.dialog.DialogCallBack;
import com.weini.maogou.utils.lzy.LZYADSUtils;


public class VipDialog extends Dialog implements View.OnClickListener {
    private DialogCallBack agreementCallBack;
    private LinearLayout sp_metro_code;
    private TextView tv_back;
    private ImageView iv_cancel;

    private FrameLayout fl_content_4;

    private Activity activityDialog;
    public static void showDialog(Activity activity, DialogCallBack agreementCallBack) {

        VipDialog agreementDialog = new VipDialog(activity, agreementCallBack);
        agreementDialog.showDialog();
    }


    public VipDialog(@NonNull Activity activity, DialogCallBack agreementCallBack) {
        super(activity);
        this.activityDialog = activity;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.agreementCallBack = agreementCallBack;
        initView(activity);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.dialog_vip, null);
        setContentView(view);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        this.sp_metro_code = view.findViewById(R.id.sp_metro_code);
        this.iv_cancel = view.findViewById(R.id.iv_cancel);
        this.tv_back = view.findViewById(R.id.tv_back);
        this.fl_content_4 = view.findViewById(R.id.fl_content_4);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.public_heartbeat);
        this.sp_metro_code.startAnimation(anim);
        new LZYADSUtils("APP",this.activityDialog).loadSimpleAd3(this.activityDialog,this.fl_content_4);
        this.sp_metro_code.setOnClickListener(this);
        this.tv_back.setOnClickListener(this);
        this.iv_cancel.setOnClickListener(this);

    }

    private void showDialog() {
        this.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

            if (v.getId()==R.id.sp_metro_code){

                    if (agreementCallBack != null) {
                        agreementCallBack.buAgree();
                    }
                    dismiss();
            }  else   if (v.getId()== R.id.tv_back){
                if (agreementCallBack != null) {
                    agreementCallBack.disagree();
                }
                dismiss();
            }  else   if (v.getId()== R.id.iv_cancel){
                if (agreementCallBack != null) {
                    agreementCallBack.disagree();
                }
                dismiss();
            }

    }
}
