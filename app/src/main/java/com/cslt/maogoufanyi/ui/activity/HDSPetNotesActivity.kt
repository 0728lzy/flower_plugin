package com.cslt.maogoufanyi.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cslt.maogoufanyi.R
import com.cslt.maogoufanyi.base.dj.BaseActivity
import com.cslt.maogoufanyi.databinding.ActivityPetNotesBinding
import com.cslt.maogoufanyi.model.Pet
import com.cslt.maogoufanyi.model.PetNote
import com.cslt.maogoufanyi.ui.adapter.PetNotesAdapter
import com.cslt.maogoufanyi.ui.dialog.AddEditNoteDialog
import com.gyf.immersionbar.ImmersionBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.cslt.maogoufanyi.utils.lzy.LZYADSUtils

class HDSPetNotesActivity : BaseActivity() {

    companion object {
        fun forward(context: Context) {
            val intent = Intent(context, HDSPetNotesActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_pet_notes
    private lateinit var lzyadsUtils: LZYADSUtils
    private lateinit var binding: ActivityPetNotesBinding
    private lateinit var notesAdapter: PetNotesAdapter
    private var notesList = mutableListOf<PetNote>()

    override fun initView(view: View, savedInstanceState: Bundle?) {
        binding = ActivityPetNotesBinding.bind(view)
        
        initViews()
        setupRecyclerView()
        loadAllNotes()
    }

    override fun initStatus() {
        ImmersionBar.with(this)
            .transparentStatusBar()
            .init()
    }

    private fun initViews() {
        // 返回按钮点击事件
        binding.ivBack.setOnClickListener {
            finish()
        }
        
        // 添加记录按钮点击事件
        binding.btnAddNote.setOnClickListener {
            showAddEditNoteDialog()
        }
        lzyadsUtils=LZYADSUtils("PetNotesActivity",this@HDSPetNotesActivity)
        lzyadsUtils.showAdCpTurn()
        lzyadsUtils.loadSimpleAdTurn(binding.feedContainer,-1)
    }

    private fun setupRecyclerView() {
        notesAdapter = PetNotesAdapter(notesList) { note, action ->
            when (action) {
                "click" -> {
                    // 点击记事，编辑记事
                    showAddEditNoteDialog(note)
                }
                "delete" -> {
                    deleteNote(note)
                }
                "toggle_status" -> {
                    toggleNoteStatus(note)
                }
            }
        }
        
        binding.rvNotes.apply {
            layoutManager = LinearLayoutManager(this@HDSPetNotesActivity)
            adapter = notesAdapter
        }
    }

    /**
     * 加载所有记录数据
     * 直接获取全部数据，不进行筛选
     */
    private fun 
            loadAllNotes() {
        try {
            notesList.clear()
            
            // 确保数据库已初始化
            if (!org.litepal.LitePal.isExist(PetNote::class.java)) {
                // 如果数据库不存在，创建数据库
                org.litepal.LitePal.getDatabase()
            }
            
            // 直接获取所有记录，按创建时间倒序排列
            val allNotes = PetNote.getAllNotes()
            notesList.addAll(allNotes)
            
            // 通知适配器数据已更新
            notesAdapter.notifyDataSetChanged()
            
            // 更新空状态显示
            updateEmptyState()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "加载记录失败: ${e.message}", Toast.LENGTH_SHORT).show()
            
            // 如果是数据库相关错误，尝试重新初始化
            if (e.message?.contains("database", ignoreCase = true) == true ||
                e.message?.contains("table", ignoreCase = true) == true) {
                try {
                    // 强制重新创建数据库
                    org.litepal.LitePal.deleteDatabase("mobanglist")
                    org.litepal.LitePal.getDatabase()
                    Toast.makeText(this, "数据库已重新初始化，请重试", Toast.LENGTH_LONG).show()
                } catch (initException: Exception) {
                    initException.printStackTrace()
                    Toast.makeText(this, "数据库初始化失败: ${initException.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * 更新空状态显示
     */
    private fun updateEmptyState() {
        if (notesList.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.rvNotes.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.rvNotes.visibility = View.VISIBLE
        }
    }
    var inputPopupView: BasePopupView? = null
    /**
     * 显示添加/编辑记录对话框
     */
    private fun showAddEditNoteDialog(note: PetNote? = null) {
        if (inputPopupView?.isShow == true) {
            return
        }
        // 获取所有宠物列表
        val pets = Pet.getAllPets()
        
        if (pets.isEmpty()) {
            Toast.makeText(this, "请先添加宠物信息", Toast.LENGTH_SHORT).show()
            return
        }
        
        val dialog = AddEditNoteDialog(
            context = this,
            pets = pets,
            editingNote = note,
            onNoteActionListener = object : AddEditNoteDialog.OnNoteActionListener {
                override fun onNoteSaved(note: PetNote) {
                    // 保存记录到数据库
                    if (note.save()) {
                        // 重新加载所有数据
                        loadAllNotes()
                        val message = if (note.id == 0L) "记录添加成功" else "记录更新成功"
                        lzyadsUtils.showAdCpTurn()
                        Toast.makeText(this@HDSPetNotesActivity, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@HDSPetNotesActivity, "保存失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        inputPopupView = XPopup.Builder(this@HDSPetNotesActivity)
            .autoOpenSoftInput(false)
            .autoDismiss(false)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .asCustom(dialog)
            .show()
    }

    /**
     * 删除记录
     */
    private fun deleteNote(note: PetNote) {
        try {
            if (note.delete() > 0) {
                Toast.makeText(this, "记录删除成功", Toast.LENGTH_SHORT).show()
                // 重新加载所有数据
                loadAllNotes()
                lzyadsUtils.showAdCpTurn()
            } else {
                Toast.makeText(this, "记录删除失败", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "删除记录时出错: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 切换记录完成状态
     */
    private fun toggleNoteStatus(note: PetNote) {
        try {
            if (note.isCompleted) {
                note.markAsIncomplete()
            } else {
                note.markAsCompleted()
            }
            
            if (note.save()) {
                // 只更新当前项，提高效率
                val position = notesList.indexOf(note)
                if (position != -1) {
                    notesAdapter.notifyItemChanged(position)
                }
                lzyadsUtils.showAdCpTurn()
                val message = if (note.isCompleted) "已标记为完成" else "已标记为未完成"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "状态更新失败", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "更新状态时出错: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}