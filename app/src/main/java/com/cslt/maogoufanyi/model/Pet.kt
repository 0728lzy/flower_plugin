package com.cslt.maogoufanyi.model

import org.litepal.crud.LitePalSupport

/**
 * 宠物数据模型
 * 使用LitePal进行数据持久化
 */
data class Pet(
    var id: Long = 0,
    var name: String = "",
    var type: String = "", // "cat" 或 "dog"
    var breed: String = "", // 品种
    var age: Double = 0.0, // 年龄（支持小数，如1.5岁）
    var weight: Double = 0.0, // 体重（公斤）
    var gender: String = "", // "male" 或 "female"
    var color: String = "", // 毛色
    var avatar: String = "", // 头像路径
    var birthday: String = "", // 生日 (yyyy-MM-dd格式)
    var adoptionDate: String = "", // 领养日期 (yyyy-MM-dd格式)
    var description: String = "", // 描述
    var isNeutered: Boolean = false, // 是否绝育
    var vaccineStatus: String = "", // 疫苗状态
    var healthStatus: String = "", // 健康状态
    var favoriteFood: String = "", // 喜欢的食物
    var favoriteActivity: String = "", // 喜欢的活动
    var notes: String = "", // 备注
    var createdAt: Long = System.currentTimeMillis(), // 创建时间
    var updatedAt: Long = System.currentTimeMillis() // 更新时间
) : LitePalSupport() {

    companion object {
        const val TYPE_CAT = "cat"
        const val TYPE_DOG = "dog"
        const val GENDER_MALE = "male"
        const val GENDER_FEMALE = "female"
        
        // 预定义的品种列表
        val CAT_BREEDS = listOf(
            "英国短毛猫", "美国短毛猫", "波斯猫", "暹罗猫", "布偶猫", 
            "苏格兰折耳猫", "俄罗斯蓝猫", "缅因猫", "阿比西尼亚猫", "其他"
        )
        
        val DOG_BREEDS = listOf(
            "金毛寻回犬", "拉布拉多", "哈士奇", "萨摩耶", "边境牧羊犬",
            "德国牧羊犬", "泰迪", "比熊", "柯基", "柴犬", "其他"
        )
        
        // 预定义的毛色
        val COLORS = listOf(
            "白色", "黑色", "棕色", "灰色", "橙色", "花色", "三花", "其他"
        )
        
        // 疫苗状态
        val VACCINE_STATUS = listOf(
            "已完成", "进行中", "未开始", "需要补种"
        )
        
        // 健康状态
        val HEALTH_STATUS = listOf(
            "健康", "轻微不适", "需要关注", "治疗中"
        )
        
        /**
         * 获取所有宠物列表
         */
        fun getAllPets(): List<Pet> {
            return org.litepal.LitePal.findAll(Pet::class.java)
        }
    }

    /**
     * 获取年龄显示文本
     */
    fun getAgeText(): String {
        return if (age < 1) {
            "${(age * 12).toInt()}个月"
        } else {
            "${age}岁"
        }
    }

    /**
     * 获取体重显示文本
     */
    fun getWeightText(): String {
        return "${weight}kg"
    }

    /**
     * 获取类型显示文本
     */
    fun getTypeText(): String {
        return when (type) {
            TYPE_CAT -> "猫咪"
            TYPE_DOG -> "狗狗"
            else -> "未知"
        }
    }

    /**
     * 获取性别显示文本
     */
    fun getGenderText(): String {
        return when (gender) {
            GENDER_MALE -> "公"
            GENDER_FEMALE -> "母"
            else -> "未知"
        }
    }

    /**
     * 获取绝育状态显示文本
     */
    fun getNeuteredText(): String {
        return if (isNeutered) "已绝育" else "未绝育"
    }

    /**
     * 更新时间戳
     */
    fun updateTimestamp() {
        updatedAt = System.currentTimeMillis()
    }

    /**
     * 验证数据完整性
     */
    fun isValid(): Boolean {
        return name.isNotBlank() && 
               type.isNotBlank() && 
               breed.isNotBlank() &&
               age >= 0 &&
               weight >= 0
    }

    /**
     * 获取默认头像资源
     */
    fun getDefaultAvatar(): String {
        return when (type) {
            TYPE_CAT -> "cat_images/cat_01.webp"
            TYPE_DOG -> "dog_images/dog_happy.jpeg"
            else -> ""
        }
    }
}