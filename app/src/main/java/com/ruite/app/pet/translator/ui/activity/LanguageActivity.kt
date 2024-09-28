package com.ruite.app.pet.translator.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.qmuiteam.qmui.kotlin.onClick
import com.ruite.app.pet.translator.Const
import com.ruite.app.pet.translator.R
import com.ruite.app.pet.translator.base.BaseActivity
import com.ruite.app.pet.translator.databinding.ActivityLanguageBinding
import com.ruite.app.pet.translator.databinding.ItemLanguageBinding
import com.ruite.app.pet.translator.entity.LanguageEntity
import com.ruite.app.pet.translator.ext.thrillClickListener
import com.ruite.app.pet.translator.utils.LanguageUtils

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

                    root.thrillClickListener {
                        binding.ivDone.isVisible = true
                        val lastSelected = selected
                        selected = modelPosition
                        if (lastSelected > -1) {
                            binding.rclLanguage.bindingAdapter.notifyItemChanged(lastSelected)
                        }
                        binding.rclLanguage.bindingAdapter.notifyItemChanged(selected)
                    }
                }
            }
        }
        binding.rclLanguage.bindingAdapter.models = Const.languageList
        binding.ivDone.thrillClickListener {
            // done
            LanguageUtils.setIndex(selected)
            if (isFirst) {
                GuideActivity.forward(this)
            } else {
                MainActivity.forward(this)
            }
            finish()
        }
    }

}