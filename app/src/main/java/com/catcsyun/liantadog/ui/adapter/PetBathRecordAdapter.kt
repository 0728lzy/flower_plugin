package com.catcsyun.liantadog.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.catcsyun.liantadog.databinding.ItemPetBathRecordBinding
import com.catcsyun.liantadog.model.PetBathRecord
import java.text.SimpleDateFormat
import java.util.*

/**
 * 宠物洗澡记录适配器
 */
class PetBathRecordAdapter(
    private val bathRecords: List<PetBathRecord>,
    private val onItemAction: (PetBathRecord, String) -> Unit
) : RecyclerView.Adapter<PetBathRecordAdapter.BathRecordViewHolder>() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BathRecordViewHolder {
        val binding = ItemPetBathRecordBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BathRecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BathRecordViewHolder, position: Int) {
        holder.bind(bathRecords[position])
    }

    override fun getItemCount(): Int = bathRecords.size

    inner class BathRecordViewHolder(
        private val binding: ItemPetBathRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: PetBathRecord) {
            with(binding) {
                // 设置标题（宠物名称 + 洗澡记录）
                tvBathTitle.text = if (record.petName.isNotEmpty()) {
                    "${record.petName}洗澡记录"
                } else {
                    "洗澡记录"
                }

                // 设置日期
                tvBathDate.text = dateFormat.format(Date(record.createdAt))

                // 设置洗澡详情
                tvShampoo.text = record.shampoo.ifEmpty { "未记录" }
                tvDuration.text = "${record.bathDuration}分钟"
                tvTemperature.text = "${record.waterTemperature}°C"
                tvDryingMethod.text = record.dryingMethod

                // 设置备注
                tvNotes.text = record.notes.ifEmpty { "无备注" }

                // 设置点击事件
                root.setOnClickListener {
                    // 可以添加点击查看详情的逻辑
                }

                // 编辑按钮
                btnEdit.setOnClickListener {
                    onItemAction(record, "edit")
                }

                // 删除按钮
                btnDelete.setOnClickListener {
                    onItemAction(record, "delete")
                }
            }
        }
    }
}