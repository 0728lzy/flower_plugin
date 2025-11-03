package com.weini.catdog.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weini.catdog.databinding.ItemPetNoteBinding
import com.weini.catdog.model.PetNote
import java.text.SimpleDateFormat
import java.util.*

/**
 * 宠物记事本适配器
 */
class PetNotesAdapter(
    private val noteList: List<PetNote>,
    private val onNoteAction: (PetNote, String) -> Unit
) : RecyclerView.Adapter<PetNotesAdapter.NoteViewHolder>() {

    companion object {
        const val ACTION_CLICK = "click"
        const val ACTION_DELETE = "delete"
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemPetNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(noteList[position])
    }

    override fun getItemCount(): Int = noteList.size

    inner class NoteViewHolder(
        private val binding: ItemPetNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(note: PetNote) {
            binding.apply {
                // 设置标题
                tvTitle.text = note.title.ifEmpty { "无标题" }
                
                // 设置内容
                tvContent.text = note.content.ifEmpty { "无内容" }
                
                // 设置日期
                val dateStr = dateFormat.format(Date(note.createdAt))
                tvDate.text = dateStr
                
                // 设置宠物名称标签
                if (note.petName.isNotEmpty()) {
                    tvPetName.text = note.petName
                    tvPetName.visibility = View.VISIBLE
                } else {
                    tvPetName.visibility = View.GONE
                }
                
                // 点击事件
                root.setOnClickListener {
                    onNoteAction(note, ACTION_CLICK)
                }
                
                // 删除按钮点击事件
                ivDelete.setOnClickListener {
                    onNoteAction(note, ACTION_DELETE)
                }
            }
        }
    }
}