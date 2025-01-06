package com.ruiteapp.pettranslator.network

import android.text.TextUtils
import com.ruiteapp.pettranslator.APP
import com.ruiteapp.pettranslator.AppConst
import com.ruiteapp.pettranslator.utils.dj.DeviceUtils
import com.ruiteapp.pettranslator.utils.dj.GetHttpDataUtil
import com.ruiteapp.pettranslator.utils.dj.Md5Util
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.net.Proxy
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit


/**
 * Created by 眼神 on 2018/3/27.
 * 封装Retrofit配置
 */

class RetrofitFactory() {

    companion object {
        private val DEFAULT_CONNECT_TIMEOUT = 30
        private val DEFAULT_WRITE_TIMEOUT = 30
        private val DEFAULT_READ_TIMEOUT = 30

        //获取单例
        val instance: RetrofitFactory
            get() = SingletonHolder.INSTANCE
    }

//    var TAG = "RetrofitFactory"

    var retrofit: Retrofit? = null

    var httpApi: HttpApi? = null

    /**
     * 请求失败重连次数
     */
    private val RETRY_COUNT = 0
    private val okHttpBuilder: OkHttpClient.Builder

    init {
        //手动创建一个OkHttpClient并设置超时时间
        okHttpBuilder = OkHttpClient.Builder().proxy(Proxy.NO_PROXY)
        /**
         * 设置缓存
         */
        val cacheFile = File(APP.instance!!.externalCacheDir, APP.instance!!.packageName)
        val cache = Cache(cacheFile, (1024 * 1024 * 50).toLong())
//    val cacheInterceptor = Interceptor { chain ->
//      var request = chain.request()
//      if (NetworkUtils.isNetworkAvaiable(App.application)) {
//        request = request.newBuilder()
//          .cacheControl(CacheControl.FORCE_CACHE)
//          .build()
//      }
//      val response = chain.proceed(request)
//      if (NetworkUtils.isNetworkAvaiable(App.application)) {
//        val maxAge = 0
//        // 有网络时 设置缓存超时时间0个小时
//        response.newBuilder()
//          .header("Cache-Control", "public, max-age=$maxAge")
//          .removeHeader(App.application.packageName)// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
//          .build()
//      } else {
//        // 无网络时，设置超时为4周
//        val maxStale = 60 * 60 * 24 * 28
//        response.newBuilder()
//          .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
//          .removeHeader(App.application.packageName)
//          .build()
//      }
//      response
//    }
        okHttpBuilder.cache(cache)


        /**
         * 设置头信息
         */
        val headerInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader("X-Up-Timestamp", "${System.currentTimeMillis()}")
                .addHeader(
                    "X-Up-UA",
                    "${APP.instance!!.packageName}/${DeviceUtils.getVersionName(APP.instance)}"
                )
                .method(originalRequest.method, originalRequest.body)
            val startRet = GetHttpDataUtil.getStartRet()
            if (TextUtils.isEmpty(startRet.appId)) {
                requestBuilder.addHeader("X-Up-Key", "0")
            } else {
                var appId = startRet.appId.toString()
                requestBuilder.addHeader("X-Up-Key", appId)
            }
            val hash = getHash(requestBuilder.build())
            requestBuilder.addHeader("X-Up-Signature", hash)


            //添加请求头信息，服务器进行token有效性验证
//      requestBuilder.addHeader("Authorization", "Bearer " + BaseConstant.TOKEN)


//            val globalParam = originalRequest.url.newBuilder()
//                .addQueryParameter("X-Up-Key:", "45435")
//                .addQueryParameter("X-Up-Timestamp", "${System.currentTimeMillis()}")
//                .addQueryParameter("X-Up-Signature", hash)
//                .addQueryParameter("X-Up-UA", "${App.application.packageName}/${DeviceUtils.getVersionCode(App.instance)}"
//                )
//            requestBuilder.url(globalParam.build())

            val request = requestBuilder.build()
//            Log.i(TAG, "request----URL:==${request.url.encodedPath}")
//            Log.i(TAG, "request----method==${request.method}")
//            Log.i(TAG, "request----headers==${request.headers["Content-Type"]}")
            chain.proceed(request)

        }

        okHttpBuilder.addInterceptor(headerInterceptor)
        //设置 Debug Log 模式
        okHttpBuilder.addInterceptor(LoggingInterceptor())
        //        }

        /**
         * 设置超时和重新连接
         */
        okHttpBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(DEFAULT_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.writeTimeout(DEFAULT_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        //错误重连
        okHttpBuilder.retryOnConnectionFailure(true)


        retrofit = Retrofit.Builder()
            .client(okHttpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())//json转换成JavaBean
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(AppConst.BASE_URL)
            .build()
        httpApi = retrofit?.create(HttpApi::class.java)
    }

    //在访问HttpMethods时创建单例
    private object SingletonHolder {
        val INSTANCE = RetrofitFactory()

    }

    private fun getHash(newRequest: Request): String {
        var stringBuffer = StringBuffer()
        stringBuffer.append(newRequest.method)
        stringBuffer.append("\n")
        if (newRequest.method == "POST") {
            val buffer = Buffer()
            try {
                newRequest.body!!.writeTo(buffer)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //编码设为UTF-8
            var charset = Charset.forName("UTF-8");
            var contentType = newRequest.body!!.contentType();
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            //拿到request
            var requestString = buffer.readString(charset)
//            Log.i(TAG, "request----body==${requestString}")

            stringBuffer.append(Md5Util.getMD5(requestString).uppercase(Locale.getDefault()))
            stringBuffer.append("\n")
        }
        stringBuffer.append("${newRequest.headers["Content-Type"]}")
        stringBuffer.append("\n")
        stringBuffer.append("${"X-Up-Key:"}${newRequest.headers["X-Up-Key"]}")
        stringBuffer.append("\n")
        stringBuffer.append("${"X-Up-Timestamp:"}${newRequest.headers["X-Up-Timestamp"]}")
        stringBuffer.append("\n")
        stringBuffer.append("${"X-Up-UA:"}${newRequest.headers["X-Up-UA"]}")
        stringBuffer.append("\n")
        stringBuffer.append(newRequest.url.encodedPath)
        var hash = stringBuffer.toString()

//        Log.i(TAG, "hash = $hash")
        val mD5 = Md5Util.getMD5(hash)
//        Log.i(TAG, "mD5 = $mD5")
        return mD5
    }

    fun changeBaseUrl(baseUrl: String) {
        retrofit = Retrofit.Builder()
            .client(okHttpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(baseUrl)
            .build()
        httpApi = retrofit?.create<HttpApi>(HttpApi::class.java)
    }
}