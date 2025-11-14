package com.cslianta.catdog.network


import com.cslianta.catdog.bean.dj.CACommonConfigBean
import com.cslianta.catdog.AppConst
import com.cslianta.catdog.bean.dj.CARiskBean
import com.cslianta.catdog.bean.dj.CAActivateBean
import com.cslianta.catdog.bean.dj.CAStartRet
import com.cslianta.catdog.bean.dj.CAWhiteListBean
import com.cslianta.catdog.bean.dj.CAHelpQuestionBean
import com.cslianta.catdog.bean.dj.CAInstallBean
import com.cslianta.catdog.bean.dj.CAOpenMemberBean
import com.cslianta.catdog.bean.dj.CAResponseBase
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
                     @Query("version") version: String,@Query("channel") channel: String): Observable<CAResponseBase<CAStartRet>>

  //Map形式

  @GET("${AppConst.PATH_SEGMENTS_URL}app/v2/config")
  fun getAppConfig(@QueryMap params: Map<String, String>): Observable<CAResponseBase<CAStartRet>>
  /**
   * 获取初始化信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/install")
  fun setInstallHttp(@Body requestBody: RequestBody): Observable<CAResponseBase<CAStartRet>>

  /**
   * 设备设置为白名单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/addWhiteList")
  fun setWhiteList(@Body requestBody: RequestBody): Observable<CAResponseBase<CAWhiteListBean>>

  @GET("${AppConst.PATH_SEGMENTS_URL}vip/commonConfig")
  fun getCommonConfig(@QueryMap params: Map<String, String>): Observable<CAResponseBase<ArrayList<CACommonConfigBean>>>

  /**
   * Vip通用配置
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}vip/issuesList")
  fun issuesList(@QueryMap params: Map<String, String>): Observable<CAResponseBase<ArrayList<CAHelpQuestionBean>>>

  /**
   *售后服务表单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}vip/afterSalesForm")
  fun afterSalesForm(@Body requestBody: RequestBody): Observable<CAResponseBase<CAOpenMemberBean>>

  /**
   *设备扩展信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/deviceInfoExtend")
  fun deviceInfoExtend(@Body requestBody: RequestBody): Observable<CAResponseBase<CAOpenMemberBean>>

  /**
   * 用户行为上报
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/report")
  fun reportingBehavior(@Body requestBody: RequestBody): Observable<CAResponseBase<CAInstallBean>>

  /**
   * 设置选项日志
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}app/unusual/action/ip")
  fun setUnsualIp(@QueryMap params: Map<String, String>): Observable<CAResponseBase<CAActivateBean>>

  /**
   * IP风险接口
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}app/v2/ipRisk")
  fun ipRisk(): Observable<CAResponseBase<CARiskBean>>

}