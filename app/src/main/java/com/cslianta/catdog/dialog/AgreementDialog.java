package com.cslianta.catdog.dialog;

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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cslianta.catdog.AppConst;
import com.cslianta.catdog.R;
import com.cslianta.catdog.csj.ZYMAllAdsUtils;

import com.cslianta.catdog.ui.activity.CAWebViewActivity;



public class AgreementDialog extends Dialog implements View.OnClickListener {
    private DialogCallBack agreementCallBack;
    private TextView btn_go_home;
    private TextView tv_back, tv_user_protocol, tv_user_policy;
    private FrameLayout fl_content_4;
    private Activity activityDialog;
    public static void showDialog(Activity  activity, DialogCallBack agreementCallBack) {
        AgreementDialog agreementDialog = new AgreementDialog(activity, agreementCallBack);
        agreementDialog.showDialog();
    }


    public AgreementDialog(@NonNull Activity activity, DialogCallBack agreementCallBack) {
        super(activity);
        this.activityDialog = activity;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.agreementCallBack = agreementCallBack;
        initView(activity);
    }

    private void initView(Context context) {
        View view = View.inflate(context, R.layout.public_dialog_wifi_privacy_agreement, null);
        setContentView(view);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
        this.tv_user_protocol = view.findViewById(R.id.tv_user_protocol);
        this.tv_user_policy = view.findViewById(R.id.tv_user_policy);
        this.btn_go_home = view.findViewById(R.id.btn_go_home);
        this.tv_back = view.findViewById(R.id.tv_back);
        this.fl_content_4 = view.findViewById(R.id.fl_content_4);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.public_heartbeat);
        this.btn_go_home.startAnimation(anim);
        ZYMAllAdsUtils.INSTANCE.loadSimpleAll(this.activityDialog,"信息",this.fl_content_4);
        this.btn_go_home.setOnClickListener(this);
        this.tv_back.setOnClickListener(this);
        this.tv_user_protocol.setOnClickListener(this);
        this.tv_user_policy.setOnClickListener(this);

    }

    private void showDialog() {
        this.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

            if (v.getId()==R.id.btn_go_home){

                    if (agreementCallBack != null) {
                        agreementCallBack.buAgree();
                    }
                    dismiss();
            }  else   if (v.getId()== R.id.tv_back){
                if (agreementCallBack != null) {
                    agreementCallBack.disagree();
                }
                dismiss();
             }
            else   if (v.getId()==  R.id.tv_user_protocol){

                CAWebViewActivity.Companion.forward(
                        getContext(),
                        getContext().getString(R.string.user_agreement),
                        AppConst.URL_USER_AGREEMENT
                );



             }
             else   if (v.getId()==  R.id.tv_user_policy){

                CAWebViewActivity.Companion.forward(
                        getContext(),
                        getContext().getString(R.string.privacy_policy),
                        AppConst.URL_PRIVACY_POLICY
                );

             }

    }
}
