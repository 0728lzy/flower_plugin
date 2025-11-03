package com.weini.catdog.model

import org.litepal.crud.LitePalSupport

/**
 * 宠物照片数据模型
 * 使用LitePal进行数据持久化
 */
data class PetPhoto(
    var id: Long = 0,
    var petId: Long = 0, // 关联的宠物ID
    var petName: String = "", // 宠物名称
    var photoPath: String = "", // 照片本地路径
    var photoUrl: String = "", // 照片网络URL（如果有）
    var description: String = "", // 照片描述
    var createdAt: Long = System.currentTimeMillis(), // 创建时间
    var updatedAt: Long = System.currentTimeMillis() // 更新时间
) : LitePalSupport() {

    /**
     * 更新时间戳
     */
    fun updateTimestamp() {
        updatedAt = System.currentTimeMillis()
    }

    companion object {
        /**
         * 根据宠物ID获取所有照片
         */
        fun getPhotosByPetId(petId: Long): List<PetPhoto> {
            return org.litepal.LitePal.where("petId = ?", petId.toString())
                .order("createdAt desc")
                .find(PetPhoto::class.java)
        }

        /**
         * 删除指定宠物的所有照片
         */
        fun deletePhotosByPetId(petId: Long): Int {
            return org.litepal.LitePal.deleteAll(PetPhoto::class.java, "petId = ?", petId.toString())
        }
    }
}