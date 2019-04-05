package me.luwenjie.lazytv.player

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.mvrx.BaseMvRxFragment
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.shuyu.gsyvideoplayer.utils.FileUtils
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_player.videoPlayer
import me.luwenjie.lazytv.R
import java.io.File

/**
 * @author luwenjie on 2019/3/14 16:56:13
 */
class PlayerFragment : BaseMvRxFragment() {
  private val argument by args<Args>()
  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_player, container, false)
  }

  private val orientationUtils by lazy { OrientationUtils(requireActivity(), videoPlayer) }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    lifecycle.addObserver(PlayObserver(videoPlayer, orientationUtils))
    videoPlayer.run {
      //需要路径的
      setUp(argument.url, false, File(FileUtils.getPath()), "");
      //增加title
      titleTextView.visibility = View.VISIBLE
      //videoPlayer.setShowPauseCover(false);
      //videoPlayer.setSpeed(2f);

      //设置返回键
      backButton.visibility = View.VISIBLE


      //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
      fullscreenButton.setOnClickListener { orientationUtils.resolveByClick() }

      //videoPlayer.setBottomProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_progress));
      //videoPlayer.setDialogVolumeProgressBar(getResources().getDrawable(R.drawable.video_new_volume_progress_bg));
      //videoPlayer.setDialogProgressBar(getResources().getDrawable(R.drawable.video_new_progress));
      //videoPlayer.setBottomShowProgressBarDrawable(getResources().getDrawable(R.drawable.video_new_seekbar_progress),
      //getResources().getDrawable(R.drawable.video_new_seekbar_thumb));
      //videoPlayer.setDialogProgressColor(getResources().getColor(R.color.colorAccent), -11);

      //是否可以滑动调整
      setIsTouchWiget(false)

      //设置返回按键功能
      videoPlayer.backButton.setOnClickListener {
        requireActivity().onBackPressed()
      }

      videoPlayer.startAfterPrepared()

    }
    }

  override fun invalidate() {
  }

  companion object {
    private const val TAG = "PlayerFragment"
    fun newInstance(url: String): PlayerFragment = PlayerFragment().apply {
      arguments = Bundle().apply {
        putParcelable(MvRx.KEY_ARG, Args(url))
      }
    }
  }

  @Parcelize
  class Args(val url: String) : Parcelable
}

