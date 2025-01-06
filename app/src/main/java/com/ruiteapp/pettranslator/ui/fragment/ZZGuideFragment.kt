package com.ruiteapp.pettranslator.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ruiteapp.pettranslator.R
import com.ruiteapp.pettranslator.ext.thrillClickListener
import com.ruiteapp.pettranslator.ui.activity.ZZGuideActivity

class ZZGuideFragment(val index: Int) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = when (index) {
            1 -> inflater.inflate(R.layout.fragment_guide_1,container,false)
            2 -> inflater.inflate(R.layout.fragment_guide_2,container,false)
            3 -> inflater.inflate(R.layout.fragment_guide_3,container,false)
            4 -> inflater.inflate(R.layout.fragment_guide_4,container,false)
            else -> inflater.inflate(R.layout.fragment_guide_1,container,false)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.tv_start).thrillClickListener {
            (requireActivity() as? ZZGuideActivity)?.next()
        }
    }

}