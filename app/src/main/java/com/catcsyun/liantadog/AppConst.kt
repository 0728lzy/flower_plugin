package com.catcsyun.liantadog

import android.content.Context
import android.text.TextUtils
import com.catcsyun.liantadog.entity.Index1Entity
import com.catcsyun.liantadog.entity.LanguageEntity


object AppConst {

    //----------------------------模板常量（常修改）----------------------------start
    @JvmField
    var CHANNEL = BuildConfig.APP_CHANNEL//用户协议MK
    @JvmField
    var DJ_APP_ID = "a92c610a89339805905121471ac23325ed1aad"
    const val UM_APP_ID = "66f79ecb61c3b13bde90b57a"  //沿用 猫狗交流翻译器
    const val TAG = "ad_log"


    //GroMore
    @JvmField var  Ad_ID = "" //穿山甲广告APP ID
    @JvmField var  GMCPAd_ID_IN = "" //插屏应用内
    @JvmField var  GMCPAd_ID_IN_TWO = "" //插屏应用内2
    @JvmField var  GMCPAd_THREE_ID_IN = "" //插屏应用内 3
    @JvmField var  GMCPAd_FOUR_ID_IN = "" //插屏应用内 4
    @JvmField var  FEEDSIMPLE_ID_ONE = "" //信息流首页1
    @JvmField var  FEEDSIMPLE_ID_TWO = "" //信息流首页2
    @JvmField var  FEEDSIMPLE_ID_THREE = "" //信息流首页3
    @JvmField var  FEEDSIMPLE_ID_FOUR = "" //信息流首页4
    @JvmField var  GMSPAd_ID = "" //开屏ID
    @JvmField var  GMDDAd_ID = "" //快报兜底开屏id
    @JvmField var  GMSPAd_TWO_ID = "" //开屏ID 2
    @JvmField var  GMDDAd_TWO_ID = "" //快报兜底开屏id 2
    @JvmField var  GMRDAd_ID_IN = "" //激励视频
    @JvmField var  GMRDAd_ID_TWO = "" //激励视频


    //对自然量和非自然量的退出后台时的唤醒开屏的逻辑控制，为true时，在自然量的情况下不会在退出后台时重新进入开屏页。
    var specialExitFlag=false//虽然这个不是常量，但是这个和SWITCH_LEAVE_RETURN_LAUNCH_NORMAL搭配使用，这个设置为true时，处理白名单模式下需要取消唤醒开屏的特殊操作

    @JvmField
    var adsJLFlag = 0

    @JvmField
    var adsCPTabFlag = 0

    @JvmField
    var adsCPNormalFlag = 0

    var oaid = ""
    var oaid_u = ""
    var oaid_h = ""

    private val urls = when (CHANNEL) {
        "CSJ" -> Pair(
            "", //用户协议
            ""    //隐私政策
        )
        else -> Pair(
            "https://app.dingjiwangluo.com/protocol/mould/agreement/2e8e43a9-d1b2-4325-8fae-0981bab2a1be.html", //用户协议  上海亮侨
            "https://app.dingjiwangluo.com/protocol/mould/privacy/e361e4a5-889c-48bf-8a85-e8437d976ab2.html"    //隐私政策 上海亮侨
        )
    }

    @JvmField val URL_USER_AGREEMENT1 = urls.first
    @JvmField val URL_PRIVACY_POLICY1 = urls.second

    @JvmField
    var URL_USER_AGREEMENT =
        if (TextUtils.isEmpty(BuildConfig.URL_USER_AGREEMENT)) URL_USER_AGREEMENT1 else BuildConfig.URL_USER_AGREEMENT//用户协议MK

    @JvmField
    var URL_PRIVACY_POLICY =
        if (TextUtils.isEmpty(BuildConfig.URL_PRIVACY_POLICY)) URL_PRIVACY_POLICY1 else BuildConfig.URL_PRIVACY_POLICY//用户协议MK;//隐私政策 MK

    //----------------------------广告包相关的常量和变量请写在这个区域，后续会考虑将这个区域里有用的属性加入到模版中来----------------------------start
    var myInstallReferrer = ""
    //----------------------------广告包相关的常量和变量请写在这个区域，后续会考虑将这个区域里有用的属性加入到模版中来----------------------------end


    //----------------------------模板变量----------------------------start
    var isStopBoolen = false //判断是否隐藏图标
    var isSuspendedBoolen = true  //开启悬浮窗权限的时候 和开启系统权限

    @JvmField
    var adsFlag = 0

    @JvmField
    var adsInfoFlag = 0

    @JvmField
    var showAdHeadTitle = ""

    @JvmField
    var isLoadCpAd = false //因插屏展示率低，当点击功能，则进行缓存插屏广告，功能内展示插屏，则不需要在主页面进行展示
    var splashInfoShowMainCP = false

    @JvmField
    var isStopped = false

    @JvmField
    var isWaked = false

    @JvmField
    var riskInfo = ""

    @JvmField
    var AndroidId = ""

    //change CpuViewLoader.BD_APPSID
    const val UM_ID = "" //友盟

    @JvmField
    var is_ban_status = false //是否封禁
    var is_lockSwitch = false //锁屏信息流开关
    var is_motivationVideo = false //首次激励视频
    lateinit var BAIDU_APP_ID: String // 保存接口返回的baidu_appid

    var is_show_ad = false //是否显示广告
    var is_curr_channel = "0" //当前安装渠道，0：自然量、1：巨量引擎、2：磁力引擎
    var is_adDelay = false
    var is_install = false //是否调用install接口了
    var DEVICE_OUT_NET_ID = "0.0.0.0"
    var DEVICE_OUT_NET_LOCATION = "0.0.0.0"
    var isPowerUninstalled = false //是否执行强力卸载
    var is_short = true //是否设置了强力卸载
    var splashIsJumpMain = false

    @JvmField
    var isFront = false //判断首页是不是在前台
    var GetWebViewUserAgent = ""  //getWebViewUserAgent(instance) 因在install接口报错，所以提前进行获取
    const val SWITCH_LEAVE_RETURN_LAUNCH_NORMAL=true
    @JvmField
    var photoExitFlag=false

    //----------------------------模板变量----------------------------end


    //----------------------------模板常量（不常修改）----------------------------start
    const val BASE_URL = "https://api.dingjiwangluo.com"
    const val PATH_SEGMENTS_URL = "/dj-tools-api/" //PathSegments

    const val TAG_PRE = "TMediationSDK_DEMO_"

    const val CHAPING = "chaping"
    const val QUANPING = "quanping"
    const val JILIVOID = "jilivoid"
    const val KAIPING = "kaiping"
    const val XINGXINLIU = "nat"
    const val INSTALL_FROM_APP = 1
    const val INSTALL_FROM_SPLASH = 2

    const val IAPP_SCENE = "iapp"
    const val OAPP_SCENE = "oapp"
    const val REPORT_TYPE_REQUEST = 2
    const val REPORT_TYPE_SHOW = 0
    const val REPORT_TYPE_CLICK = 1
    const val REPORT_TYPE_REQUEST_OK = 3
    //----------------------------模板常量（不常修改）----------------------------end


    val languageList = listOf(
        LanguageEntity("Portuguese", com.catcsyun.liantadog.R.drawable.ic_portugal),
        LanguageEntity("China", com.catcsyun.liantadog.R.drawable.ic_china),
        LanguageEntity("English", com.catcsyun.liantadog.R.drawable.ic_english),
        LanguageEntity("Hindi", com.catcsyun.liantadog.R.drawable.ic_hindi),
        LanguageEntity("Spanish", com.catcsyun.liantadog.R.drawable.ic_spanish),
        LanguageEntity("French", com.catcsyun.liantadog.R.drawable.ic_france),
    )

    fun dogSoundList(context: Context) = listOf(
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_10),
            com.catcsyun.liantadog.R.mipmap.ic_dog_dance,
            com.catcsyun.liantadog.R.raw.dog_dance
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_2),
            com.catcsyun.liantadog.R.mipmap.ic_dog_let_play,
            com.catcsyun.liantadog.R.raw.dog_playful_chirping
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_1),
            com.catcsyun.liantadog.R.mipmap.ic_dog_curious,
            com.catcsyun.liantadog.R.raw.dog_curious_sniff
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_5),
            com.catcsyun.liantadog.R.mipmap.ic_dog_happy,
            com.catcsyun.liantadog.R.raw.dog_happy
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_33),
            com.catcsyun.liantadog.R.mipmap.ic_dog_adoring_chatter,
            com.catcsyun.liantadog.R.raw.dog_adoring_chatter
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_6),
            com.catcsyun.liantadog.R.mipmap.ic_dog_cry,
            com.catcsyun.liantadog.R.raw.dog_cry
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_13),
            com.catcsyun.liantadog.R.mipmap.ic_dog_happy_cry,
            com.catcsyun.liantadog.R.raw.dog_happy_cry
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_21),
            com.catcsyun.liantadog.R.mipmap.ic_dog_raise_hand,
            com.catcsyun.liantadog.R.raw.dog_raise_hand
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_22),
            com.catcsyun.liantadog.R.mipmap.ic_dog_sad,
            com.catcsyun.liantadog.R.raw.dog_sad
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_15),
            com.catcsyun.liantadog.R.mipmap.ic_dog_hi_fence,
            com.catcsyun.liantadog.R.raw.dog_hi_fence
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_17),
            com.catcsyun.liantadog.R.mipmap.ic_dog_lie,
            com.catcsyun.liantadog.R.raw.dog_lie
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_8),
            com.catcsyun.liantadog.R.mipmap.ic_dog_begging,
            com.catcsyun.liantadog.R.raw.dog_begging
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_34),
            com.catcsyun.liantadog.R.mipmap.ic_dog_affectionate_barking,
            com.catcsyun.liantadog.R.raw.dog_affectionate_barking
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_35),
            com.catcsyun.liantadog.R.mipmap.ic_dog_affectionate_howl,
            com.catcsyun.liantadog.R.raw.dog_affectionate_howl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_18),
            com.catcsyun.liantadog.R.mipmap.ic_dog_love,
            com.catcsyun.liantadog.R.raw.dog_love
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_104),
            com.catcsyun.liantadog.R.mipmap.ic_dog_sleepy_sigh,
            com.catcsyun.liantadog.R.raw.dog_sleepy_sigh
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_105),
            com.catcsyun.liantadog.R.mipmap.ic_dog_thankful_whistle,
            com.catcsyun.liantadog.R.raw.dog_thankful_whistle
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_11),
            com.catcsyun.liantadog.R.mipmap.ic_dog_exhauted,
            com.catcsyun.liantadog.R.raw.dog_exhausted
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_12),
            com.catcsyun.liantadog.R.mipmap.ic_dog_hand_clap,
            com.catcsyun.liantadog.R.raw.dog_handclap
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_23),
            com.catcsyun.liantadog.R.mipmap.ic_dog_scratch,
            com.catcsyun.liantadog.R.raw.dog_scratch
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_24),
            com.catcsyun.liantadog.R.mipmap.ic_dog_shy,
            com.catcsyun.liantadog.R.raw.dog_shy
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_9),
            com.catcsyun.liantadog.R.mipmap.ic_dog_cry_lying,
            com.catcsyun.liantadog.R.raw.dog_cry_lying
        ),

        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_31),
            com.catcsyun.liantadog.R.mipmap.ic_dog_yeah,
            com.catcsyun.liantadog.R.raw.dog_yeah
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_32),
            com.catcsyun.liantadog.R.mipmap.ic_dog_yes,
            com.catcsyun.liantadog.R.raw.dog_yes
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_7),
            com.catcsyun.liantadog.R.mipmap.ic_dog_agree,
            com.catcsyun.liantadog.R.raw.dog_agree
        ),

        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_3),
            com.catcsyun.liantadog.R.mipmap.ic_dog_angry,
            com.catcsyun.liantadog.R.raw.dog_angry
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_14),
            com.catcsyun.liantadog.R.mipmap.ic_dog_happy_walk,
            com.catcsyun.liantadog.R.raw.dog_happy_walk
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_16),
            com.catcsyun.liantadog.R.mipmap.ic_dog_hi,
            com.catcsyun.liantadog.R.raw.dog_hi
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_19),
            com.catcsyun.liantadog.R.mipmap.ic_dog_no,
            com.catcsyun.liantadog.R.raw.dog_no
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_20),
            com.catcsyun.liantadog.R.mipmap.ic_dog_pet,
            com.catcsyun.liantadog.R.raw.dog_pet
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_48),
            com.catcsyun.liantadog.R.mipmap.ic_dog_cheerful_growl,
            com.catcsyun.liantadog.R.raw.dog_cheerful_growl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_49),
            com.catcsyun.liantadog.R.mipmap.ic_dog_cheerful_quacking,
            com.catcsyun.liantadog.R.raw.dog_cheerful_quacking
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_25),
            com.catcsyun.liantadog.R.mipmap.ic_dog_soft_angry,
            com.catcsyun.liantadog.R.raw.dog_soft_angry
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_59),
            com.catcsyun.liantadog.R.mipmap.ic_dog_ecstatic_barking,
            com.catcsyun.liantadog.R.raw.dog_ecstatic_barking
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_60),
            com.catcsyun.liantadog.R.mipmap.ic_dog_ecstatic_peep,
            com.catcsyun.liantadog.R.raw.dog_ecstatic_peep
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_26),
            com.catcsyun.liantadog.R.mipmap.ic_dog_soft_begging,
            com.catcsyun.liantadog.R.raw.dog_soft_begging
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_4),
            com.catcsyun.liantadog.R.mipmap.ic_dog_friendly,
            com.catcsyun.liantadog.R.raw.dog_friendly_greet
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_27),
            com.catcsyun.liantadog.R.mipmap.ic_dog_startle,
            com.catcsyun.liantadog.R.raw.dog_startle
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_28),
            com.catcsyun.liantadog.R.mipmap.ic_dog_super_angry,
            com.catcsyun.liantadog.R.raw.dog_super_angry
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_29),
            com.catcsyun.liantadog.R.mipmap.ic_dog_wonder,
            com.catcsyun.liantadog.R.raw.dog_wonder
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_30),
            com.catcsyun.liantadog.R.mipmap.ic_dog_wow,
            com.catcsyun.liantadog.R.raw.dog_wow
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_36),
            com.catcsyun.liantadog.R.mipmap.ic_dog_affectionate_lick,
            com.catcsyun.liantadog.R.raw.dog_affectionate_lick
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_37),
            com.catcsyun.liantadog.R.mipmap.ic_dog_affectionate_neigh,
            com.catcsyun.liantadog.R.raw.dog_affectionate_neigh
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_52),
            com.catcsyun.liantadog.R.mipmap.ic_dog_contented_ruff,
            com.catcsyun.liantadog.R.raw.dog_contented_ruff
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_53),
            com.catcsyun.liantadog.R.mipmap.ic_dog_curious_sniff,
            com.catcsyun.liantadog.R.raw.dog_curious_sniff
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_38),
            com.catcsyun.liantadog.R.mipmap.ic_dog_affectionate_nuzzle,
            com.catcsyun.liantadog.R.raw.dog_affectionate_nuzzle
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_39),
            com.catcsyun.liantadog.R.mipmap.ic_dog_alert_bark,
            com.catcsyun.liantadog.R.raw.dog_alert_bark
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_40),
            com.catcsyun.liantadog.R.mipmap.ic_dog_amused_honking,
            com.catcsyun.liantadog.R.raw.dog_amused_honking
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_41),
            com.catcsyun.liantadog.R.mipmap.ic_dog_amused_roaring,
            com.catcsyun.liantadog.R.raw.dog_amused_roaring
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_42),
            com.catcsyun.liantadog.R.mipmap.ic_dog_amused_squeak,
            com.catcsyun.liantadog.R.raw.dog_amused_squeak
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_43),
            com.catcsyun.liantadog.R.mipmap.ic_dog_anxious_howl,
            com.catcsyun.liantadog.R.raw.dog_anxious_howl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_46),
            com.catcsyun.liantadog.R.mipmap.ic_dog_blissful_roar,
            com.catcsyun.liantadog.R.raw.dog_blissful_roar
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_47),
            com.catcsyun.liantadog.R.mipmap.ic_dog_cautious_growl,
            com.catcsyun.liantadog.R.raw.dog_cautious_growl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_50),
            com.catcsyun.liantadog.R.mipmap.ic_dog_confused_yelp,
            com.catcsyun.liantadog.R.raw.dog_confused_yelp
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_51),
            com.catcsyun.liantadog.R.mipmap.ic_dog_contented_cluck,
            com.catcsyun.liantadog.R.raw.dog_contented_cluck
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_54),
            com.catcsyun.liantadog.R.mipmap.ic_dog_delighted_trilling,
            com.catcsyun.liantadog.R.raw.dog_delighted_trilling
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_55),
            com.catcsyun.liantadog.R.mipmap.ic_dog_delighted_whisted,
            com.catcsyun.liantadog.R.raw.dog_delighted_whistle
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_56),
            com.catcsyun.liantadog.R.mipmap.ic_dog_delighted_yelp,
            com.catcsyun.liantadog.R.raw.dog_delighted_yelp
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_57),
            com.catcsyun.liantadog.R.mipmap.ic_dog_eager_pant,
            com.catcsyun.liantadog.R.raw.dog_eager_pant
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_58),
            com.catcsyun.liantadog.R.mipmap.ic_dog_eager_whine,
            com.catcsyun.liantadog.R.raw.dog_eager_whine
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_61),
            com.catcsyun.liantadog.R.mipmap.ic_dog_elated_snuffing,
            com.catcsyun.liantadog.R.raw.dog_elated_snuffling
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_62),
            com.catcsyun.liantadog.R.mipmap.ic_dog_elated_trill,
            com.catcsyun.liantadog.R.raw.dog_elated_trill
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_63),
            com.catcsyun.liantadog.R.mipmap.ic_dog_euphoric_crooning,
            com.catcsyun.liantadog.R.raw.dog_euphoric_crooning
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_70),
            com.catcsyun.liantadog.R.mipmap.ic_dog_gleeful_coo,
            com.catcsyun.liantadog.R.raw.dog_gleeful_coo
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_71),
            com.catcsyun.liantadog.R.mipmap.ic_dog_gleeful_squealing,
            com.catcsyun.liantadog.R.raw.dog_gleeful_squealing
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_64),
            com.catcsyun.liantadog.R.mipmap.ic_dog_excitable_crowing,
            com.catcsyun.liantadog.R.raw.dog_excitable_crowing
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_65),
            com.catcsyun.liantadog.R.mipmap.ic_dog_excitable_chase,
            com.catcsyun.liantadog.R.raw.dog_excited_chase
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_66),
            com.catcsyun.liantadog.R.mipmap.ic_dog_excited_chirp,
            com.catcsyun.liantadog.R.raw.dog_excited_chirp
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_67),
            com.catcsyun.liantadog.R.mipmap.ic_dog_excited_woof,
            com.catcsyun.liantadog.R.raw.dog_excited_woof
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_68),
            com.catcsyun.liantadog.R.mipmap.ic_dog_friendly_greet,
            com.catcsyun.liantadog.R.raw.dog_friendly_greet
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_69),
            com.catcsyun.liantadog.R.mipmap.ic_dog_frightened_whine,
            com.catcsyun.liantadog.R.raw.dog_frightened_whine
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_72),
            com.catcsyun.liantadog.R.mipmap.ic_dog_grumpy_grumble,
            com.catcsyun.liantadog.R.raw.dog_grumpy_grumble
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_73),
            com.catcsyun.liantadog.R.mipmap.ic_dog_happy_bark,
            com.catcsyun.liantadog.R.raw.dog_happy_bark
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_76),
            com.catcsyun.liantadog.R.mipmap.ic_dog_hopeful_yelp,
            com.catcsyun.liantadog.R.raw.dog_hopeful_yelp
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_77),
            com.catcsyun.liantadog.R.mipmap.ic_dog_hungry_chew,
            com.catcsyun.liantadog.R.raw.dog_hungry_chew
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_44),
            com.catcsyun.liantadog.R.mipmap.ic_dog_blissful_chatter,
            com.catcsyun.liantadog.R.raw.dog_blissful_chatter
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_45),
            com.catcsyun.liantadog.R.mipmap.ic_dog_blissful_gobbling,
            com.catcsyun.liantadog.R.raw.dog_blissful_gobbling
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_78),
            com.catcsyun.liantadog.R.mipmap.ic_dog_jealous_hiss,
            com.catcsyun.liantadog.R.raw.dog_jealous_hiss
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_79),
            com.catcsyun.liantadog.R.mipmap.ic_dog_jovial_mumble,
            com.catcsyun.liantadog.R.raw.dog_jovial_mumble
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_80),
            com.catcsyun.liantadog.R.mipmap.ic_dog_jovial_squeal,
            com.catcsyun.liantadog.R.raw.dog_jovial_squeal
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_83),
            com.catcsyun.liantadog.R.mipmap.ic_dog_loving_hoot,
            com.catcsyun.liantadog.R.raw.dog_loving_hoot
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_84),
            com.catcsyun.liantadog.R.mipmap.ic_dog_loving_hooting,
            com.catcsyun.liantadog.R.raw.dog_loving_hooting
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_85),
            com.catcsyun.liantadog.R.mipmap.ic_dog_loving_snarl,
            com.catcsyun.liantadog.R.raw.dog_loving_snarl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_99),
            com.catcsyun.liantadog.R.mipmap.ic_dog_playfule_yip,
            com.catcsyun.liantadog.R.raw.dog_playful_yip
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_100),
            com.catcsyun.liantadog.R.mipmap.ic_dog_pleased_hiss,
            com.catcsyun.liantadog.R.raw.dog_pleased_hiss
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_86),
            com.catcsyun.liantadog.R.mipmap.ic_dog_loving_whimper,
            com.catcsyun.liantadog.R.raw.dog_loving_whimper
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_87),
            com.catcsyun.liantadog.R.mipmap.ic_dog_merry_trumpeting,
            com.catcsyun.liantadog.R.raw.dog_merry_trumpeting
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_88),
            com.catcsyun.liantadog.R.mipmap.ic_dog_mournful_sob,
            com.catcsyun.liantadog.R.raw.dog_mournful_sob
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_89),
            com.catcsyun.liantadog.R.mipmap.ic_dog_nervous_tremor,
            com.catcsyun.liantadog.R.raw.dog_nervous_tremor
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_90),
            com.catcsyun.liantadog.R.mipmap.ic_dog_overjoyed_purr,
            com.catcsyun.liantadog.R.raw.dog_overjoyed_purr
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_91),
            com.catcsyun.liantadog.R.mipmap.ic_dog_overjoyed_screech,
            com.catcsyun.liantadog.R.raw.dog_overjoyed_screech
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_74),
            com.catcsyun.liantadog.R.mipmap.ic_dog_happy_dance,
            com.catcsyun.liantadog.R.raw.dog_happy_dance
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_75),
            com.catcsyun.liantadog.R.mipmap.ic_dog_happy_wag,
            com.catcsyun.liantadog.R.raw.dog_happy_wag
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_92),
            com.catcsyun.liantadog.R.mipmap.ic_dog_overjoued_sniveling,
            com.catcsyun.liantadog.R.raw.dog_overjoyed_sniveling
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_93),
            com.catcsyun.liantadog.R.mipmap.ic_dog_peaceful_snarl,
            com.catcsyun.liantadog.R.raw.dog_peaceful_snarl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_94),
            com.catcsyun.liantadog.R.mipmap.ic_dog_playful_bleat,
            com.catcsyun.liantadog.R.raw.dog_playful_bleat
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_95),
            com.catcsyun.liantadog.R.mipmap.ic_dog_playful_chirping,
            com.catcsyun.liantadog.R.raw.dog_playful_chirping
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_96),
            com.catcsyun.liantadog.R.mipmap.ic_dog_playful_hiss,
            com.catcsyun.liantadog.R.raw.dog_playful_hiss
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_97),
            com.catcsyun.liantadog.R.mipmap.ic_dog_playful_nudge,
            com.catcsyun.liantadog.R.raw.dog_playful_nudge
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_98),
            com.catcsyun.liantadog.R.mipmap.ic_dog_playful_squeak,
            com.catcsyun.liantadog.R.raw.dog_playful_squeak
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_101),
            com.catcsyun.liantadog.R.mipmap.ic_dog_pleased_rumbling,
            com.catcsyun.liantadog.R.raw.dog_pleased_rumbling
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_102),
            com.catcsyun.liantadog.R.mipmap.ic_dog_relaxed_purr,
            com.catcsyun.liantadog.R.raw.dog_relaxed_purr
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_103),
            com.catcsyun.liantadog.R.mipmap.ic_dog_satisfied_grunt,
            com.catcsyun.liantadog.R.raw.dog_satisfied_grunt
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_106),
            com.catcsyun.liantadog.R.mipmap.ic_dog_thirsty_lap,
            com.catcsyun.liantadog.R.raw.dog_thirsty_lap
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_81),
            com.catcsyun.liantadog.R.mipmap.ic_dog_joyful_howl,
            com.catcsyun.liantadog.R.raw.dog_joyful_howl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_82),
            com.catcsyun.liantadog.R.mipmap.ic_dog_jubilant_whinny,
            com.catcsyun.liantadog.R.raw.dog_jubilant_whinny
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_107),
            com.catcsyun.liantadog.R.mipmap.ic_dog_thrilled_braying,
            com.catcsyun.liantadog.R.raw.dog_thrilled_braying
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_108),
            com.catcsyun.liantadog.R.mipmap.ic_dog_trusting_whuff,
            com.catcsyun.liantadog.R.raw.dog_trusting_whuff
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.dog_sound_109),
            com.catcsyun.liantadog.R.mipmap.ic_dog_upbeat_whickering,
            com.catcsyun.liantadog.R.raw.dog_upbeat_whickering
        ),
    )

    fun catSoundList(context: Context) = listOf(

        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_5),
            com.catcsyun.liantadog.R.mipmap.ic_cat_fine,
            com.catcsyun.liantadog.R.raw.cat_fine
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_32),
            com.catcsyun.liantadog.R.mipmap.ic_cat_kitten_mew,
            com.catcsyun.liantadog.R.raw.cat_kitten_mew
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_33),
            com.catcsyun.liantadog.R.mipmap.ic_cat_lilting_purr,
            com.catcsyun.liantadog.R.raw.cat_lilting_purr
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_6),
            com.catcsyun.liantadog.R.mipmap.ic_cat_angry,
            com.catcsyun.liantadog.R.raw.dog_angry
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_7),
            com.catcsyun.liantadog.R.mipmap.ic_cat_no_no,
            com.catcsyun.liantadog.R.raw.cat_nono
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_10),
            com.catcsyun.liantadog.R.mipmap.ic_cat_happy,
            com.catcsyun.liantadog.R.raw.cat_yes
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_11),
            com.catcsyun.liantadog.R.mipmap.ic_cat_tired,
            com.catcsyun.liantadog.R.raw.cat_leave_me_alone
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_20),
            com.catcsyun.liantadog.R.mipmap.ic_cat_defensive_hiss,
            com.catcsyun.liantadog.R.raw.cat_defensive_hiss
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_21),
            com.catcsyun.liantadog.R.mipmap.ic_cat_edgy_yowl,
            com.catcsyun.liantadog.R.raw.cat_edgy_yowl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_12),
            com.catcsyun.liantadog.R.mipmap.ic_cat_sleep,
            com.catcsyun.liantadog.R.raw.cat_come_here
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_13),
            com.catcsyun.liantadog.R.mipmap.ic_cat_anxious_moan,
            com.catcsyun.liantadog.R.raw.cat_anxious_moan
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_14),
            com.catcsyun.liantadog.R.mipmap.ic_cat_bass_meow,
            com.catcsyun.liantadog.R.raw.cat_bass_meow
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_17),
            com.catcsyun.liantadog.R.mipmap.ic_cat_cheery_yowl,
            com.catcsyun.liantadog.R.raw.cat_cheery_yowl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_18),
            com.catcsyun.liantadog.R.mipmap.ic_cat_croaky_chirp,
            com.catcsyun.liantadog.R.raw.cat_croaky_chirp
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_19),
            com.catcsyun.liantadog.R.mipmap.ic_cat_deep_purring,
            com.catcsyun.liantadog.R.raw.cat_deep_purring
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_22),
            com.catcsyun.liantadog.R.mipmap.ic_cat_excited_trill,
            com.catcsyun.liantadog.R.raw.cat_excited_trill
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_8),
            com.catcsyun.liantadog.R.mipmap.ic_cat_sad,
            com.catcsyun.liantadog.R.raw.cat_nono
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_26),
            com.catcsyun.liantadog.R.mipmap.ic_cat_friendly_meow,
            com.catcsyun.liantadog.R.raw.cat_friendly_meow
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_1),
            com.catcsyun.liantadog.R.mipmap.ic_cat_hello,
            com.catcsyun.liantadog.R.raw.cat_hello
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_15),
            com.catcsyun.liantadog.R.mipmap.ic_cat_bossy_hiss,
            com.catcsyun.liantadog.R.raw.cat_bossy_hiss
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_16),
            com.catcsyun.liantadog.R.mipmap.ic_cat_bright_mew,
            com.catcsyun.liantadog.R.raw.cat_bright_mew
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_50),
            com.catcsyun.liantadog.R.mipmap.ic_cat_snooty_meow,
            com.catcsyun.liantadog.R.raw.cat_snooty_meow
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_59),
            com.catcsyun.liantadog.R.mipmap.ic_cat_weak_mew,
            com.catcsyun.liantadog.R.raw.cat_weak_mew
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_60),
            com.catcsyun.liantadog.R.mipmap.ic_cat_weak_whine,
            com.catcsyun.liantadog.R.raw.cat_weak_whine
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_51),
            com.catcsyun.liantadog.R.mipmap.ic_cat_soft_chirp,
            com.catcsyun.liantadog.R.raw.cat_soft_chirp
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_43),
            com.catcsyun.liantadog.R.mipmap.ic_cat_pain_meow,
            com.catcsyun.liantadog.R.raw.cat_pain_meow
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_44),
            com.catcsyun.liantadog.R.mipmap.ic_cat_petulant_yowl,
            com.catcsyun.liantadog.R.raw.cat_petulant_yowl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_41),
            com.catcsyun.liantadog.R.mipmap.ic_cat_nasally_growl,
            com.catcsyun.liantadog.R.raw.cat_nasally_growl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_53),
            com.catcsyun.liantadog.R.mipmap.ic_cat_squeak_meow,
            com.catcsyun.liantadog.R.raw.cat_squeak_meow
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_56),
            com.catcsyun.liantadog.R.mipmap.ic_cat_sweet_meow,
            com.catcsyun.liantadog.R.raw.cat_sweet_meow
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_3),
            com.catcsyun.liantadog.R.mipmap.ic_cat_come_here,
            com.catcsyun.liantadog.R.raw.cat_come_here
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_4),
            com.catcsyun.liantadog.R.mipmap.ic_cat_hungry,
            com.catcsyun.liantadog.R.raw.dog_hungry
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_57),
            com.catcsyun.liantadog.R.mipmap.ic_cat_trembling_mew,
            com.catcsyun.liantadog.R.raw.cat_trembling_mew
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_58),
            com.catcsyun.liantadog.R.mipmap.ic_cat_upbeat_mew,
            com.catcsyun.liantadog.R.raw.cat_upbeat_mew
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_61),
            com.catcsyun.liantadog.R.mipmap.ic_cat_whiny_yowl,
            com.catcsyun.liantadog.R.raw.cat_whiny_yowl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_42),
            com.catcsyun.liantadog.R.mipmap.ic_cat_orotund_meow,
            com.catcsyun.liantadog.R.raw.cat_orotund_meow
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_2),
            com.catcsyun.liantadog.R.mipmap.ic_cat_i_love_u,
            com.catcsyun.liantadog.R.raw.cat_i_love_you
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_9),
            com.catcsyun.liantadog.R.mipmap.ic_cat_fighting,
            com.catcsyun.liantadog.R.raw.cat_should_not
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_25),
            com.catcsyun.liantadog.R.mipmap.ic_cat_frail_moan,
            com.catcsyun.liantadog.R.raw.cat_frail_moan
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_27),
            com.catcsyun.liantadog.R.mipmap.ic_cat_greating_meow,
            com.catcsyun.liantadog.R.raw.cat_grating_meow
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_28),
            com.catcsyun.liantadog.R.mipmap.ic_cat_growly_meow,
            com.catcsyun.liantadog.R.raw.cat_growly_meow
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_38),
            com.catcsyun.liantadog.R.mipmap.ic_cat_low_purr,
            com.catcsyun.liantadog.R.raw.cat_low_purr
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_39),
            com.catcsyun.liantadog.R.mipmap.ic_cat_luscious_mew,
            com.catcsyun.liantadog.R.raw.cat_luscious_mew
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_29),
            com.catcsyun.liantadog.R.mipmap.ic_cat_hateful_yowl,
            com.catcsyun.liantadog.R.raw.cat_hateful_yowl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_30),
            com.catcsyun.liantadog.R.mipmap.ic_cat_high_mew,
            com.catcsyun.liantadog.R.raw.cat_high_mew
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_31),
            com.catcsyun.liantadog.R.mipmap.ic_cat_insistent_hiss,
            com.catcsyun.liantadog.R.raw.cat_insistent_hiss
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_34),
            com.catcsyun.liantadog.R.mipmap.ic_cat_long_grunt,
            com.catcsyun.liantadog.R.raw.cat_long_grunt
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_35),
            com.catcsyun.liantadog.R.mipmap.ic_cat_long_yowls,
            com.catcsyun.liantadog.R.raw.cat_long_yowls
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_54),
            com.catcsyun.liantadog.R.mipmap.ic_cat_stressed_purr,
            com.catcsyun.liantadog.R.raw.cat_stressed_purr
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_55),
            com.catcsyun.liantadog.R.mipmap.ic_cat_sullen_grunt,
            com.catcsyun.liantadog.R.raw.cat_sullen_grunt
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_36),
            com.catcsyun.liantadog.R.mipmap.ic_cat_lound_hiss,
            com.catcsyun.liantadog.R.raw.cat_lound_hiss
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_37),
            com.catcsyun.liantadog.R.mipmap.ic_cat_lovely_moan,
            com.catcsyun.liantadog.R.raw.cat_lovely_moan
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_40),
            com.catcsyun.liantadog.R.mipmap.ic_cat_mawkish_yowl,
            com.catcsyun.liantadog.R.raw.cat_mawkish_yowl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_45),
            com.catcsyun.liantadog.R.mipmap.ic_cat_raspy_meow,
            com.catcsyun.liantadog.R.raw.cat_raspy_meow
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_46),
            com.catcsyun.liantadog.R.mipmap.ic_cat_scare_chirp,
            com.catcsyun.liantadog.R.raw.cat_scare_chirp
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_23),
            com.catcsyun.liantadog.R.mipmap.ic_cat_feeble_yowl,
            com.catcsyun.liantadog.R.raw.cat_feeble_yowl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_24),
            com.catcsyun.liantadog.R.mipmap.ic_cat_fight_hiss,
            com.catcsyun.liantadog.R.raw.cat_fight_hiss
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_47),
            com.catcsyun.liantadog.R.mipmap.ic_cat_short_yowl,
            com.catcsyun.liantadog.R.raw.cat_short_yowl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_48),
            com.catcsyun.liantadog.R.mipmap.ic_cat_shrill_meow,
            com.catcsyun.liantadog.R.raw.cat_shrill_meow
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_49),
            com.catcsyun.liantadog.R.mipmap.ic_cat_snarly_snarl,
            com.catcsyun.liantadog.R.raw.cat_snarly_snarl
        ),
        Index1Entity(
            context.getString(com.catcsyun.liantadog.R.string.cat_sound_52),
            com.catcsyun.liantadog.R.mipmap.ic_cat_soft_purr,
            com.catcsyun.liantadog.R.raw.cat_soft_purr
        ),

        )


}