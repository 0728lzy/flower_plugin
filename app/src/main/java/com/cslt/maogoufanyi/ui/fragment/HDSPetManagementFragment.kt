package com.cslt.maogoufanyi.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.lxj.xpopup.XPopup
import com.cslt.maogoufanyi.AppConst
import com.cslt.maogoufanyi.R
import com.cslt.maogoufanyi.base.dj.RootFragment
import com.cslt.maogoufanyi.csj.AdFeedSimpleThreeUtils
import com.cslt.maogoufanyi.csj.ZYMAllAdsUtils
import com.cslt.maogoufanyi.databinding.FragmentPetManagementBinding
import com.cslt.maogoufanyi.event.SimpleEvent
import com.cslt.maogoufanyi.model.Pet
import com.cslt.maogoufanyi.ui.activity.HDSPetBathActivity
import com.cslt.maogoufanyi.ui.activity.HDSPetNotesActivity
import com.cslt.maogoufanyi.ui.activity.HDSPetPhotosActivity
import com.cslt.maogoufanyi.ui.adapter.PetAdapter
import com.cslt.maogoufanyi.ui.dialog.AddPetDialog
import com.cslt.maogoufanyi.utils.ToastUtils

import com.cslt.maogoufanyi.utils.lzy.LZYLog
import org.litepal.LitePal
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 宠物管理Fragment
 * 实现宠物信息的增删改查功能
 */
class HDSPetManagementFragment : RootFragment(R.layout.fragment_pet_management) {

    private var _binding: FragmentPetManagementBinding? = null
    private val binding get() = _binding!!

    private lateinit var petAdapter: PetAdapter
    private val petList = mutableListOf<Pet>()

    private val fragmentScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private lateinit var dialog: AddPetDialog

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
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false) // 2列网格布局
            adapter = petAdapter
        }

        // 添加宠物按钮点击事件
        binding.btnAddPet.setOnClickListener {
            showAddPetDialog()
        }

        binding.ymAddDangan.setOnClickListener{
            showAddPetDialog()
        }

        // 管理功能点击事件
        binding.layoutPetAlbum.setOnClickListener {
            if (petList.isEmpty()) {
                ToastUtils.show("请先添加宠物")
                return@setOnClickListener
            }

            // 如果只有一只宠物，直接跳转到该宠物的相册
            HDSPetPhotosActivity.forward(requireContext())

        }

        binding.layoutPetNotes.setOnClickListener {
            if (petList.isEmpty()) {
                ToastUtils.show("请先添加宠物")
                return@setOnClickListener
            }

            // 如果只有一只宠物，直接跳转到该宠物的相册
            HDSPetNotesActivity.forward(requireContext())
        }

        binding.layoutBathRecord.setOnClickListener {
            if (petList.isEmpty()) {
                ToastUtils.show("请先添加宠物")
                return@setOnClickListener
            }

            // 如果只有一只宠物，直接跳转到该宠物的相册
            HDSPetBathActivity.forward(requireContext())
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
        dialog = AddPetDialog(requireContext(),this) { pet ->
            addPet(pet)
        }
        XPopup.Builder(requireContext())
            .asCustom(dialog)
            .show()
    }

    /**
     * 显示编辑宠物对话框
     */
    private fun showEditPetDialog(pet: Pet) {
        dialog = AddPetDialog(requireContext(),this, pet) { updatedPet ->
            updatePet(updatedPet)
        }
        XPopup.Builder(requireContext())
            .asCustom(dialog)
            .show()
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
     * 显示宠物选择对话框
     */
    private fun showPetSelectionDialog() {
        val petNames = petList.map { it.name }.toTypedArray()

        XPopup.Builder(requireContext())
            .asBottomList("选择宠物", petNames) { position, text ->
                com.cslt.maogoufanyi.ui.activity.HDSPetPhotosActivity.forward(requireContext())
            }
            .show()
    }

    /**
     * 显示宠物详情
     */
    private fun showPetDetail(pet: Pet) {
        // 点击宠物卡片跳转到宠物相册
        com.cslt.maogoufanyi.ui.activity.HDSPetPhotosActivity.forward(requireContext())
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
                    ZYMAllAdsUtils.showAdCpTurnTab(requireActivity(),"CP")
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
                        ZYMAllAdsUtils.showAdCpTurnTab(requireActivity(),"CP")
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
                        ZYMAllAdsUtils.showAdCpTurnTab(requireActivity(),"CP")
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
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageSimpleEvent(message: SimpleEvent) {
        if (message.simple == 2) { // 使用新的事件ID避免冲突
            LZYLog.e("simple", "DogLanguageFragment message simple:${message.simple}")


            ZYMAllAdsUtils.loadSimpleAll(requireActivity(),"信息",binding.feedContainerMg)
        }
    }



    companion object {
        fun newInstance(): HDSPetManagementFragment {
            return HDSPetManagementFragment()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            if (uri != null) {
                dialog.setSelectedImage(uri)
            }
        }
    }

}