package com.appcatdog.translations.network


import com.appcatdog.translations.bean.dj.WNCDCommonConfigBean
import com.appcatdog.translations.AppConst
import com.appcatdog.translations.bean.dj.WNCDActivateBean
import com.appcatdog.translations.bean.dj.WNCDStartRet
import com.appcatdog.translations.bean.dj.WNCDWhiteListBean
import com.appcatdog.translations.bean.dj.WNCDHelpQuestionBean
import com.appcatdog.translations.bean.dj.WNCDInstallBean
import com.appcatdog.translations.bean.dj.WNCDOpenMemberBean
import com.appcatdog.translations.bean.dj.WNCDResponseBase
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface HttpApi {

  /**
   * 获取初始化信息
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}app/config")
  fun getConfigInit( @Query("pkgName") pkgName: String,
                     @Query("version") version: String,@Query("channel") channel: String): Observable<WNCDResponseBase<WNCDStartRet>>

  //Map形式

  @GET("${AppConst.PATH_SEGMENTS_URL}app/v2/config")
  fun getAppConfig(@QueryMap params: Map<String, String>): Observable<WNCDResponseBase<WNCDStartRet>>
  /**
   * 获取初始化信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/install")
  fun setInstallHttp(@Body requestBody: RequestBody): Observable<WNCDResponseBase<WNCDStartRet>>

  /**
   * 设备设置为白名单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/addWhiteList")
  fun setWhiteList(@Body requestBody: RequestBody): Observable<WNCDResponseBase<WNCDWhiteListBean>>

  @GET("${AppConst.PATH_SEGMENTS_URL}vip/commonConfig")
  fun getCommonConfig(@QueryMap params: Map<String, String>): Observable<WNCDResponseBase<ArrayList<WNCDCommonConfigBean>>>

  /**
   * Vip通用配置
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}vip/issuesList")
  fun issuesList(@QueryMap params: Map<String, String>): Observable<WNCDResponseBase<ArrayList<WNCDHelpQuestionBean>>>

  /**
   *售后服务表单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}vip/afterSalesForm")
  fun afterSalesForm(@Body requestBody: RequestBody): Observable<WNCDResponseBase<WNCDOpenMemberBean>>

  /**
   *设备扩展信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/deviceInfoExtend")
  fun deviceInfoExtend(@Body requestBody: RequestBody): Observable<WNCDResponseBase<WNCDOpenMemberBean>>

  /**
   * 用户行为上报
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/report")
  fun reportingBehavior(@Body requestBody: RequestBody): Observable<WNCDResponseBase<WNCDInstallBean>>

  /**
   * 设置选项日志
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}app/unusual/action/ip")
  fun setUnsualIp(@QueryMap params: Map<String, String>): Observable<WNCDResponseBase<WNCDActivateBean>>


}