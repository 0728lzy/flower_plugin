package com.qingchu.wangmiao.widget.dialog.dj

import android.app.Activity
import android.content.Context
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.qingchu.wangmiao.AppConst
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.base.dj.BaseDialog
import com.qingchu.wangmiao.databinding.DialogAgreementBinding
import com.qingchu.wangmiao.ext.dj.thrillClickListener
import com.qingchu.wangmiao.ui.activity.WHWebViewActivity
import com.qingchu.wangmiao.utils.dj.SharedPreferencesDelegate


/**
 * date :   2023-12-25 025
 * author:  DengZhiYang
 * desc:    something
 */
class NBAgreementDialog(val activity: Activity) : BaseDialog() {

    override fun getLayoutId() = R.layout.dialog_agreement
    private var mOnReceiveDataListener: IOnReceiveDataListener? = null
    private var _binding: DialogAgreementBinding? = null
    private val binding get() = requireNotNull(_binding) { "The property of binding has been destroyed." }


    var isAgree by SharedPreferencesDelegate({ requireContext() }, false, "IS_AGREE")

    override fun initView(view: View) {

        _binding = DialogAgreementBinding.bind(view)

        if (null !=_binding) {
//            val subView =AgreementBottomOneLayoutBinding.bind(_binding!!.root)
//            val subView = _binding!!.vBottomOne.inflate()

            _binding!!.tvDisagree.thrillClickListener {
                activity.finish()
                dismiss()
            }

            _binding!!.buAgree.thrillClickListener {
                isAgree = true
                _binding!!.buAgreeIco.isChecked = true
                mOnReceiveDataListener?.OnSuccessReceiveData()
                dismiss()
            }
//            subView.getBinding<AgreementBottomOneLayoutBinding>().apply {
//                tvDisagree.thrillClickListener {
//                    activity.finish()
//                    dismiss()
//                }
//
//                buAgree.thrillClickListener {
//                    isAgree = true
//                    buAgreeIco.isChecked = true
//                    mOnReceiveDataListener?.OnSuccessReceiveData()
//                    dismiss()
//                }
//            }
            val spannableString = SpannableString(getString(R.string.agreement_dialog_context3))
            spannableString.setSpan(
                AgreementClickableSpan(
                    requireContext(),
                    AgreementClickableSpan.SPAN_TYPE_USER_SERVICE_AGREEMENT
                ), 22, 28, 33
            )
            spannableString.setSpan(
                AgreementClickableSpan(
                    requireContext(),
                    AgreementClickableSpan.SPAN_TYPE_PRIVACY_POLICY_AGREEMENT
                ), 29, 35, 33
            )
            _binding!!.tvSP.text = spannableString;
            _binding!!.tvSP.highlightColor = 0;
            _binding!!.tvSP.movementMethod = LinkMovementMethod.getInstance();

            val spannableStringTwo = SpannableString(getString(R.string.agreement_dialog_context4))
            spannableStringTwo.setSpan(
                AgreementClickableSpan(
                    activity,
                    AgreementClickableSpan.SPAN_TYPE_USER_SERVICE_AGREEMENT
                ), 5, 11, 33
            )
            spannableStringTwo.setSpan(
                AgreementClickableSpan(
                    activity,
                    AgreementClickableSpan.SPAN_TYPE_PRIVACY_POLICY_AGREEMENT
                ), 12, 18, 33
            )
            _binding!!.butAgreement.text = spannableStringTwo;
            _binding!!.butAgreement.highlightColor = 0;
            _binding!!.butAgreement.movementMethod = LinkMovementMethod.getInstance();
            val stringName = activity.getString(R.string.app_name)
            _binding!!.tvTips.setText(
                String.format(
                    getString(R.string.agreement_dialog_title2),
                    stringName
                )
            )
            _binding!!.tvTips2.setText(
                String.format(
                    getString(R.string.agreement_dialog_context9),
                    stringName
                )
            )
        }
    }

    /**
     * 协议点击。
     */
    class AgreementClickableSpan(private val context: Context, private val spanType: String) :
        ClickableSpan() {
        override fun onClick(view: View) {
            when (spanType) {
                SPAN_TYPE_USER_SERVICE_AGREEMENT -> {
                    WHWebViewActivity.forward(
                        context,
                        AppConst.URL_USER_AGREEMENT,
                        context.getString(R.string.user_agreement)
                    )
                }

                SPAN_TYPE_PRIVACY_POLICY_AGREEMENT -> {
                    WHWebViewActivity.forward(
                        context,
                        AppConst.URL_PRIVACY_POLICY,
                        context.getString(R.string.privacy_policy)
                    )
                }

                else -> {
                }
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = context.resources.getColor(R.color.ex);
        }

        companion object {
            const val SPAN_TYPE_USER_SERVICE_AGREEMENT = "UserServiceAgreement"
            const val SPAN_TYPE_PRIVACY_POLICY_AGREEMENT = "PrivacyPolicyAgreement"
        }

    }
    //回调接收数据成功或者失败
    fun setOnReceiveDataListener(iOnReceiveDataListener: IOnReceiveDataListener?) {
        mOnReceiveDataListener = iOnReceiveDataListener
    }
    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

    //接收数据成功或者失败的回调
    interface IOnReceiveDataListener {
        fun OnSuccessReceiveData()
    }
}