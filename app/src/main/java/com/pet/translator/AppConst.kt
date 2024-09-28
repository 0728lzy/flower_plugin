package com.pet.translator

import android.Manifest
import android.content.Context
import com.pet.translator.entity.Index1Entity
import com.pet.translator.entity.LanguageEntity


object AppConst {

    //----------------------------模板常量（常修改）----------------------------start
    const val CHANNEL = "XIAOMI" //CSJ HUAWEI BAIDU OPPO
    const val DJ_APP_ID = "a92c610a88358742855638431ac72034ec"
    const val UM_APP_ID = "66f505a7f5ffeb6b15c00b64"
    const val TAG = "ad_log"
    //GroMore
    const val Ad_ID = "5611545" //穿山甲广告APP ID
    const val GMCPAd_ID_IN = "103159170" //插屏应用内
    const val GMCPAd_ID_IN_TWO = "103159250" //插屏应用内2
    const val FEEDSIMPLE_ID_ONE = "103159171" //信息流首页1
    const val FEEDSIMPLE_ID_TWO = "103159249" //信息流首页2
    const val GMSPAd_ID = "103159339" //开屏ID
    const val GMDDAd_ID = "890007337" //快报兜底开屏id
    const val GMSPAd_TWO_ID = "103158975" //开屏ID 2
    const val GMDDAd_TWO_ID = "890007338" //快报兜底开屏id 2
    const val GMRDAd_ID_IN = "103158878" //激励视频
    //用户协议和隐私政策
    const val APP_HOST = "云翼"  //建议每次修改协议时，同时修改这里的主体名，这样就可以保证出包主体不会错！
    const val URL_USER_AGREEMENT =
        "https://app.xiaodanzi.com/protocol/mould/agreement/0d592a18-231e-4ce4-adee-af681dd8f618.html"//用户协议MK
    const val URL_PRIVACY_POLICY =
        "https://app.xiaodanzi.com/protocol/mould/privacy/d8799336-9438-419a-ac61-459d4b7435dd.html"//隐私政策 MK
    //----------------------------模板常量（常修改）----------------------------end


    //----------------------------白包相关的常量和变量请写在这个区域，修改广告不要改这个区域----------------------------start
    //----------------------------白包相关的常量和变量请写在这个区域，修改广告不要改这个区域----------------------------end


    //----------------------------广告包相关的常量和变量请写在这个区域，后续会考虑将这个区域里有用的属性加入到模版中来----------------------------start
    //----------------------------广告包相关的常量和变量请写在这个区域，后续会考虑将这个区域里有用的属性加入到模版中来----------------------------end


    //----------------------------模板变量----------------------------start
    var isStopBoolen = false //判断是否隐藏图标
    var isSuspendedBoolen = true  //开启悬浮窗权限的时候 和开启系统权限
    @JvmField
    var adsFlag=0
    @JvmField
    var showAdHeadTitle = ""
    @JvmField
    var isLoadCpAd = false //因插屏展示率低，当点击功能，则进行缓存插屏广告，功能内展示插屏，则不需要在主页面进行展示
    var splashInfoShowMainCP = false
    @JvmField
    var isStopped = false
    @JvmField
    var isWaked=false
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

    var oaid = ""
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

    /**
     * 要申请的权限字段
     */
    enum class PERMISSONURL constructor(val value: String, val errorMsg: String) {
        WRITE_EXTERNAL(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            "你已拒绝存储权限，请在设置或安全中心里开启"
        ), // 写入权限
        READ_EXTERNAL(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            "你已拒绝存储权限，请在设置或安全中心里开启"
        ), //读取权限
        READ_PHONE(
            Manifest.permission.READ_PHONE_STATE,
            "你已拒绝获取手机设备信息权限，请在设置或安全中心里开启"
        ), //获取设备号权限
        LOCATION(
            "android.permission.ACCESS_COARSE_LOCATION",
            "你已拒绝定位权限，请在设置或安全中心里开启"
        ), //摄像头权限
        FINE_LOCATION(
            Manifest.permission.ACCESS_FINE_LOCATION,
            "你已拒绝定位权限，请在设置或安全中心里开启"
        ), //摄像头权限
        CAMERA(Manifest.permission.CAMERA, "你已拒绝拍照权限，请在设置或安全中心里开启"), //摄像头权限
    }

    val languageList = listOf(
        LanguageEntity("Portuguese", com.pet.translator.R.drawable.ic_portugal),
        LanguageEntity("China", com.pet.translator.R.drawable.ic_china),
        LanguageEntity("English", com.pet.translator.R.drawable.ic_english),
        LanguageEntity("Hindi", com.pet.translator.R.drawable.ic_hindi),
        LanguageEntity("Spanish", com.pet.translator.R.drawable.ic_spanish),
        LanguageEntity("French", com.pet.translator.R.drawable.ic_france),
    )

    fun dogSoundList(context: Context) = listOf(
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_1),
            com.pet.translator.R.mipmap.ic_dog_curious,
            com.pet.translator.R.raw.dog_curious_sniff
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_2),
            com.pet.translator.R.mipmap.ic_dog_let_play,
            com.pet.translator.R.raw.dog_playful_chirping
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_3),
            com.pet.translator.R.mipmap.ic_dog_angry,
            com.pet.translator.R.raw.dog_angry
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_4),
            com.pet.translator.R.mipmap.ic_dog_friendly,
            com.pet.translator.R.raw.dog_friendly_greet
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_5),
            com.pet.translator.R.mipmap.ic_dog_happy,
            com.pet.translator.R.raw.dog_happy
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_6),
            com.pet.translator.R.mipmap.ic_dog_cry,
            com.pet.translator.R.raw.dog_cry
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_7),
            com.pet.translator.R.mipmap.ic_dog_agree,
            com.pet.translator.R.raw.dog_agree
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_8),
            com.pet.translator.R.mipmap.ic_dog_begging,
            com.pet.translator.R.raw.dog_begging
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_9),
            com.pet.translator.R.mipmap.ic_dog_cry_lying,
            com.pet.translator.R.raw.dog_cry_lying
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_10),
            com.pet.translator.R.mipmap.ic_dog_dance,
            com.pet.translator.R.raw.dog_dance
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_11),
            com.pet.translator.R.mipmap.ic_dog_exhauted,
            com.pet.translator.R.raw.dog_exhausted
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_12),
            com.pet.translator.R.mipmap.ic_dog_hand_clap,
            com.pet.translator.R.raw.dog_handclap
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_13),
            com.pet.translator.R.mipmap.ic_dog_happy_cry,
            com.pet.translator.R.raw.dog_happy_cry
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_14),
            com.pet.translator.R.mipmap.ic_dog_happy_walk,
            com.pet.translator.R.raw.dog_happy_walk
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_15),
            com.pet.translator.R.mipmap.ic_dog_hi_fence,
            com.pet.translator.R.raw.dog_hi_fence
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_16),
            com.pet.translator.R.mipmap.ic_dog_hi,
            com.pet.translator.R.raw.dog_hi
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_17),
            com.pet.translator.R.mipmap.ic_dog_lie,
            com.pet.translator.R.raw.dog_lie
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_18),
            com.pet.translator.R.mipmap.ic_dog_love,
            com.pet.translator.R.raw.dog_love
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_19),
            com.pet.translator.R.mipmap.ic_dog_no,
            com.pet.translator.R.raw.dog_no
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_20),
            com.pet.translator.R.mipmap.ic_dog_pet,
            com.pet.translator.R.raw.dog_pet
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_21),
            com.pet.translator.R.mipmap.ic_dog_raise_hand,
            com.pet.translator.R.raw.dog_raise_hand
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_22),
            com.pet.translator.R.mipmap.ic_dog_sad,
            com.pet.translator.R.raw.dog_sad
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_23),
            com.pet.translator.R.mipmap.ic_dog_scratch,
            com.pet.translator.R.raw.dog_scratch
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_24),
            com.pet.translator.R.mipmap.ic_dog_shy,
            com.pet.translator.R.raw.dog_shy
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_25),
            com.pet.translator.R.mipmap.ic_dog_soft_angry,
            com.pet.translator.R.raw.dog_soft_angry
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_26),
            com.pet.translator.R.mipmap.ic_dog_soft_begging,
            com.pet.translator.R.raw.dog_soft_begging
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_27),
            com.pet.translator.R.mipmap.ic_dog_startle,
            com.pet.translator.R.raw.dog_startle
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_28),
            com.pet.translator.R.mipmap.ic_dog_super_angry,
            com.pet.translator.R.raw.dog_super_angry
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_29),
            com.pet.translator.R.mipmap.ic_dog_wonder,
            com.pet.translator.R.raw.dog_wonder
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_30),
            com.pet.translator.R.mipmap.ic_dog_wow,
            com.pet.translator.R.raw.dog_wow
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_31),
            com.pet.translator.R.mipmap.ic_dog_yeah,
            com.pet.translator.R.raw.dog_yeah
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_32),
            com.pet.translator.R.mipmap.ic_dog_yes,
            com.pet.translator.R.raw.dog_yes
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_33),
            com.pet.translator.R.mipmap.ic_dog_adoring_chatter,
            com.pet.translator.R.raw.dog_adoring_chatter
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_34),
            com.pet.translator.R.mipmap.ic_dog_affectionate_barking,
            com.pet.translator.R.raw.dog_affectionate_barking
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_35),
            com.pet.translator.R.mipmap.ic_dog_affectionate_howl,
            com.pet.translator.R.raw.dog_affectionate_howl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_36),
            com.pet.translator.R.mipmap.ic_dog_affectionate_lick,
            com.pet.translator.R.raw.dog_affectionate_lick
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_37),
            com.pet.translator.R.mipmap.ic_dog_affectionate_neigh,
            com.pet.translator.R.raw.dog_affectionate_neigh
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_38),
            com.pet.translator.R.mipmap.ic_dog_affectionate_nuzzle,
            com.pet.translator.R.raw.dog_affectionate_nuzzle
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_39),
            com.pet.translator.R.mipmap.ic_dog_alert_bark,
            com.pet.translator.R.raw.dog_alert_bark
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_40),
            com.pet.translator.R.mipmap.ic_dog_amused_honking,
            com.pet.translator.R.raw.dog_amused_honking
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_41),
            com.pet.translator.R.mipmap.ic_dog_amused_roaring,
            com.pet.translator.R.raw.dog_amused_roaring
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_42),
            com.pet.translator.R.mipmap.ic_dog_amused_squeak,
            com.pet.translator.R.raw.dog_amused_squeak
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_43),
            com.pet.translator.R.mipmap.ic_dog_anxious_howl,
            com.pet.translator.R.raw.dog_anxious_howl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_44),
            com.pet.translator.R.mipmap.ic_dog_blissful_chatter,
            com.pet.translator.R.raw.dog_blissful_chatter
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_45),
            com.pet.translator.R.mipmap.ic_dog_blissful_gobbling,
            com.pet.translator.R.raw.dog_blissful_gobbling
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_46),
            com.pet.translator.R.mipmap.ic_dog_blissful_roar,
            com.pet.translator.R.raw.dog_blissful_roar
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_47),
            com.pet.translator.R.mipmap.ic_dog_cautious_growl,
            com.pet.translator.R.raw.dog_cautious_growl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_48),
            com.pet.translator.R.mipmap.ic_dog_cheerful_growl,
            com.pet.translator.R.raw.dog_cheerful_growl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_49),
            com.pet.translator.R.mipmap.ic_dog_cheerful_quacking,
            com.pet.translator.R.raw.dog_cheerful_quacking
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_50),
            com.pet.translator.R.mipmap.ic_dog_confused_yelp,
            com.pet.translator.R.raw.dog_confused_yelp
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_51),
            com.pet.translator.R.mipmap.ic_dog_contented_cluck,
            com.pet.translator.R.raw.dog_contented_cluck
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_52),
            com.pet.translator.R.mipmap.ic_dog_contented_ruff,
            com.pet.translator.R.raw.dog_contented_ruff
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_53),
            com.pet.translator.R.mipmap.ic_dog_curious_sniff,
            com.pet.translator.R.raw.dog_curious_sniff
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_54),
            com.pet.translator.R.mipmap.ic_dog_delighted_trilling,
            com.pet.translator.R.raw.dog_delighted_trilling
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_55),
            com.pet.translator.R.mipmap.ic_dog_delighted_whisted,
            com.pet.translator.R.raw.dog_delighted_whistle
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_56),
            com.pet.translator.R.mipmap.ic_dog_delighted_yelp,
            com.pet.translator.R.raw.dog_delighted_yelp
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_57),
            com.pet.translator.R.mipmap.ic_dog_eager_pant,
            com.pet.translator.R.raw.dog_eager_pant
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_58),
            com.pet.translator.R.mipmap.ic_dog_eager_whine,
            com.pet.translator.R.raw.dog_eager_whine
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_59),
            com.pet.translator.R.mipmap.ic_dog_ecstatic_barking,
            com.pet.translator.R.raw.dog_ecstatic_barking
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_60),
            com.pet.translator.R.mipmap.ic_dog_ecstatic_peep,
            com.pet.translator.R.raw.dog_ecstatic_peep
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_61),
            com.pet.translator.R.mipmap.ic_dog_elated_snuffing,
            com.pet.translator.R.raw.dog_elated_snuffling
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_62),
            com.pet.translator.R.mipmap.ic_dog_elated_trill,
            com.pet.translator.R.raw.dog_elated_trill
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_63),
            com.pet.translator.R.mipmap.ic_dog_euphoric_crooning,
            com.pet.translator.R.raw.dog_euphoric_crooning
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_64),
            com.pet.translator.R.mipmap.ic_dog_excitable_crowing,
            com.pet.translator.R.raw.dog_excitable_crowing
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_65),
            com.pet.translator.R.mipmap.ic_dog_excitable_chase,
            com.pet.translator.R.raw.dog_excited_chase
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_66),
            com.pet.translator.R.mipmap.ic_dog_excited_chirp,
            com.pet.translator.R.raw.dog_excited_chirp
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_67),
            com.pet.translator.R.mipmap.ic_dog_excited_woof,
            com.pet.translator.R.raw.dog_excited_woof
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_68),
            com.pet.translator.R.mipmap.ic_dog_friendly_greet,
            com.pet.translator.R.raw.dog_friendly_greet
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_69),
            com.pet.translator.R.mipmap.ic_dog_frightened_whine,
            com.pet.translator.R.raw.dog_frightened_whine
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_70),
            com.pet.translator.R.mipmap.ic_dog_gleeful_coo,
            com.pet.translator.R.raw.dog_gleeful_coo
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_71),
            com.pet.translator.R.mipmap.ic_dog_gleeful_squealing,
            com.pet.translator.R.raw.dog_gleeful_squealing
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_72),
            com.pet.translator.R.mipmap.ic_dog_grumpy_grumble,
            com.pet.translator.R.raw.dog_grumpy_grumble
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_73),
            com.pet.translator.R.mipmap.ic_dog_happy_bark,
            com.pet.translator.R.raw.dog_happy_bark
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_74),
            com.pet.translator.R.mipmap.ic_dog_happy_dance,
            com.pet.translator.R.raw.dog_happy_dance
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_75),
            com.pet.translator.R.mipmap.ic_dog_happy_wag,
            com.pet.translator.R.raw.dog_happy_wag
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_76),
            com.pet.translator.R.mipmap.ic_dog_hopeful_yelp,
            com.pet.translator.R.raw.dog_hopeful_yelp
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_77),
            com.pet.translator.R.mipmap.ic_dog_hungry_chew,
            com.pet.translator.R.raw.dog_hungry_chew
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_78),
            com.pet.translator.R.mipmap.ic_dog_jealous_hiss,
            com.pet.translator.R.raw.dog_jealous_hiss
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_79),
            com.pet.translator.R.mipmap.ic_dog_jovial_mumble,
            com.pet.translator.R.raw.dog_jovial_mumble
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_80),
            com.pet.translator.R.mipmap.ic_dog_jovial_squeal,
            com.pet.translator.R.raw.dog_jovial_squeal
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_81),
            com.pet.translator.R.mipmap.ic_dog_joyful_howl,
            com.pet.translator.R.raw.dog_joyful_howl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_82),
            com.pet.translator.R.mipmap.ic_dog_jubilant_whinny,
            com.pet.translator.R.raw.dog_jubilant_whinny
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_83),
            com.pet.translator.R.mipmap.ic_dog_loving_hoot,
            com.pet.translator.R.raw.dog_loving_hoot
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_84),
            com.pet.translator.R.mipmap.ic_dog_loving_hooting,
            com.pet.translator.R.raw.dog_loving_hooting
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_85),
            com.pet.translator.R.mipmap.ic_dog_loving_snarl,
            com.pet.translator.R.raw.dog_loving_snarl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_86),
            com.pet.translator.R.mipmap.ic_dog_loving_whimper,
            com.pet.translator.R.raw.dog_loving_whimper
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_87),
            com.pet.translator.R.mipmap.ic_dog_merry_trumpeting,
            com.pet.translator.R.raw.dog_merry_trumpeting
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_88),
            com.pet.translator.R.mipmap.ic_dog_mournful_sob,
            com.pet.translator.R.raw.dog_mournful_sob
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_89),
            com.pet.translator.R.mipmap.ic_dog_nervous_tremor,
            com.pet.translator.R.raw.dog_nervous_tremor
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_90),
            com.pet.translator.R.mipmap.ic_dog_overjoyed_purr,
            com.pet.translator.R.raw.dog_overjoyed_purr
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_91),
            com.pet.translator.R.mipmap.ic_dog_overjoyed_screech,
            com.pet.translator.R.raw.dog_overjoyed_screech
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_92),
            com.pet.translator.R.mipmap.ic_dog_overjoued_sniveling,
            com.pet.translator.R.raw.dog_overjoyed_sniveling
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_93),
            com.pet.translator.R.mipmap.ic_dog_peaceful_snarl,
            com.pet.translator.R.raw.dog_peaceful_snarl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_94),
            com.pet.translator.R.mipmap.ic_dog_playful_bleat,
            com.pet.translator.R.raw.dog_playful_bleat
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_95),
            com.pet.translator.R.mipmap.ic_dog_playful_chirping,
            com.pet.translator.R.raw.dog_playful_chirping
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_96),
            com.pet.translator.R.mipmap.ic_dog_playful_hiss,
            com.pet.translator.R.raw.dog_playful_hiss
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_97),
            com.pet.translator.R.mipmap.ic_dog_playful_nudge,
            com.pet.translator.R.raw.dog_playful_nudge
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_98),
            com.pet.translator.R.mipmap.ic_dog_playful_squeak,
            com.pet.translator.R.raw.dog_playful_squeak
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_99),
            com.pet.translator.R.mipmap.ic_dog_playfule_yip,
            com.pet.translator.R.raw.dog_playful_yip
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_100),
            com.pet.translator.R.mipmap.ic_dog_pleased_hiss,
            com.pet.translator.R.raw.dog_pleased_hiss
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_101),
            com.pet.translator.R.mipmap.ic_dog_pleased_rumbling,
            com.pet.translator.R.raw.dog_pleased_rumbling
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_102),
            com.pet.translator.R.mipmap.ic_dog_relaxed_purr,
            com.pet.translator.R.raw.dog_relaxed_purr
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_103),
            com.pet.translator.R.mipmap.ic_dog_satisfied_grunt,
            com.pet.translator.R.raw.dog_satisfied_grunt
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_104),
            com.pet.translator.R.mipmap.ic_dog_sleepy_sigh,
            com.pet.translator.R.raw.dog_sleepy_sigh
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_105),
            com.pet.translator.R.mipmap.ic_dog_thankful_whistle,
            com.pet.translator.R.raw.dog_thankful_whistle
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_106),
            com.pet.translator.R.mipmap.ic_dog_thirsty_lap,
            com.pet.translator.R.raw.dog_thirsty_lap
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_107),
            com.pet.translator.R.mipmap.ic_dog_thrilled_braying,
            com.pet.translator.R.raw.dog_thrilled_braying
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_108),
            com.pet.translator.R.mipmap.ic_dog_trusting_whuff,
            com.pet.translator.R.raw.dog_trusting_whuff
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.dog_sound_109),
            com.pet.translator.R.mipmap.ic_dog_upbeat_whickering,
            com.pet.translator.R.raw.dog_upbeat_whickering
        ),
    )

    fun catSoundList(context: Context) = listOf(
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_1),
            com.pet.translator.R.mipmap.ic_cat_hello,
            com.pet.translator.R.raw.cat_hello
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_2),
            com.pet.translator.R.mipmap.ic_cat_i_love_u,
            com.pet.translator.R.raw.cat_i_love_you
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_3),
            com.pet.translator.R.mipmap.ic_cat_come_here,
            com.pet.translator.R.raw.cat_come_here
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_4),
            com.pet.translator.R.mipmap.ic_cat_hungry,
            com.pet.translator.R.raw.dog_hungry
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_5),
            com.pet.translator.R.mipmap.ic_cat_fine,
            com.pet.translator.R.raw.cat_fine
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_6),
            com.pet.translator.R.mipmap.ic_cat_angry,
            com.pet.translator.R.raw.dog_angry
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_7),
            com.pet.translator.R.mipmap.ic_cat_no_no,
            com.pet.translator.R.raw.cat_nono
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_8),
            com.pet.translator.R.mipmap.ic_cat_sad,
            com.pet.translator.R.raw.cat_nono
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_9),
            com.pet.translator.R.mipmap.ic_cat_fighting,
            com.pet.translator.R.raw.cat_should_not
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_10),
            com.pet.translator.R.mipmap.ic_cat_happy,
            com.pet.translator.R.raw.cat_yes
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_11),
            com.pet.translator.R.mipmap.ic_cat_tired,
            com.pet.translator.R.raw.cat_leave_me_alone
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_12),
            com.pet.translator.R.mipmap.ic_cat_sleep,
            com.pet.translator.R.raw.cat_come_here
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_13),
            com.pet.translator.R.mipmap.ic_cat_anxious_moan,
            com.pet.translator.R.raw.cat_anxious_moan
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_14),
            com.pet.translator.R.mipmap.ic_cat_bass_meow,
            com.pet.translator.R.raw.cat_bass_meow
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_15),
            com.pet.translator.R.mipmap.ic_cat_bossy_hiss,
            com.pet.translator.R.raw.cat_bossy_hiss
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_16),
            com.pet.translator.R.mipmap.ic_cat_bright_mew,
            com.pet.translator.R.raw.cat_bright_mew
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_17),
            com.pet.translator.R.mipmap.ic_cat_cheery_yowl,
            com.pet.translator.R.raw.cat_cheery_yowl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_18),
            com.pet.translator.R.mipmap.ic_cat_croaky_chirp,
            com.pet.translator.R.raw.cat_croaky_chirp
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_19),
            com.pet.translator.R.mipmap.ic_cat_deep_purring,
            com.pet.translator.R.raw.cat_deep_purring
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_20),
            com.pet.translator.R.mipmap.ic_cat_defensive_hiss,
            com.pet.translator.R.raw.cat_defensive_hiss
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_21),
            com.pet.translator.R.mipmap.ic_cat_edgy_yowl,
            com.pet.translator.R.raw.cat_edgy_yowl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_22),
            com.pet.translator.R.mipmap.ic_cat_excited_trill,
            com.pet.translator.R.raw.cat_excited_trill
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_23),
            com.pet.translator.R.mipmap.ic_cat_feeble_yowl,
            com.pet.translator.R.raw.cat_feeble_yowl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_24),
            com.pet.translator.R.mipmap.ic_cat_fight_hiss,
            com.pet.translator.R.raw.cat_fight_hiss
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_25),
            com.pet.translator.R.mipmap.ic_cat_frail_moan,
            com.pet.translator.R.raw.cat_frail_moan
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_26),
            com.pet.translator.R.mipmap.ic_cat_friendly_meow,
            com.pet.translator.R.raw.cat_friendly_meow
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_27),
            com.pet.translator.R.mipmap.ic_cat_greating_meow,
            com.pet.translator.R.raw.cat_grating_meow
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_28),
            com.pet.translator.R.mipmap.ic_cat_growly_meow,
            com.pet.translator.R.raw.cat_growly_meow
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_29),
            com.pet.translator.R.mipmap.ic_cat_hateful_yowl,
            com.pet.translator.R.raw.cat_hateful_yowl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_30),
            com.pet.translator.R.mipmap.ic_cat_high_mew,
            com.pet.translator.R.raw.cat_high_mew
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_31),
            com.pet.translator.R.mipmap.ic_cat_insistent_hiss,
            com.pet.translator.R.raw.cat_insistent_hiss
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_32),
            com.pet.translator.R.mipmap.ic_cat_kitten_mew,
            com.pet.translator.R.raw.cat_kitten_mew
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_33),
            com.pet.translator.R.mipmap.ic_cat_lilting_purr,
            com.pet.translator.R.raw.cat_lilting_purr
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_34),
            com.pet.translator.R.mipmap.ic_cat_long_grunt,
            com.pet.translator.R.raw.cat_long_grunt
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_35),
            com.pet.translator.R.mipmap.ic_cat_long_yowls,
            com.pet.translator.R.raw.cat_long_yowls
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_36),
            com.pet.translator.R.mipmap.ic_cat_lound_hiss,
            com.pet.translator.R.raw.cat_lound_hiss
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_37),
            com.pet.translator.R.mipmap.ic_cat_lovely_moan,
            com.pet.translator.R.raw.cat_lovely_moan
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_38),
            com.pet.translator.R.mipmap.ic_cat_low_purr,
            com.pet.translator.R.raw.cat_low_purr
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_39),
            com.pet.translator.R.mipmap.ic_cat_luscious_mew,
            com.pet.translator.R.raw.cat_luscious_mew
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_40),
            com.pet.translator.R.mipmap.ic_cat_mawkish_yowl,
            com.pet.translator.R.raw.cat_mawkish_yowl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_41),
            com.pet.translator.R.mipmap.ic_cat_nasally_growl,
            com.pet.translator.R.raw.cat_nasally_growl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_42),
            com.pet.translator.R.mipmap.ic_cat_orotund_meow,
            com.pet.translator.R.raw.cat_orotund_meow
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_43),
            com.pet.translator.R.mipmap.ic_cat_pain_meow,
            com.pet.translator.R.raw.cat_pain_meow
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_44),
            com.pet.translator.R.mipmap.ic_cat_petulant_yowl,
            com.pet.translator.R.raw.cat_petulant_yowl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_45),
            com.pet.translator.R.mipmap.ic_cat_raspy_meow,
            com.pet.translator.R.raw.cat_raspy_meow
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_46),
            com.pet.translator.R.mipmap.ic_cat_scare_chirp,
            com.pet.translator.R.raw.cat_scare_chirp
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_47),
            com.pet.translator.R.mipmap.ic_cat_short_yowl,
            com.pet.translator.R.raw.cat_short_yowl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_48),
            com.pet.translator.R.mipmap.ic_cat_shrill_meow,
            com.pet.translator.R.raw.cat_shrill_meow
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_49),
            com.pet.translator.R.mipmap.ic_cat_snarly_snarl,
            com.pet.translator.R.raw.cat_snarly_snarl
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_50),
            com.pet.translator.R.mipmap.ic_cat_snooty_meow,
            com.pet.translator.R.raw.cat_snooty_meow
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_51),
            com.pet.translator.R.mipmap.ic_cat_soft_chirp,
            com.pet.translator.R.raw.cat_soft_chirp
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_52),
            com.pet.translator.R.mipmap.ic_cat_soft_purr,
            com.pet.translator.R.raw.cat_soft_purr
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_53),
            com.pet.translator.R.mipmap.ic_cat_squeak_meow,
            com.pet.translator.R.raw.cat_squeak_meow
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_54),
            com.pet.translator.R.mipmap.ic_cat_stressed_purr,
            com.pet.translator.R.raw.cat_stressed_purr
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_55),
            com.pet.translator.R.mipmap.ic_cat_sullen_grunt,
            com.pet.translator.R.raw.cat_sullen_grunt
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_56),
            com.pet.translator.R.mipmap.ic_cat_sweet_meow,
            com.pet.translator.R.raw.cat_sweet_meow
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_57),
            com.pet.translator.R.mipmap.ic_cat_trembling_mew,
            com.pet.translator.R.raw.cat_trembling_mew
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_58),
            com.pet.translator.R.mipmap.ic_cat_upbeat_mew,
            com.pet.translator.R.raw.cat_upbeat_mew
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_59),
            com.pet.translator.R.mipmap.ic_cat_weak_mew,
            com.pet.translator.R.raw.cat_weak_mew
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_60),
            com.pet.translator.R.mipmap.ic_cat_weak_whine,
            com.pet.translator.R.raw.cat_weak_whine
        ),
        Index1Entity(context.getString(com.pet.translator.R.string.cat_sound_61),
            com.pet.translator.R.mipmap.ic_cat_whiny_yowl,
            com.pet.translator.R.raw.cat_whiny_yowl
        ),
    )


}