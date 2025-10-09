package com.qingchu.wangmiao.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.widget.doAfterTextChanged
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.grid
import com.drake.brv.utils.setup
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.base.dj.RootFragment
import com.qingchu.wangmiao.databinding.FragmentIndex5Binding
import com.qingchu.wangmiao.databinding.Item5Binding
import com.qingchu.wangmiao.event.SimpleEvent
import com.qingchu.wangmiao.ext.getBinding
import com.qingchu.wangmiao.ext.thrillClickListener
import com.qingchu.wangmiao.ui.activity.QCVideoActivity
import com.qingchu.wangmiao.utils.lzy.LZYADSUtils
import com.qingchu.wangmiao.utils.lzy.LZYLog
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class WHIndex4Fragment : RootFragment(R.layout.fragment_index_5) {

    var _binding: FragmentIndex5Binding? = null

    val binding get() = _binding!!

    val list by lazy {  listOf(
        getString(R.string.call_3) to R.mipmap.call_3,
        getString(R.string.call_1) to R.mipmap.call_1,
        getString(R.string.call_5) to R.mipmap.call_5,
        getString(R.string.call_2) to R.mipmap.call_2,
        getString(R.string.call_4) to R.mipmap.call_4,
    ).shuffled() }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = view.getBinding()

        binding.rvList.grid(2).setup {
            addType<Pair<String, Int>>(R.layout.item_5)
            onBind {
                getBinding<Item5Binding>().apply {
                    val item = getModel<Pair<String, Int>>()
                    ivAvatar.setImageResource(item.second)
                    tvName.text = item.first
                    root.thrillClickListener {
                        val index = list.indexOf(item) + 1
                        QCVideoActivity.show(requireContext(), index)
                    }
                }
            }
        }

        binding.rvList.bindingAdapter.models = list
        binding.etSearch.doAfterTextChanged {
            it?.let {
                val keyworkds = it.toString()
                binding.rvList.bindingAdapter.models =
                    list.filter { it.first.contains(keyworkds) || keyworkds.isEmpty() }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageSimpleEvent(message: SimpleEvent) {
        if(message.simple == 3){
            LZYLog.e("simple","message simple:${message.simple}")
            LZYADSUtils("Index5Fragment",requireActivity()).loadSimpleAd4(requireActivity(),binding.feedContainerFragment5)
        }
    }
}