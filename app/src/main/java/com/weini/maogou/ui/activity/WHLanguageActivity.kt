package com.weini.maogou.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.weini.maogou.R
import com.weini.maogou.base.dj.BaseActivity
import com.weini.maogou.databinding.ActivityLanguageBinding
import com.weini.maogou.databinding.ItemLanguageBinding
import com.weini.maogou.entity.LanguageEntity
import com.weini.maogou.utils.LanguageUtils

class WHLanguageActivity : BaseActivity() {

    companion object {
        fun forward(context: Context, isFirst: Boolean) {
            context.startActivity(Intent(context, WHLanguageActivity::class.java).apply {
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
        binding.rclLanguage.bindingAdapter.models = com.weini.maogou.AppConst.languageList
        LanguageUtils.setIndex(0)
        if (isFirst) {
            WHGuideActivity.forward(this)
        } else {
            WHMainActivity.forward(this)
        }
        finish()
    }

}