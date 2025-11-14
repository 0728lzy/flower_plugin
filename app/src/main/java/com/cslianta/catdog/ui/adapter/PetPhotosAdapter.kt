package com.cslianta.catdog.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cslianta.catdog.R
import com.cslianta.catdog.databinding.ItemPetPhotoBinding
import com.cslianta.catdog.model.PetPhoto
import java.text.SimpleDateFormat
import java.util.*

/**
 * 共享相册照片列表适配器
 */
class PetPhotosAdapter(
    private val photoList: List<PetPhoto>,
    private val onPhotoAction: (PetPhoto, String) -> Unit
) : RecyclerView.Adapter<PetPhotosAdapter.PhotoViewHolder>() {

    companion object {
        const val ACTION_CLICK = "click"
        const val ACTION_DELETE = "delete"
    }

    private val dateFormat = SimpleDateFormat("MM月dd日", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = ItemPetPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photoList[position])
    }

    override fun getItemCount(): Int = photoList.size

    inner class PhotoViewHolder(
        private val binding: ItemPetPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: PetPhoto) {
            with(binding) {
                // 加载照片
                if (photo.photoPath.isNotEmpty()) {
                    Glide.with(itemView.context)
                        .load(photo.photoPath)
                        .placeholder(R.color.color_9C27B0)
                        .error(R.color.color_9C27B0)
                        .into(ivPhoto)
                    
                    // 隐藏相机占位符
                    ivCameraPlaceholder.visibility = View.GONE
                    // 显示删除按钮
                    ivDelete.visibility = View.VISIBLE
                } else {
                    // 显示相机占位符
                    ivCameraPlaceholder.visibility = View.VISIBLE
                    // 隐藏删除按钮
                    ivDelete.visibility = View.GONE
                }

                // 设置宠物名称和日期
                val dateStr = dateFormat.format(Date(photo.createdAt))
                val displayText = if (photo.petName.isNotEmpty()) {
                    "${photo.petName}·$dateStr"
                } else {
                    "共享相册·$dateStr"
                }
                tvPetName.text = displayText

                // 照片点击事件
                root.setOnClickListener {
                    onPhotoAction(photo, ACTION_CLICK)
                }

                // 删除按钮点击事件
                ivDelete.setOnClickListener {
                    onPhotoAction(photo, ACTION_DELETE)
                }
            }
        }
    }
}