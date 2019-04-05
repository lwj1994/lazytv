package me.luwenjie.lazytv

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import io.fabric.sdk.android.Fabric
import me.luwenjie.lazytv.util.RxUtil
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level.DEBUG

/**
 * @author luwenjie on 2019/3/13 22:12:08
 */
class App : Application() {
  override fun onCreate() {
    super.onCreate()
    context = this
    startKoin {
      androidContext(this@App)
      androidLogger(DEBUG)
      androidFileProperties()
    }
    if (BuildConfig.DEBUG){
      Stetho.initializeWithDefaults(this)
    }else{
      Fabric.with(this, Crashlytics())
    }
    RxUtil.initErrorHandler()
  }

  companion object {
    @SuppressLint("StaticFieldLeak")
    @JvmStatic
    lateinit var context: Context

    private const val TAG = "App"
  }
}