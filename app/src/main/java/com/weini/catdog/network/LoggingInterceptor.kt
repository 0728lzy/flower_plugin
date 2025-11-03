package com.weini.catdog.network

import android.util.Log
import com.weini.catdog.utils.dj.DESPlus
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.lang.StringBuilder

class LoggingInterceptor : Interceptor {
    private val TAG = "LoggingInterceptor"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val startTime = System.currentTimeMillis()
        var response: Response = chain.proceed(chain.request())
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        val responseBody = response.body ?: return response
        val mediaType = responseBody.contentType()
        val content = responseBody.string()
        Log.i(TAG, "\n")
        Log.i(TAG, "----------Start----------------")
        Log.i(TAG, "| " + request.url)
        val method = request.method
        if ("POST" == method) {
            val sb = StringBuilder()
            if (request.body is FormBody) {
                val body = request.body as FormBody?
                if (body != null) {
                    for (i in 0 until body.size) {
                        sb.append(body.encodedName(i)).append("=").append(body.encodedValue(i))
                            .append(",")
                    }
                }
                sb.delete(sb.length - 1, sb.length)
//                Log.i(TAG, "| RequestParams:{$sb}")
            }
        }
//        Log.i(TAG, "| Response:$content")
        Log.i(TAG, "----------End:" + duration + "毫秒----------")
        if (content.contains("code") && content.contains("data")) {
            return response.newBuilder()
                .body(ResponseBody.create(mediaType, content))
                .build()
        } else {
            try {
//            val gson = Gson()
//            val fromJson = gson.fromJson<ResponseDESBase>(content, ResponseDESBase::class.java)
//            val data = fromJson.data.toString()

                val desPlus = DESPlus("PKCS#8@6")
                val responseData = desPlus.decrypt(content)
//                Log.e(TAG, "bodyString====》${responseData}")
//            val replace = content.replace(data, responseData)
//            fromJson.data= responseData
//            val toJson = gson.toJson(fromJson)
//                Log.e(TAG, "bodyString====》${responseData}")
//            Log.e(TAG, "toJson====》${toJson}")
//            val replace = StringEscapeUtils.unescapeJava(toJson);
//            Log.e(TAG, "replace====》${replace}")
//            /*将解密后的明文返回*/
                val newResponseBody = ResponseBody.create(mediaType, responseData)
                return response.newBuilder().body(newResponseBody).build()
            } catch (e: Exception) {
                /*异常说明解密失败 信息被篡改 直接返回即可 */
                return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build()
            }
        }
        return response.newBuilder()
            .body(ResponseBody.create(mediaType, content))
            .build()
    }
}