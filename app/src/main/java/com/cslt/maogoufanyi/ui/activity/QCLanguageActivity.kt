package com.cslt.maogoufanyi.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.cslt.maogoufanyi.R
import com.cslt.maogoufanyi.base.dj.BaseActivity
import com.cslt.maogoufanyi.databinding.ActivityLanguageBinding
import com.cslt.maogoufanyi.databinding.ItemLanguageBinding
import com.cslt.maogoufanyi.entity.LanguageEntity
import com.cslt.maogoufanyi.utils.LanguageUtils

class QCLanguageActivity : BaseActivity() {

    companion object {
        fun forward(context: Context, isFirst: Boolean) {
            context.startActivity(Intent(context, QCLanguageActivity::class.java).apply {
                putExtra("isFirst", isFirst)
            })
        }
    }

    override fun getLayoutId() = R.layout.activity_language

    private lateinit var binding: ActivityLanguageBinding

    private var selected: Int = -1

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityLanguageBinding.bind(view)
        val isFirst = intent.getBooleanExtra("isFirst", false)
        binding.rclLanguage.linear().setup {
            addType<LanguageEntity>(R.layout.item_language)

            onBind {
                getBinding<ItemLanguageBinding>().apply {
                    val item = getModel<LanguageEntity>()
                    imgLanguage.setImageResource(item.icon)
                    tvTitleLanguage.text = item.name
                    checkboxLanguage.isChecked = modelPosition == selected
                }
            }
        }
        binding.rclLanguage.bindingAdapter.models = com.cslt.maogoufanyi.AppConst.languageList
        LanguageUtils.setIndex(0)
        if (isFirst) {
            QCGuideActivity.forward(this)
        } else {
            MainActivity.forward(this)
        }
        finish()
    }

}