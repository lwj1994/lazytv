package me.luwenjie.lazytv.player

import androidx.lifecycle.Lifecycle.Event.ON_CREATE
import androidx.lifecycle.Lifecycle.Event.ON_DESTROY
import androidx.lifecycle.Lifecycle.Event.ON_PAUSE
import androidx.lifecycle.Lifecycle.Event.ON_RESUME
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import me.luwenjie.lazytv.common.gsyplayer.SampleVideo

/**
 * @author luwenjie on 2019/3/14 18:04:14
 */
class PlayObserver(private val videoPlayer: SampleVideo,
    private val orientationUtils: OrientationUtils) : LifecycleObserver {


  @OnLifecycleEvent(ON_CREATE)
  fun onCreate() {

  }

  @OnLifecycleEvent(ON_RESUME)
  fun onResume() {
    videoPlayer.onVideoResume()
  }

  @OnLifecycleEvent(ON_PAUSE)
  fun onPause() {
    videoPlayer.onVideoPause()
  }

  @OnLifecycleEvent(ON_DESTROY)
  fun onDestroy() {
    orientationUtils.releaseListener()
  }


  companion object {
    private const val TAG = "PlayObserver"
  }
}