package com.weini.catdog.network


import com.weini.catdog.bean.dj.WCCommonConfigBean
import com.weini.catdog.AppConst
import com.weini.catdog.bean.dj.WCRiskBean
import com.weini.catdog.bean.dj.WCActivateBean
import com.weini.catdog.bean.dj.WCStartRet
import com.weini.catdog.bean.dj.WCWhiteListBean
import com.weini.catdog.bean.dj.WCHelpQuestionBean
import com.weini.catdog.bean.dj.WCInstallBean
import com.weini.catdog.bean.dj.WCOpenMemberBean
import com.weini.catdog.bean.dj.WCResponseBase
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
                     @Query("version") version: String,@Query("channel") channel: String): Observable<WCResponseBase<WCStartRet>>

  //Map形式

  @GET("${AppConst.PATH_SEGMENTS_URL}app/v2/config")
  fun getAppConfig(@QueryMap params: Map<String, String>): Observable<WCResponseBase<WCStartRet>>
  /**
   * 获取初始化信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/install")
  fun setInstallHttp(@Body requestBody: RequestBody): Observable<WCResponseBase<WCStartRet>>

  /**
   * 设备设置为白名单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/addWhiteList")
  fun setWhiteList(@Body requestBody: RequestBody): Observable<WCResponseBase<WCWhiteListBean>>

  @GET("${AppConst.PATH_SEGMENTS_URL}vip/commonConfig")
  fun getCommonConfig(@QueryMap params: Map<String, String>): Observable<WCResponseBase<ArrayList<WCCommonConfigBean>>>

  /**
   * Vip通用配置
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}vip/issuesList")
  fun issuesList(@QueryMap params: Map<String, String>): Observable<WCResponseBase<ArrayList<WCHelpQuestionBean>>>

  /**
   *售后服务表单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}vip/afterSalesForm")
  fun afterSalesForm(@Body requestBody: RequestBody): Observable<WCResponseBase<WCOpenMemberBean>>

  /**
   *设备扩展信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/deviceInfoExtend")
  fun deviceInfoExtend(@Body requestBody: RequestBody): Observable<WCResponseBase<WCOpenMemberBean>>

  /**
   * 用户行为上报
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/report")
  fun reportingBehavior(@Body requestBody: RequestBody): Observable<WCResponseBase<WCInstallBean>>

  /**
   * 设置选项日志
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}app/unusual/action/ip")
  fun setUnsualIp(@QueryMap params: Map<String, String>): Observable<WCResponseBase<WCActivateBean>>

  /**
   * IP风险接口
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}app/v2/ipRisk")
  fun ipRisk(): Observable<WCResponseBase<WCRiskBean>>

}