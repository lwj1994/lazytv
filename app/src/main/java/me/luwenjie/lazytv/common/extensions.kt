package me.luwenjie.lazytv.common

import androidx.core.view.isVisible
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import me.luwenjie.lazytv.BuildConfig

fun AdView.load(){
  val builder = AdRequest.Builder()
  if (BuildConfig.DEBUG){
    builder.addTestDevice("D9B98AA177A07FA07E7B5AFCB25AF499").build()
  }
  loadAd(builder.build())
  adListener = object : AdListener() {
    override fun onAdClosed() {
      isVisible = false
    }

    override fun onAdClicked() {
      isVisible = false
    }
    override fun onAdOpened() {
      isVisible = true
    }
  }
}