package com.qingchu.wangmiao.network


import com.qingchu.wangmiao.bean.dj.WHCommonConfigBean
import com.qingchu.wangmiao.AppConst
import com.qingchu.wangmiao.bean.dj.QCJRiskBean
import com.qingchu.wangmiao.bean.dj.WHActivateBean
import com.qingchu.wangmiao.bean.dj.WHStartRet
import com.qingchu.wangmiao.bean.dj.WHWhiteListBean
import com.qingchu.wangmiao.bean.dj.WHHelpQuestionBean
import com.qingchu.wangmiao.bean.dj.WHInstallBean
import com.qingchu.wangmiao.bean.dj.WHOpenMemberBean
import com.qingchu.wangmiao.bean.dj.WHResponseBase
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
                     @Query("version") version: String,@Query("channel") channel: String): Observable<WHResponseBase<WHStartRet>>

  //Map形式

  @GET("${AppConst.PATH_SEGMENTS_URL}app/v2/config")
  fun getAppConfig(@QueryMap params: Map<String, String>): Observable<WHResponseBase<WHStartRet>>
  /**
   * 获取初始化信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/install")
  fun setInstallHttp(@Body requestBody: RequestBody): Observable<WHResponseBase<WHStartRet>>

  /**
   * 设备设置为白名单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/addWhiteList")
  fun setWhiteList(@Body requestBody: RequestBody): Observable<WHResponseBase<WHWhiteListBean>>

  @GET("${AppConst.PATH_SEGMENTS_URL}vip/commonConfig")
  fun getCommonConfig(@QueryMap params: Map<String, String>): Observable<WHResponseBase<ArrayList<WHCommonConfigBean>>>

  /**
   * Vip通用配置
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}vip/issuesList")
  fun issuesList(@QueryMap params: Map<String, String>): Observable<WHResponseBase<ArrayList<WHHelpQuestionBean>>>

  /**
   *售后服务表单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}vip/afterSalesForm")
  fun afterSalesForm(@Body requestBody: RequestBody): Observable<WHResponseBase<WHOpenMemberBean>>

  /**
   *设备扩展信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/deviceInfoExtend")
  fun deviceInfoExtend(@Body requestBody: RequestBody): Observable<WHResponseBase<WHOpenMemberBean>>

  /**
   * 用户行为上报
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/report")
  fun reportingBehavior(@Body requestBody: RequestBody): Observable<WHResponseBase<WHInstallBean>>

  /**
   * 设置选项日志
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}app/unusual/action/ip")
  fun setUnsualIp(@QueryMap params: Map<String, String>): Observable<WHResponseBase<WHActivateBean>>

  /**
   * IP风险接口
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}app/v2/ipRisk")
  fun ipRisk(): Observable<WHResponseBase<QCJRiskBean>>

}