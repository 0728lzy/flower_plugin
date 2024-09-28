package com.pet.translator.ui.activity

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.pet.translator.R
import com.pet.translator.base.dj.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import com.pet.translator.databinding.ActivityMainBinding
import com.pet.translator.ext.thrillClickListener
import com.pet.translator.ui.fragment.Index1Fragment
import com.pet.translator.ui.fragment.Index2Fragment
import com.pet.translator.ui.fragment.Index3Fragment
import com.pet.translator.ui.fragment.Index4Fragment
import com.pet.translator.ui.fragment.Index5Fragment

class MainActivity : BaseActivity() {

    companion object {
        fun forward(context: BaseActivity) {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
            context.overridePendingTransition(0, 0)
        }
    }

    override fun getLayoutId() = R.layout.activity_main

    lateinit var binding: ActivityMainBinding

    val fragments = listOf<Fragment>(
        Index1Fragment(),
        Index2Fragment(),
        Index3Fragment(),
        Index4Fragment(),
        Index5Fragment(),
    )

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.bind(view)

        binding.ivMenu.thrillClickListener {
            binding.drawerLayout.openDrawer(binding.navView)
        }

        binding.mainPager.adapter = object : FragmentStateAdapter(this@MainActivity) {

            override fun getItemCount() = fragments.size

            override fun createFragment(position: Int) = fragments[position]

        }
        binding.mainPager.offscreenPageLimit = 1
        binding.mainPager.isUserInputEnabled = false
        binding.mainPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabChange(position)
            }
        })

        binding.bottomBar.tab1.thrillClickListener {
            tabChange(0)
        }
        binding.bottomBar.tab2.thrillClickListener {
            tabChange(1)
        }
        binding.bottomBar.tab3.thrillClickListener {
            tabChange(2)
        }
        binding.bottomBar.tab4.thrillClickListener {
            tabChange(3)
        }
        binding.bottomBar.tab5.thrillClickListener {
            tabChange(4)
        }

        binding.contentNav.llLanguage.thrillClickListener {
            LanguageActivity.forward(this, false)
        }
//        binding.contentNav.llShareNow.thrillClickListener {
//            val str1 = getString(R.string.app_name)
//            val str2 = getString(R.string.let_me_recommend)
//            val url = "https://play.google.com/store/apps/details?id=com.ruite.app.pet.translator"
//            val result = "${str1}\n${str2}\n${url}"
//            com.pet.translator.utils.ShareFileUtils.shareUrl(this, result)
//        }
    }

    private fun tabChange(index: Int) {
        binding.bottomBar.iv1.setImageResource(R.drawable.ic_index1_n)
        binding.bottomBar.iv2.setImageResource(R.drawable.ic_index2_n)
        binding.bottomBar.iv3.setImageResource(R.drawable.ic_index3_n)
        binding.bottomBar.iv4.setImageResource(R.drawable.ic_index4_n)
        binding.bottomBar.iv5.setImageResource(R.drawable.ic_index5_n)
        binding.bottomBar.tv1.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tv2.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tv3.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tv4.setTextColor(ContextCompat.getColor(this, R.color.color_777777))
        binding.bottomBar.tv5.setTextColor(ContextCompat.getColor(this, R.color.color_777777))

        when (index) {
            0 -> {
                binding.bottomBar.tv1.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv1.setImageResource(R.drawable.ic_index1_s)
            }

            1 -> {
                binding.bottomBar.tv2.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv2.setImageResource(R.drawable.ic_index2_s)
            }

            2 -> {
                binding.bottomBar.tv3.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv3.setImageResource(R.drawable.ic_index3_s)
            }

            3 -> {
                binding.bottomBar.tv4.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv4.setImageResource(R.drawable.ic_index4_s)
            }

            4 -> {
                binding.bottomBar.tv5.setTextColor(ContextCompat.getColor(this, R.color.color_main))
                binding.bottomBar.iv5.setImageResource(R.drawable.ic_index5_s)
            }
        }
        binding.mainPager.currentItem = index
    }

    override fun initStatus() {
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .statusBarColor(R.color.white)
            .init()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isOpen) {
            binding.drawerLayout.closeDrawers()
            return
        }
        super.onBackPressed()

    }
}