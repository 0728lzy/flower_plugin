package com.qingchu.wangmiao.ui.activity

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qingchu.wangmiao.bean.dj.QCCommonConfigBean

import com.google.gson.Gson
import com.gyf.immersionbar.ImmersionBar
import com.hjq.toast.ToastUtils
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.adapter.dj.RBHelpQuestionAdapter
import com.qingchu.wangmiao.adapter.dj.base.ItemClickListener
import com.qingchu.wangmiao.adapter.dj.utils.GridRec
import com.qingchu.wangmiao.base.dj.BaseActivity
import com.qingchu.wangmiao.bean.dj.QCHelpQuestionBean
import com.qingchu.wangmiao.utils.dj.GetHttpDataUtil
import com.qingchu.wangmiao.utils.dj.IntentUtil
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.LoadingPopupView


@SuppressLint("NonConstantResourceId")
class QCContactCustomerServiceActivity : BaseActivity(){



    private var questionList = ArrayList<QCHelpQuestionBean>()
    private var subErrorType = ""   //用于提交反馈类型

    private val mAdapter by lazy {
        RBHelpQuestionAdapter(
            this,
            R.layout.item_help_question,
            questionList
        )
    }

    lateinit var recycler_view: RecyclerView
    lateinit var contact_btn_submit_info: Button
    lateinit var toolbar_close_title: TextView
    lateinit var toolbar_close_iv: ImageView
    lateinit var contact_tv_email: TextView
    lateinit var contact_tv_copy_email: TextView
    lateinit var contact_tv_qq: TextView
    lateinit var contact_tv_mobile: TextView
    lateinit var contact_et_backfeed: EditText
    lateinit var contact_et_mobile_or_qq: EditText
    lateinit var contact_et_email_new: EditText

    override fun getLayoutId(): Int = R.layout.activity_contact_customer_service_xx

    override fun initView(view: View, savedInstanceState: Bundle?) {



        recycler_view = view.findViewById(R.id.recycler_view)
        contact_btn_submit_info = view.findViewById(R.id.contact_btn_submit_info)
        toolbar_close_title = view.findViewById(R.id.toolbar_close_title)
        toolbar_close_iv = view.findViewById(R.id.toolbar_close_iv)
        contact_tv_email = view.findViewById(R.id.contact_tv_email)
        contact_tv_copy_email = view.findViewById(R.id.contact_tv_copy_email)
        contact_tv_qq = view.findViewById(R.id.contact_tv_qq)
        contact_tv_mobile = view.findViewById(R.id.contact_tv_mobile)
        contact_et_backfeed = view.findViewById(R.id.contact_et_backfeed)
        contact_et_mobile_or_qq = view.findViewById(R.id.contact_et_mobile_or_qq)
        contact_et_email_new = view.findViewById(R.id.contact_et_email)




        initActionBar("联系客服")

        recycler_view.layoutManager = GridLayoutManager(this,3)
        // 设置边距
        recycler_view.addItemDecoration(GridRec(15));
        recycler_view.adapter = mAdapter



        getIssuesList()

        mAdapter.setItemClickListener(object : ItemClickListener<QCHelpQuestionBean> {
            override fun click(position: Int, data: QCHelpQuestionBean) {
                if(questionList?.size!! >0){



                    for(i in 0 until questionList?.size!!){
                        if(questionList.get(i).id === data.id){
                            questionList.get(i).isSelected = true
                            subErrorType = questionList.get(i).id.toString()
                        }else{
                            questionList.get(i).isSelected = false
                        }
                    }
                    val gson = Gson()
                    mAdapter.notifyDataSetChanged()
                }


            }
        })

//        contact_tv_pull_qq.setOnClickListener {
//            toQQ()
//        }

//        contact_tv_dial.setOnClickListener {
//
//            addPhoneCall()
//
//        }

        contact_btn_submit_info.setOnClickListener {

            submitInfo()

        }

        getCommonConfig()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        super.setContentView(R.layout.activity_contact_customer_service_xx)
//
//
//    }


    companion object {
        fun show(context: Context?) {
            IntentUtil.redirect(context, QCContactCustomerServiceActivity::class.java, false, null)
        }
    }
    private fun initActionBar(title: String) {
        toolbar_close_iv.setOnClickListener {
            finish()
        }
        toolbar_close_title.text = title
        toolbar_close_title.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))


        contact_tv_copy_email.setOnClickListener {
            val cm: ClipboardManager =
                getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            if(TextUtils.isEmpty(contact_tv_email.text)){
                ToastUtils.show("暂无信息")
                return@setOnClickListener
            }
            val mClipData: ClipData = ClipData.newPlainText("Label", contact_tv_email.text)
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData)
            ToastUtils.show("复制成功")
        }


        ImmersionBar.with(this)
            .keyboardEnable(true).init()
    }


    var commonConfigs = ArrayList<QCCommonConfigBean>()

    //获取Vip通用配置
    fun getCommonConfig(){
        GetHttpDataUtil.getCommonConfig(object : GetHttpDataUtil.OnSuccessAndFaultListener{
            override fun onSuccess(t: Any) {
                commonConfigs = t as ArrayList<QCCommonConfigBean>
                setData()
            }

            override fun onFault() {

            }

        })
    }


    //获取客服问题标签列表
    fun getIssuesList(){
        GetHttpDataUtil.getIssuesList(object : GetHttpDataUtil.OnSuccessAndFaultListener{
            override fun onSuccess(t: Any) {
                val gson = Gson()
                questionList.clear()

                questionList.addAll(t as ArrayList<QCHelpQuestionBean>)





                if(questionList?.size!! >0) {
                    questionList.get(0).isSelected = true
                    subErrorType = questionList.get(0).id.toString()
                    mAdapter.notifyDataSetChanged()
                }
            }

            override fun onFault() {

            }

        })
    }


    fun setData(){
        if(commonConfigs?.size!! >0){
            for(bean in commonConfigs){
                if(bean.configKey.equals("vip_service_email")){
                    contact_tv_email.text = bean.configValue
                }else if(bean.configKey.equals("vip_service_qq")){
                    contact_tv_qq.text = bean.configValue
                }else if(bean.configKey.equals("vip_service_phone")){
                    contact_tv_mobile.text = bean.configValue
                }
            }
        }
    }


    private var loadingPopupView: LoadingPopupView? = null
    fun submitInfo(){
        val contactBackfeed = contact_et_backfeed.text.toString().trim()
        val contactMobileOrQQ = contact_et_mobile_or_qq.text.toString().trim()
        val contactEmail= contact_et_email_new.text.toString().trim()
        if(TextUtils.isEmpty(contactBackfeed)){
            ToastUtils.show("请输入描述您的问题")
        }else if(TextUtils.isEmpty(contactMobileOrQQ)&&TextUtils.isEmpty(contactEmail)){
            ToastUtils.show("请输入您的联系方式:手机号或者邮箱")
        }else if(!isValidPhoneNumber(contactMobileOrQQ)&& !TextUtils.isEmpty(contactMobileOrQQ)){
            ToastUtils.show("手机号非法，请检查~")
        }else if (!isValidEmail(contactEmail)&& !TextUtils.isEmpty(contactEmail)){
            ToastUtils.show("邮箱格式有误，请检查~")
        }else if(TextUtils.isEmpty(subErrorType)){
            ToastUtils.show("未知错误类型")
        }else{
            loadingPopupView = XPopup.Builder(this)
                .isDestroyOnDismiss(true)
                .dismissOnTouchOutside(false)
                .asLoading()
            loadingPopupView?.show()
            GetHttpDataUtil.uploadAfterSalesForm(contactMobileOrQQ,contactBackfeed,subErrorType,contactEmail,object : GetHttpDataUtil.OnSuccessAndFaultListener{
                override fun onSuccess(t: Any) {
                    loadingPopupView?.dismiss()
                    ToastUtils.show("提交成功~")
                    finish()

                }

                override fun onFault() {
                    loadingPopupView?.dismiss()
                }

            })
        }
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        val regex = Regex("^1[3-9]\\d{9}$")
        return regex.matches(phone)
    }

    fun isValidEmail(email: String): Boolean {
        val regex = Regex(
            "^(?!.*\\.\\.)" +                     // 不允许连续点
                    "[A-Za-z0-9+_.-]+(?<!\\.)" +         // 用户名部分，不以点结尾
                    "@" +
                    "([A-Za-z0-9-]+\\.)+" +              // 域名部分，可以有子域名
                    "[A-Za-z]{2,}$"                       // 顶级域名，至少 2 个字母
        )
        return regex.matches(email)
    }


}