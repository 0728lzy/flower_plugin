package com.weini.maogou.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.weini.maogou.R
import com.weini.maogou.base.dj.RootFragment
import com.weini.maogou.databinding.FragmentPetManagementBinding
import com.weini.maogou.model.Pet
import com.weini.maogou.ui.adapter.PetAdapter
import com.weini.maogou.ui.dialog.AddPetDialog
import com.weini.maogou.utils.ToastUtils
import org.litepal.LitePal
import kotlinx.coroutines.*

/**
 * 宠物管理Fragment
 * 实现宠物信息的增删改查功能
 */
class PetManagementFragment : RootFragment(R.layout.fragment_pet_management) {

    private var _binding: FragmentPetManagementBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var petAdapter: PetAdapter
    private val petList = mutableListOf<Pet>()
    
    private val fragmentScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun initView(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentPetManagementBinding.bind(view)
        initViews()
        loadPets()
    }

    private fun initViews() {
        // 初始化RecyclerView
        petAdapter = PetAdapter(petList) { pet, action ->
            when (action) {
                PetAdapter.ACTION_CLICK -> {
                    // 点击宠物卡片，显示详情
                    showPetDetail(pet)
                }
                PetAdapter.ACTION_EDIT -> {
                    // 编辑宠物
                    showEditPetDialog(pet)
                }
                PetAdapter.ACTION_DELETE -> {
                    // 删除宠物
                    showDeleteConfirmDialog(pet)
                }
            }
        }
        
        binding.rvPets.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = petAdapter
        }

        // 添加宠物按钮点击事件
        binding.btnAddPet.setOnClickListener {
            showAddPetDialog()
        }

        // 管理功能点击事件
        binding.layoutPetAlbum.setOnClickListener {
            ToastUtils.show("宠物相册功能开发中...")
        }

        binding.layoutPetNotes.setOnClickListener {
            ToastUtils.show("记事本功能开发中...")
        }

        binding.layoutBathRecord.setOnClickListener {
            ToastUtils.show("洗澡记录功能开发中...")
        }
    }

    /**
     * 加载宠物列表
     */
    private fun loadPets() {
        showLoading(true)
        
        fragmentScope.launch {
            try {
                val pets = withContext(Dispatchers.IO) {
                    LitePal.findAll(Pet::class.java)
                }
                
                petList.clear()
                petList.addAll(pets)
                petAdapter.notifyDataSetChanged()
                
                updateEmptyState()
                
            } catch (e: Exception) {
                e.printStackTrace()
                ToastUtils.show("加载宠物列表失败: ${e.message}")
            } finally {
                showLoading(false)
            }
        }
    }

    /**
     * 显示添加宠物对话框
     */
    private fun showAddPetDialog() {
        val dialog = AddPetDialog(requireContext()) { pet ->
            addPet(pet)
        }
        dialog.show()
    }

    /**
     * 显示编辑宠物对话框
     */
    private fun showEditPetDialog(pet: Pet) {
        val dialog = AddPetDialog(requireContext(), pet) { updatedPet ->
            updatePet(updatedPet)
        }
        dialog.show()
    }

    /**
     * 显示删除确认对话框
     */
    private fun showDeleteConfirmDialog(pet: Pet) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("删除宠物")
            .setMessage("确定要删除 ${pet.name} 吗？此操作不可撤销。")
            .setPositiveButton("删除") { _, _ ->
                deletePet(pet)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    /**
     * 显示宠物详情
     */
    private fun showPetDetail(pet: Pet) {
        // TODO: 实现宠物详情页面
        ToastUtils.show("宠物详情页面开发中...")
    }

    /**
     * 添加宠物
     */
    private fun addPet(pet: Pet) {
        fragmentScope.launch {
            try {
                val success = withContext(Dispatchers.IO) {
                    pet.save()
                }
                
                if (success) {
                    petList.add(pet)
                    petAdapter.notifyItemInserted(petList.size - 1)
                    updateEmptyState()
                    ToastUtils.show("添加宠物成功")
                } else {
                    ToastUtils.show("添加宠物失败")
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
                ToastUtils.show("添加宠物失败: ${e.message}")
            }
        }
    }

    /**
     * 更新宠物信息
     */
    private fun updatePet(pet: Pet) {
        fragmentScope.launch {
            try {
                val success = withContext(Dispatchers.IO) {
                    pet.updateTimestamp()
                    pet.save()
                }
                
                if (success) {
                    val index = petList.indexOfFirst { it.id == pet.id }
                    if (index != -1) {
                        petList[index] = pet
                        petAdapter.notifyItemChanged(index)
                        ToastUtils.show("更新宠物信息成功")
                    }
                } else {
                    ToastUtils.show("更新宠物信息失败")
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
                ToastUtils.show("更新宠物信息失败: ${e.message}")
            }
        }
    }

    /**
     * 删除宠物
     */
    private fun deletePet(pet: Pet) {
        fragmentScope.launch {
            try {
                val success = withContext(Dispatchers.IO) {
                    LitePal.delete(Pet::class.java, pet.id)
                }
                
                if (success > 0) {
                    val index = petList.indexOfFirst { it.id == pet.id }
                    if (index != -1) {
                        petList.removeAt(index)
                        petAdapter.notifyItemRemoved(index)
                        updateEmptyState()
                        ToastUtils.show("删除宠物成功")
                    }
                } else {
                    ToastUtils.show("删除宠物失败")
                }
                
            } catch (e: Exception) {
                e.printStackTrace()
                ToastUtils.show("删除宠物失败: ${e.message}")
            }
        }
    }

    /**
     * 更新空状态显示
     */
    private fun updateEmptyState() {
        if (petList.isEmpty()) {
            binding.rvPets.visibility = View.GONE
            binding.layoutEmptyState.visibility = View.VISIBLE
        } else {
            binding.rvPets.visibility = View.VISIBLE
            binding.layoutEmptyState.visibility = View.GONE
        }
    }

    /**
     * 显示/隐藏加载状态
     */
    private fun showLoading(show: Boolean) {
        binding.progressLoading.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentScope.cancel()
        _binding = null
    }

    companion object {
        fun newInstance(): PetManagementFragment {
            return PetManagementFragment()
        }
    }
}