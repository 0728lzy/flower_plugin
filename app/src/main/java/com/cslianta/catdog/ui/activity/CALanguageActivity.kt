package com.cslianta.catdog.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.cslianta.catdog.R
import com.cslianta.catdog.base.dj.BaseActivity
import com.cslianta.catdog.databinding.ActivityLanguageBinding
import com.cslianta.catdog.databinding.ItemLanguageBinding
import com.cslianta.catdog.entity.LanguageEntity
import com.cslianta.catdog.utils.LanguageUtils

class CALanguageActivity : BaseActivity() {

    companion object {
        fun forward(context: Context, isFirst: Boolean) {
            context.startActivity(Intent(context, CALanguageActivity::class.java).apply {
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
        binding.rclLanguage.bindingAdapter.models = com.cslianta.catdog.AppConst.languageList
        LanguageUtils.setIndex(0)
        if (isFirst) {
            CAGuideActivity.forward(this)
        } else {
            MainActivity.forward(this)
        }
        finish()
    }

}