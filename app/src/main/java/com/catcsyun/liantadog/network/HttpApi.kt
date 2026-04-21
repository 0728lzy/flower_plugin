package com.catcsyun.liantadog.network


import com.catcsyun.liantadog.bean.dj.KLTGCommonConfigBean
import com.catcsyun.liantadog.AppConst
import com.catcsyun.liantadog.bean.dj.KLTGRiskBean
import com.catcsyun.liantadog.bean.dj.KLTGActivateBean
import com.catcsyun.liantadog.bean.dj.KLTGStartRet
import com.catcsyun.liantadog.bean.dj.KLTGWhiteListBean
import com.catcsyun.liantadog.bean.dj.KLTGHelpQuestionBean
import com.catcsyun.liantadog.bean.dj.KLTGInstallBean
import com.catcsyun.liantadog.bean.dj.KLTGOpenMemberBean
import com.catcsyun.liantadog.bean.dj.KLTGResponseBase
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
                     @Query("version") version: String,@Query("channel") channel: String): Observable<KLTGResponseBase<KLTGStartRet>>

  //Map形式

  @GET("${AppConst.PATH_SEGMENTS_URL}app/v2/config")
  fun getAppConfig(@QueryMap params: Map<String, String>): Observable<KLTGResponseBase<KLTGStartRet>>
  /**
   * 获取初始化信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/install")
  fun setInstallHttp(@Body requestBody: RequestBody): Observable<KLTGResponseBase<KLTGStartRet>>

  /**
   * 设备设置为白名单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/addWhiteList")
  fun setWhiteList(@Body requestBody: RequestBody): Observable<KLTGResponseBase<KLTGWhiteListBean>>

  @GET("${AppConst.PATH_SEGMENTS_URL}vip/commonConfig")
  fun getCommonConfig(@QueryMap params: Map<String, String>): Observable<KLTGResponseBase<ArrayList<KLTGCommonConfigBean>>>

  /**
   * Vip通用配置
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}vip/issuesList")
  fun issuesList(@QueryMap params: Map<String, String>): Observable<KLTGResponseBase<ArrayList<KLTGHelpQuestionBean>>>

  /**
   *售后服务表单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}vip/afterSalesForm")
  fun afterSalesForm(@Body requestBody: RequestBody): Observable<KLTGResponseBase<KLTGOpenMemberBean>>

  /**
   *设备扩展信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/deviceInfoExtend")
  fun deviceInfoExtend(@Body requestBody: RequestBody): Observable<KLTGResponseBase<KLTGOpenMemberBean>>

  /**
   * 用户行为上报
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/report")
  fun reportingBehavior(@Body requestBody: RequestBody): Observable<KLTGResponseBase<KLTGInstallBean>>

  /**
   * 设置选项日志
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}app/unusual/action/ip")
  fun setUnsualIp(@QueryMap params: Map<String, String>): Observable<KLTGResponseBase<KLTGActivateBean>>

  /**
   * IP风险接口
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}app/v2/ipRisk")
  fun ipRisk(): Observable<KLTGResponseBase<KLTGRiskBean>>

}