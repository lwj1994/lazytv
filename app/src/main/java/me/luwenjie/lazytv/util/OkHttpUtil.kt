package me.luwenjie.lazytv.util

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

object OkHttpUtil {
  val okHttpClient: OkHttpClient by lazy {
    OkHttpClient()
  }

  fun get(url: String): Response? {
    val request = Request.Builder()
        .url(url)
        .addHeader("User-Agent",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
        .build()

    try {
      return okHttpClient.newCall(request).execute()
    } catch (e: IOException) {
      return null
    }
  }

}