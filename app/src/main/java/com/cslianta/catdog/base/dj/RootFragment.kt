package com.cslianta.catdog.base.dj

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment


abstract class RootFragment(@LayoutRes private val contentId: Int) : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(contentId, container, false)
        initView(view, savedInstanceState)
        return view
    }

    abstract fun initView(view: View, savedInstanceState: Bundle?)

    fun requireRootActivity(): RootActivity {
        return requireActivity() as RootActivity
    }

    fun setResultAndFinish(resultCode: Int = Activity.RESULT_OK, intent: Intent) {
        requireActivity().setResult(resultCode, intent)
        finishActivity()
    }

    open fun onBackPressed() {
        requireActivity().onBackPressed()
    }

    fun finishActivity() {
        val activity = requireRootActivity()
        if (!activity.isFinishing && !activity.isDestroyed)
            activity.finish()
    }
}