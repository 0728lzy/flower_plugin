package com.pet.translator.network


import com.pet.translator.bean.dj.CommonConfigBean
import com.pet.translator.AppConst
import com.pet.translator.bean.dj.StartRet
import com.pet.translator.bean.dj.WhiteListBean
import com.pet.translator.bean.dj.HelpQuestionBean
import com.pet.translator.bean.dj.InstallBean
import com.pet.translator.bean.dj.OpenMemberBean
import com.pet.translator.bean.dj.ResponseBase
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
                     @Query("version") version: String,@Query("channel") channel: String): Observable<ResponseBase<StartRet>>

  //Map形式

  @GET("${AppConst.PATH_SEGMENTS_URL}app/v2/config")
  fun getAppConfig(@QueryMap params: Map<String, String>): Observable<ResponseBase<StartRet>>
  /**
   * 获取初始化信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/install")
  fun setInstallHttp(@Body requestBody: RequestBody): Observable<ResponseBase<StartRet>>

  /**
   * 设备设置为白名单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/addWhiteList")
  fun setWhiteList(@Body requestBody: RequestBody): Observable<ResponseBase<WhiteListBean>>

  @GET("${AppConst.PATH_SEGMENTS_URL}vip/commonConfig")
  fun getCommonConfig(@QueryMap params: Map<String, String>): Observable<ResponseBase<ArrayList<CommonConfigBean>>>

  /**
   * Vip通用配置
   * @param
   * @return
   * */
  @GET("${AppConst.PATH_SEGMENTS_URL}vip/issuesList")
  fun issuesList(@QueryMap params: Map<String, String>): Observable<ResponseBase<ArrayList<HelpQuestionBean>>>

  /**
   *售后服务表单
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}vip/afterSalesForm")
  fun afterSalesForm(@Body requestBody: RequestBody): Observable<ResponseBase<OpenMemberBean>>

  /**
   *设备扩展信息
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/deviceInfoExtend")
  fun deviceInfoExtend(@Body requestBody: RequestBody): Observable<ResponseBase<OpenMemberBean>>

  /**
   * 用户行为上报
   * @param
   * @return
   * */
  @POST("${AppConst.PATH_SEGMENTS_URL}app/v2/report")
  fun reportingBehavior(@Body requestBody: RequestBody): Observable<ResponseBase<InstallBean>>
}