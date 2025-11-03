package com.weini.catdog.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.weini.catdog.R
import com.weini.catdog.base.dj.BaseActivity
import com.weini.catdog.databinding.ActivityPermissionBinding
import com.weini.catdog.ext.thrillClickListener

class WCPermissionActivity : BaseActivity() {

    companion object {
        fun forward(context: Context) {
            context.startActivity(Intent(context, WCPermissionActivity::class.java))
        }
    }

    override fun getLayoutId() = R.layout.activity_permission

    private lateinit var binding: ActivityPermissionBinding

    private var selected: Int = -1

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityPermissionBinding.bind(view)

        binding.trCheck.thrillClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@WCPermissionActivity,
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
                    this@WCPermissionActivity,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@thrillClickListener
            }
            MainActivity.forward(this@WCPermissionActivity)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 11 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            binding.swPermission.isChecked = true
        }
    }

}