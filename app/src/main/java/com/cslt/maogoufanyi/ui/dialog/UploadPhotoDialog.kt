package com.cslt.maogoufanyi.ui.dialog

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.View
import com.bumptech.glide.Glide
import com.cslt.maogoufanyi.R
import com.cslt.maogoufanyi.databinding.DialogUploadPhotoBinding
import com.cslt.maogoufanyi.model.Pet
import com.cslt.maogoufanyi.utils.ToastUtils
import com.lxj.xpopup.core.CenterPopupView
import org.litepal.LitePal
import com.github.dhaval2404.imagepicker.ImagePicker

/**
 * 上传照片对话框
 */
class UploadPhotoDialog(
    context: Context,
    private val onPhotoSelected: (description: String, imageUri: Uri?, selectedPet: Pet?) -> Unit
) : CenterPopupView(context) {

    private lateinit var binding: DialogUploadPhotoBinding
    private var selectedImageUri: Uri? = null
    private var petList: List<Pet> = emptyList()
    private var selectedPet: Pet? = null

    override fun getImplLayoutId(): Int {
        return R.layout.dialog_upload_photo
    }

    override fun onCreate() {
        super.onCreate()
        binding = DialogUploadPhotoBinding.bind(popupImplView)
        loadPets()
        setupViews()
    }

    private fun loadPets() {
        try {
            petList = LitePal.findAll(Pet::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.show("加载宠物列表失败")
        }
    }

    private fun setupViews() {
        // 设置宠物选择下拉框
        setupPetSpinner()

        // 设置关闭按钮点击事件
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        // 设置取消按钮点击事件
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // 设置保存按钮点击事件
        binding.btnSave.setOnClickListener {
            if (validateInput()) {
                val description = binding.etDescription.text.toString().trim()
                onPhotoSelected(description, selectedImageUri, selectedPet)
                dismiss()
            }
        }

        // 设置选择文件按钮点击事件 - 使用ImagePicker
        binding.btnSelectFile.setOnClickListener {
            openImagePicker()
        }

        // 设置选择任意文件按钮点击事件 - 使用ImagePicker
        binding.btnSelectAnyFile.setOnClickListener {
            openImagePicker()
        }
    }

    private fun setupPetSpinner() {
        if (petList.isEmpty()) {
            binding.tvPetName.text = "暂无宠物"
            binding.tvPetName.isClickable = false
            return
        }

        // 创建宠物名称列表
        val petNames = petList.map { it.name }
        
        // 设置默认选中第一个宠物
        selectedPet = petList.firstOrNull()
        binding.tvPetName.text = selectedPet?.name ?: "请选择宠物"
        
        // 设置点击事件显示选择对话框
        binding.tvPetName.setOnClickListener {
            showPetSelectionDialog()
        }
    }

    private fun showPetSelectionDialog() {
        if (petList.isEmpty()) return
        
        val petNames = petList.map { it.name }.toTypedArray()
        val currentIndex = petList.indexOf(selectedPet)
        
        android.app.AlertDialog.Builder(context)
            .setTitle("选择宠物")
            .setSingleChoiceItems(petNames, currentIndex) { dialog, which ->
                selectedPet = petList[which]
                binding.tvPetName.text = selectedPet?.name
                dialog.dismiss()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    /**
     * 使用ImagePicker打开图片选择器
     */
    private fun openImagePicker() {
        if (context is Activity) {
            ImagePicker.with(context as Activity)
                .crop()                    // 启用裁剪功能
                .compress(1024)            // 压缩到1MB以下
                .maxResultSize(1080, 1080) // 最大分辨率
                .galleryOnly()             // 只从相册选择
                .start()
        } else {
            ToastUtils.show("无法打开图片选择器")
        }
    }

    /**
     * 设置选中的图片URI（从外部调用）
     */
    fun setSelectedImage(uri: Uri) {
        selectedImageUri = uri
        showImagePreview(uri)
    }

    private fun validateInput(): Boolean {
        // 检查是否选择了宠物
        if (selectedPet == null) {
            ToastUtils.show("请选择宠物")
            return false
        }
        
        // 检查是否选择了照片
        if (selectedImageUri == null) {
            ToastUtils.show("请选择照片")
            return false
        }

        // 检查描述（可选，但如果输入了要检查长度）
        val description = binding.etDescription.text.toString().trim()
        if (description.length > 200) {
            ToastUtils.show("照片描述不能超过200个字符")
            return false
        }

        return true
    }

    private fun showImagePreview(uri: Uri) {
        try {
            // 显示预览布局
            binding.layoutPhotoPreview.visibility = View.VISIBLE
            
            // 使用Glide加载图片预览
            Glide.with(context)
                .load(uri)
                .placeholder(R.drawable.ic_camera_white)
                .error(R.drawable.ic_camera_white)
                .centerCrop()
                .into(binding.ivPhotoPreview)
                
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.show("图片预览失败")
        }
    }

    companion object {
        const val REQUEST_CODE_SELECT_IMAGE = 1001
        const val REQUEST_CODE_SELECT_FILE = 1002
    }
}