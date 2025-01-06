package com.ruiteapp.pettranslator.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.ruiteapp.pettranslator.R
import com.ruiteapp.pettranslator.base.dj.BaseActivity
import com.ruiteapp.pettranslator.databinding.ActivityLanguageBinding
import com.ruiteapp.pettranslator.databinding.ItemLanguageBinding
import com.ruiteapp.pettranslator.entity.LanguageEntity
import com.ruiteapp.pettranslator.utils.LanguageUtils

class ZZLanguageActivity : BaseActivity() {

    companion object {
        fun forward(context: Context, isFirst: Boolean) {
            context.startActivity(Intent(context, ZZLanguageActivity::class.java).apply {
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
        binding.rclLanguage.bindingAdapter.models = com.ruiteapp.pettranslator.AppConst.languageList
        LanguageUtils.setIndex(0)
        if (isFirst) {
            ZZGuideActivity.forward(this)
        } else {
            ZZMainActivity.forward(this)
        }
        finish()
    }

}