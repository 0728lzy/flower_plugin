package com.qingchu.wangmiao.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.qingchu.wangmiao.R
import com.qingchu.wangmiao.base.dj.BaseActivity
import com.qingchu.wangmiao.databinding.ActivityPermissionBinding
import com.qingchu.wangmiao.ext.thrillClickListener

class QCPermissionActivity : BaseActivity() {

    companion object {
        fun forward(context: Context) {
            context.startActivity(Intent(context, QCPermissionActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_permission

    private lateinit var binding: ActivityPermissionBinding

    private var selected: Int = -1

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityPermissionBinding.bind(view)

        binding.trCheck.thrillClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@QCPermissionActivity,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 11);
                }
                return@thrillClickListener
            }
        }
        binding.btnContinue2.thrillClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@QCPermissionActivity,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@thrillClickListener
            }
            MainActivity.forward(this@QCPermissionActivity)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            binding.swPermission.isChecked = true
        }
    }

}