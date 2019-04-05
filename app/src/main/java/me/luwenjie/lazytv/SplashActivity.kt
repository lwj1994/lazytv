package me.luwenjie.lazytv

import android.content.Intent
import android.os.Bundle
import com.airbnb.mvrx.BaseMvRxActivity
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_splash.adView
import me.luwenjie.lazytv.common.load

/**
 * @author luwenjie on 2019/3/15 16:24:38
 */
class SplashActivity : BaseMvRxActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    MobileAds.initialize(this, "ca-app-pub-2002896826185542~3452525970")
    setContentView(R.layout.activity_splash)
    adView.load()
    startActivity(Intent(this, MainActivity::class.java))
    finish()
  }

  companion object {
    private const val TAG = "SplashActivity"
  }
}