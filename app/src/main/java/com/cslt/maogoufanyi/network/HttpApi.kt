package com.cslt.maogoufanyi.network


import com.cslt.maogoufanyi.bean.dj.HDSCommonConfigBean
import com.cslt.maogoufanyi.AppConst
import com.cslt.maogoufanyi.bean.dj.HDSJRiskBean
import com.cslt.maogoufanyi.bean.dj.HDSActivateBean
import com.cslt.maogoufanyi.bean.dj.HDSStartRet
import com.cslt.maogoufanyi.bean.dj.HDSWhiteListBean
import com.cslt.maogoufanyi.bean.dj.HDSHelpQuestionBean
import com.cslt.maogoufanyi.bean.dj.HDSInstallBean
import com.cslt.maogoufanyi.bean.dj.HDSOpenMemberBean
import com.cslt.maogoufanyi.bean.dj.HDSResponseBase
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
                     @Query("version") version: String,@Query("channel") channel: String): Observable<HDSResponseBase<HDSStartRet>>

  //Map形式

  @GET("${AppConst.PATH_SEGMENTS_URL}app/v2/config")
  fun getAppConfig(@QueryMap params: Map<String, String>): Observable<HDSResponseBase<HDSStartRet>>
  /**
   * 获取初始化信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/install")
  fun setInstallHttp(@Body requestBody: RequestBody): Observable<HDSResponseBase<HDSStartRet>>

  /**
   * 设备设置为白名单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/addWhiteList")
  fun setWhiteList(@Body requestBody: RequestBody): Observable<HDSResponseBase<HDSWhiteListBean>>

  @GET("${AppConst.PATH_SEGMENTS_URL}vip/commonConfig")
  fun getCommonConfig(@QueryMap params: Map<String, String>): Observable<HDSResponseBase<ArrayList<HDSCommonConfigBean>>>

  /**
   * Vip通用配置
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}vip/issuesList")
  fun issuesList(@QueryMap params: Map<String, String>): Observable<HDSResponseBase<ArrayList<HDSHelpQuestionBean>>>

  /**
   *售后服务表单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}vip/afterSalesForm")
  fun afterSalesForm(@Body requestBody: RequestBody): Observable<HDSResponseBase<HDSOpenMemberBean>>

  /**
   *设备扩展信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/deviceInfoExtend")
  fun deviceInfoExtend(@Body requestBody: RequestBody): Observable<HDSResponseBase<HDSOpenMemberBean>>

  /**
   * 用户行为上报
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/report")
  fun reportingBehavior(@Body requestBody: RequestBody): Observable<HDSResponseBase<HDSInstallBean>>

  /**
   * 设置选项日志
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}app/unusual/action/ip")
  fun setUnsualIp(@QueryMap params: Map<String, String>): Observable<HDSResponseBase<HDSActivateBean>>

  /**
   * IP风险接口
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}app/v2/ipRisk")
  fun ipRisk(): Observable<HDSResponseBase<HDSJRiskBean>>

}