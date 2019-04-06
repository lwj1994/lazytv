package me.luwenjie.lazytv.main.subscribe.channel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.transaction
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseActivity

/**
 * @author luwenjie on 2019/4/6 17:54:18
 */
class ChannelActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.view_container)
    supportFragmentManager.transaction {
      replace(R.id.container, SubscribeChannelFragment.newInstance(intent.getParcelableExtra(ARGS)))
    }
  }

  companion object {
    private const val TAG = "ChannelActivity"
    private const val ARGS = "ChannelActivityArgs"
    fun newIntent(context: Context, args: SubscribeChannelFragmentArgs) = Intent(context,
        ChannelActivity::class.java).apply {
      putExtra(ARGS, args)
    }
  }
}