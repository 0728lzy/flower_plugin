package com.appcatdog.translations.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.appcatdog.translations.R
import com.appcatdog.translations.base.dj.BaseActivity
import com.appcatdog.translations.databinding.ActivityPermissionBinding
import com.appcatdog.translations.ext.thrillClickListener

class WNCDPermissionActivity : BaseActivity() {

    companion object {
        fun forward(context: Context) {
            context.startActivity(Intent(context, WNCDPermissionActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_permission

    private lateinit var binding: ActivityPermissionBinding

    private var selected: Int = -1

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityPermissionBinding.bind(view)

        binding.trCheck.thrillClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@WNCDPermissionActivity,
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
                    this@WNCDPermissionActivity,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@thrillClickListener
            }
            WNCDMainActivity.forward(this@WNCDPermissionActivity)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            binding.swPermission.isChecked = true
        }
    }

}