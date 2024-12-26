package com.appcatdog.translations.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.appcatdog.translations.R
import com.appcatdog.translations.base.dj.BaseActivity
import com.appcatdog.translations.databinding.ActivityLanguageBinding
import com.appcatdog.translations.databinding.ItemLanguageBinding
import com.appcatdog.translations.entity.LanguageEntity
import com.appcatdog.translations.utils.LanguageUtils

class LanguageActivity : BaseActivity() {

    companion object {
        fun forward(context: Context, isFirst: Boolean) {
            context.startActivity(Intent(context, LanguageActivity::class.java).apply {
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
        binding.rclLanguage.bindingAdapter.models = com.appcatdog.translations.AppConst.languageList
        LanguageUtils.setIndex(0)
        if (isFirst) {
            GuideActivity.forward(this)
        } else {
            MainActivity.forward(this)
        }
        finish()
    }

}