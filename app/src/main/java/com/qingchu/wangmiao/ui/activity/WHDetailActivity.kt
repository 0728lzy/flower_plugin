package com.qingchu.wangmiao.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.base.dj.BaseActivity
import com.qingchu.wangmiao.databinding.ActivityDetailBinding
import com.qingchu.wangmiao.ext.getBinding
import com.qingchu.wangmiao.ext.thrillClickListener
import com.qingchu.wangmiao.utils.lzy.LZYADSUtils

class WHDetailActivity : BaseActivity() {

    companion object {
        fun show(context: Context, index: Int) {
            context.startActivity(Intent(context, WHDetailActivity::class.java).apply { putExtra("index", index) })
        }
    }

    override fun getLayoutId() = R.layout.activity_detail

    private lateinit var binding: ActivityDetailBinding

    private lateinit var lzyadsUtils: LZYADSUtils


    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = view.getBinding()
        lzyadsUtils=LZYADSUtils("DetailActivity",this)
        lzyadsUtils.showAdCpTurn()
        lzyadsUtils.loadSimpleAdTurn(binding.feedContainerActivityDetail,-1)
        binding.toolbar.ivMenu.setImageResource(R.drawable.ic_arrow_back_24)
        binding.toolbar.ivMenu.thrillClickListener { onBackPressed() }
        val index = intent.getIntExtra("index", 1)
        when (index) {
            1 -> {
                binding.content05.root.isVisible = true
                binding.ivDog.setImageResource(R.mipmap.icon_barking)
                binding.toolbar.tvTitle.text = getString(R.string.index_4_5)
            }

            2 -> {
                binding.content02.root.isVisible = true
                binding.ivDog.setImageResource(R.mipmap.icon_praise)
                binding.toolbar.tvTitle.text = getString(R.string.index_4_2)
            }

            3 -> {
                binding.content01.root.isVisible = true
                binding.ivDog.setImageResource(R.mipmap.icon_food)
                binding.toolbar.tvTitle.text = getString(R.string.index_4_1)
            }

            4 -> {
                binding.content04.root.isVisible = true
                binding.ivDog.setImageResource(R.mipmap.icon_obedience)
                binding.toolbar.tvTitle.text = getString(R.string.index_4_4)
            }

            5 -> {

                binding.content03.root.isVisible = true
                binding.ivDog.setImageResource(R.mipmap.icon_bitting)
                binding.toolbar.tvTitle.text = getString(R.string.index_4_3)
            }

            6 -> {
                binding.content06.root.isVisible = true
                binding.ivDog.setImageResource(R.mipmap.icon_feeding_cats)
                binding.toolbar.tvTitle.text = getString(R.string.index_4_6)
            }

            7 -> {
                binding.content07.root.isVisible = true
                binding.ivDog.setImageResource(R.mipmap.ic_playing_with_cats)
                binding.toolbar.tvTitle.text = getString(R.string.index_4_7)
            }

            8 -> {
                binding.content08.root.isVisible = true
                binding.ivDog.setImageResource(R.mipmap.icon_petting_cats)
                binding.toolbar.tvTitle.text = getString(R.string.index_4_8)
            }

            9 -> {
                binding.content09.root.isVisible = true
                binding.ivDog.setImageResource(R.mipmap.ic_scratching_needs)
                binding.toolbar.tvTitle.text = getString(R.string.index_4_9)
            }

            else -> {}
        }
    }
}