package com.weini.catdog.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.weini.catdog.R
import com.weini.catdog.base.dj.BaseActivity
import com.weini.catdog.databinding.ActivityLanguageBinding
import com.weini.catdog.databinding.ItemLanguageBinding
import com.weini.catdog.entity.LanguageEntity
import com.weini.catdog.utils.LanguageUtils

class WCLanguageActivity : BaseActivity() {

    companion object {
        fun forward(context: Context, isFirst: Boolean) {
            context.startActivity(Intent(context, WCLanguageActivity::class.java).apply {
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
        binding.rclLanguage.bindingAdapter.models = com.weini.catdog.AppConst.languageList
        LanguageUtils.setIndex(0)
        if (isFirst) {
            WCGuideActivity.forward(this)
        } else {
            MainActivity.forward(this)
        }
        finish()
    }

}