package com.weini.maogou.model

import org.litepal.crud.LitePalSupport

/**
 * 宠物洗澡记录数据模型
 * 使用LitePal进行数据持久化
 */
data class PetBathRecord(
    var id: Long = 0,
    var petId: Long = 0, // 关联的宠物ID
    var petName: String = "", // 宠物名称
    var waterTemperature: Int = 38, // 水温（摄氏度）
    var bathDuration: Int = 15, // 洗澡时长（分钟）
    var shampoo: String = "", // 使用的香波
    var dryingMethod: String = "低热风", // 吹风方式（低热风、中热风、高热风、自然风干）
    var notes: String = "", // 备注信息
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
         * 根据宠物ID获取所有洗澡记录
         */
        fun getBathRecordsByPetId(petId: Long): List<PetBathRecord> {
            return org.litepal.LitePal.where("petId = ?", petId.toString())
                .order("createdAt desc")
                .find(PetBathRecord::class.java)
        }

        /**
         * 获取所有洗澡记录（按创建时间倒序）
         */
        fun getAllBathRecords(): List<PetBathRecord> {
            return org.litepal.LitePal.order("createdAt desc")
                .find(PetBathRecord::class.java)
        }

        /**
         * 删除指定宠物的所有洗澡记录
         */
        fun deleteBathRecordsByPetId(petId: Long): Int {
            return org.litepal.LitePal.deleteAll(PetBathRecord::class.java, "petId = ?", petId.toString())
        }

        /**
         * 获取最近的洗澡记录
         */
        fun getRecentBathRecords(limit: Int = 10): List<PetBathRecord> {
            return org.litepal.LitePal.order("createdAt desc")
                .limit(limit)
                .find(PetBathRecord::class.java)
        }
    }
}