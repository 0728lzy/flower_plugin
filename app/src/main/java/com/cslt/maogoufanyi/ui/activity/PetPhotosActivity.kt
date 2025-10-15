package com.cslt.maogoufanyi.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.lxj.xpopup.XPopup
import com.cslt.maogoufanyi.R
import com.cslt.maogoufanyi.base.dj.BaseActivity
import com.cslt.maogoufanyi.databinding.ActivityPetPhotosBinding
import com.cslt.maogoufanyi.model.Pet
import com.cslt.maogoufanyi.model.PetPhoto
import com.cslt.maogoufanyi.ui.adapter.PetPhotosAdapter
import com.cslt.maogoufanyi.ui.dialog.UploadPhotoDialog
import org.litepal.LitePal
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.core.BasePopupView
import com.cslt.maogoufanyi.utils.lzy.LZYADSUtils

class PetPhotosActivity : BaseActivity() {

    companion object {
        fun forward(context: Context) {
            val intent = Intent(context, PetPhotosActivity::class.java)
            context.startActivity(intent)
        }
        
        private const val REQUEST_PERMISSION_CODE = 1001
    }

    override fun getLayoutId() = R.layout.activity_pet_photos
    private lateinit var lzyadsUtils: LZYADSUtils
    private lateinit var binding: ActivityPetPhotosBinding
    private lateinit var photosAdapter: PetPhotosAdapter
    private var photosList = mutableListOf<PetPhoto>()
    private var currentUploadDialog: UploadPhotoDialog? = null

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityPetPhotosBinding.bind(view)
        
        initViews()
        setupRecyclerView()
        loadPhotos()
    }
    override fun initStatus() {
        ImmersionBar.with(this)
            .transparentStatusBar()  //透明状态栏，不写默认透明色
            .init()
    }
    private fun initViews() {
        // 设置标题

        // 返回按钮点击事件
        binding.ivBack.setOnClickListener {
            finish()
        }
        
        // 上传照片按钮点击事件
        binding.btnUploadPhoto.setOnClickListener {
            showUploadPhotoDialog()
        }
        lzyadsUtils=LZYADSUtils("PetNotesActivity",this@PetPhotosActivity)
        lzyadsUtils.showAdCpTurn()
        lzyadsUtils.loadSimpleAdTurn(binding.feedContainer,-1)
    }

    private fun setupRecyclerView() {
        photosAdapter = PetPhotosAdapter(photosList) { photo, action ->
            when (action) {
                "click" -> {
                    // 点击照片，可以实现预览功能
                    // TODO: 实现照片预览
                }
                "delete" -> {
                    deletePhoto(photo)
                }
            }
        }
        
        binding.rvPhotos.apply {
            layoutManager = GridLayoutManager(this@PetPhotosActivity, 2)
            adapter = photosAdapter
        }
    }

    private fun loadPhotos() {
        try {
            photosList.clear()
            photosList.addAll(LitePal.order("createdAt desc").find(PetPhoto::class.java))
            photosAdapter.notifyDataSetChanged()
            updateEmptyState()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateEmptyState() {
        if (photosList.isEmpty()) {
            // 显示空状态
            binding.layoutEmpty.visibility=View.VISIBLE
        } else {
            // 隐藏空状态
            binding.layoutEmpty.visibility=View.GONE
        }
    }
    var inputPopupView: BasePopupView? = null
    private fun showUploadPhotoDialog() {
        if (inputPopupView?.isShow == true) {
            return
        }
        val dialog = UploadPhotoDialog(this) { description, selectedImageUri, selectedPet ->
            if (selectedImageUri != null && selectedPet != null) {
                savePhotoToDatabase(selectedImageUri, description, selectedPet)
            }
        }
        
        currentUploadDialog = dialog

        inputPopupView = XPopup.Builder(this@PetPhotosActivity)
            .autoOpenSoftInput(false)
            .autoDismiss(false)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .asCustom(dialog)
            .show()
    }

    private fun savePhotoToDatabase(imageUri: Uri, description: String, selectedPet: Pet) {
        try {
            // 生成文件名
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "PET_${selectedPet.id}_${timeStamp}.jpg"
            
            // 保存到应用内部存储的宠物专属目录
            val internalDir = File(filesDir, "pet_${selectedPet.id}_photos")
            if (!internalDir.exists()) {
                internalDir.mkdirs()
            }
            val photoFile = File(internalDir, fileName)
            
            contentResolver.openInputStream(imageUri)?.use { input ->
                photoFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            
            // 保存到数据库
            val petPhoto = PetPhoto(
                petId = selectedPet.id,
                petName = selectedPet.name, // 添加宠物名称
                photoPath = photoFile.absolutePath,
                description = description,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            
            if (petPhoto.save()) {
                Toast.makeText(this, "照片保存成功", Toast.LENGTH_SHORT).show()
                loadPhotos() // 重新加载照片列表
                lzyadsUtils.showAdCpTurn()
            } else {
                Toast.makeText(this, "照片保存失败", Toast.LENGTH_SHORT).show()
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "保存照片时出错: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePhoto(photo: PetPhoto) {
        try {
            // 删除本地文件
            val file = File(photo.photoPath)
            if (file.exists()) {
                file.delete()
            }
            
            // 从数据库删除
            if (photo.delete() > 0) {
                Toast.makeText(this, "照片删除成功", Toast.LENGTH_SHORT).show()
                loadPhotos() // 重新加载照片列表
                lzyadsUtils.showAdCpTurn()
            } else {
                Toast.makeText(this, "照片删除失败", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "删除照片时出错: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermissions(): Boolean {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        
        return permissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        
        ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                // 权限已授予，可以继续操作
                Toast.makeText(this, "权限已授予", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "需要存储权限才能保存照片", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        // 处理ImagePicker的结果
        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            if (uri != null) {
                // 将选中的图片URI传递给当前的UploadPhotoDialog
                currentUploadDialog?.setSelectedImage(uri)
            }
        }
    }
}